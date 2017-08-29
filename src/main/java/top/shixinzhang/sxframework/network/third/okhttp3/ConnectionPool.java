package top.shixinzhang.sxframework.network.third.okhttp3;

import java.lang.ref.Reference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import top.shixinzhang.sxframework.network.third.okhttp3.internal.Util;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.connection.RealConnection;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.connection.RouteDatabase;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.connection.StreamAllocation;
import top.shixinzhang.sxframework.network.third.okhttp3.internal.platform.Platform;

import static top.shixinzhang.sxframework.network.third.okhttp3.internal.Util.closeQuietly;
import static top.shixinzhang.sxframework.network.third.okhttp3.internal.platform.Platform.WARN;

/**
 * 连接池，管理 HTTP 和 SPDY 连接的复用，降低网络延迟
 * <p>
 * 请求相同地址的 HTTP 请求会共用一个连接
 * <p>
 * Manages reuse of HTTP and SPDY connections for reduced network latency. HTTP requests that share
 * the same {@link Address} may share a {@link Connection}. This class implements the policy of
 * which connections to keep open for future use.
 */
public final class ConnectionPool {
    /**
     * Background threads are used to cleanup expired connections. There will be at most a single
     * thread running per connection pool. The thread pool executor permits the pool itself to be
     * garbage collected.
     */
    private static final Executor executor = new ThreadPoolExecutor(0 /* corePoolSize */,
            Integer.MAX_VALUE /* maximumPoolSize */, 60L /* keepAliveTime */, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));

    /**
     * The maximum number of idle connections for each address.
     */
    private final int maxIdleConnections;   //空闲的 socket 最大连接数
    private final long keepAliveDurationNs;

    private final Runnable cleanupRunnable = new Runnable() {   //负责根据计数回收连接的线程
        @Override
        public void run() {
            while (true) {
                long waitNanos = cleanup(System.nanoTime());
                if (waitNanos == -1) return;
                if (waitNanos > 0) {
                    long waitMillis = waitNanos / 1000000L;
                    waitNanos -= (waitMillis * 1000000L);
                    synchronized (ConnectionPool.this) {
                        try {
                            ConnectionPool.this.wait(waitMillis, (int) waitNanos);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        }
    };

    //实际连接的双端队列，这里用作复用连接
    //主要围绕它进行 操作
    private final Deque<RealConnection> connections = new ArrayDeque<>();
    final RouteDatabase routeDatabase = new RouteDatabase();
    boolean cleanupRunning;

    /**
     * 空闲的 socket 最多连接数为 5，存活时间为 5 分钟
     * <p>
     * Create a new connection pool with tuning parameters appropriate for a single-user application.
     * The tuning parameters in this pool are subject to change in future OkHttp releases. Currently
     * this pool holds up to 5 idle connections which will be evicted after 5 minutes of inactivity.
     */
    public ConnectionPool() {
        this(5, 5, TimeUnit.MINUTES);
    }

    public ConnectionPool(int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit) {
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDurationNs = timeUnit.toNanos(keepAliveDuration);

        // Put a floor on the keep alive duration, otherwise cleanup will spin loop.
        if (keepAliveDuration <= 0) {
            throw new IllegalArgumentException("keepAliveDuration <= 0: " + keepAliveDuration);
        }
    }

    /**
     * Returns the number of idle connections in the pool.
     */
    public synchronized int idleConnectionCount() {
        int total = 0;
        for (RealConnection connection : connections) {
            if (connection.allocations.isEmpty()) total++;
        }
        return total;
    }

    /**
     * Returns total number of connections in the pool. Note that prior to OkHttp 2.7 this included
     * only idle connections and SPDY connections. Since OkHttp 2.7 this includes all connections,
     * both active and inactive. Use {@link #idleConnectionCount()} to count connections not currently
     * in use.
     */
    public synchronized int connectionCount() {
        return connections.size();
    }

    /**
     * 提供地址和流，获取一个复用的连接
     * <p>
     * Returns a recycled connection to {@code address}, or null if no such connection exists.
     */
    RealConnection get(Address address, StreamAllocation streamAllocation) {
        assert (Thread.holdsLock(this));
        for (RealConnection connection : connections) {
            //遍历每个连接，如果这个连接的地址和流目的地一致，而且还有空位，就复用这个链接，不用新建链接了
            if (connection.allocations.size() < connection.allocationLimit
                    && address.equals(connection.route().address)
                    && !connection.noNewStreams) {
                streamAllocation.acquire(connection);
                return connection;
            }
        }
        return null;
    }


    /**
     * 放入连接队列
     *
     * @param connection
     */
    void put(RealConnection connection) {
        assert (Thread.holdsLock(this));
        if (!cleanupRunning) {
            cleanupRunning = true;
            executor.execute(cleanupRunnable);
        }
        connections.add(connection);
    }

    /**
     * 提醒连接池移除 过期的空闲连接
     * <p>
     * Notify this pool that {@code connection} has become idle. Returns true if the connection has
     * been removed from the pool and should be closed.
     */
    boolean connectionBecameIdle(RealConnection connection) {
        assert (Thread.holdsLock(this));
        if (connection.noNewStreams || maxIdleConnections == 0) {
            connections.remove(connection);
            return true;
        } else {
            notifyAll(); // Awake the cleanup thread: we may have exceeded the idle connection limit.
            return false;
        }
    }

    /**
     * 空闲的，一个不留！
     * <p>
     * Close and remove all idle connections in the pool.
     */
    public void evictAll() {
        List<RealConnection> evictedConnections = new ArrayList<>();
        synchronized (this) {
            for (Iterator<RealConnection> i = connections.iterator(); i.hasNext(); ) {
                RealConnection connection = i.next();
                if (connection.allocations.isEmpty()) {
                    connection.noNewStreams = true;
                    evictedConnections.add(connection);
                    i.remove();
                }
            }
        }

        for (RealConnection connection : evictedConnections) {
            closeQuietly(connection.socket());
        }
    }

    /**
     * 为了性能考虑，在每次添加新的连接时，会处理下那些空闲很久的连接
     * <p>
     * Performs maintenance on this pool, evicting the connection that has been idle the longest if
     * either it has exceeded the keep alive limit or the idle connections limit.
     * <p>
     * <p>Returns the duration in nanos to sleep until the next scheduled call to this method. Returns
     * -1 if no further cleanups are required.
     */
    long cleanup(long now) {
        int inUseConnectionCount = 0;
        int idleConnectionCount = 0;
        RealConnection longestIdleConnection = null;
        long longestIdleDurationNs = Long.MIN_VALUE;

        // Find either a connection to evict, or the time that the next eviction is due.
        synchronized (this) {
            for (Iterator<RealConnection> i = connections.iterator(); i.hasNext(); ) {      //遍历连接池中的连接
                RealConnection connection = i.next();

                // If the connection is in use, keep searching.
                if (pruneAndGetAllocationCount(connection, now) > 0) {      //如果这个连接被引用次数大于 0，就逃过一劫
                    inUseConnectionCount++;
                    continue;
                }

                //到这里，说明这个链接没有被引用了，空闲的
                idleConnectionCount++;

                // If the connection is ready to be evicted, we're done.
                long idleDurationNs = now - connection.idleAtNanos;
                if (idleDurationNs > longestIdleDurationNs) {
                    longestIdleDurationNs = idleDurationNs;
                    longestIdleConnection = connection;
                }
            }

            if (longestIdleDurationNs >= this.keepAliveDurationNs
                    || idleConnectionCount > this.maxIdleConnections) {     //撒有哪啦，你该被回收了，从列表移除后还得关闭连接
                // We've found a connection to evict. Remove it from the list, then close it below (outside
                // of the synchronized block).
                connections.remove(longestIdleConnection);
            } else if (idleConnectionCount > 0) {
                // A connection will be ready to evict soon.
                return keepAliveDurationNs - longestIdleDurationNs;
            } else if (inUseConnectionCount > 0) {
                // All connections are in use. It'll be at least the keep alive duration 'til we run again.
                return keepAliveDurationNs;
            } else {
                // No connections, idle or in use.
                cleanupRunning = false;
                return -1;
            }
        }

        closeQuietly(longestIdleConnection.socket());

        // Cleanup again immediately.
        return 0;
    }

    /**
     * Prunes any leaked allocations and then returns the number of remaining live allocations on
     * {@code connection}. Allocations are leaked if the connection is tracking them but the
     * application code has abandoned them. Leak detection is imprecise and relies on garbage
     * collection.
     */
    private int pruneAndGetAllocationCount(RealConnection connection, long now) {
        List<Reference<StreamAllocation>> references = connection.allocations;  //获取这个链接被引用几次
        for (int i = 0; i < references.size(); ) {
            Reference<StreamAllocation> reference = references.get(i);

            if (reference.get() != null) {  //判断每个引用是否为空
                i++;
                continue;
            }

            // We've discovered a leaked allocation. This is an application bug.
            Platform.get().log(WARN, "A connection to " + connection.route().address().url()
                    + " was leaked. Did you forget to close a response body?", null);
            references.remove(i);
            connection.noNewStreams = true;

            // If this was the last allocation, the connection is eligible for immediate eviction.
            if (references.isEmpty()) {
                connection.idleAtNanos = now - keepAliveDurationNs;
                return 0;
            }
        }

        return references.size();
    }
}
