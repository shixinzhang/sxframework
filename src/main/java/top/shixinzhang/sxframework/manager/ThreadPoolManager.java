package top.shixinzhang.sxframework.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * <br> 线程池
 * <p>
 * <br> Created by shixinzhang on 17/5/12.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class ThreadPoolManager {
    private final String TAG = this.getClass().getSimpleName();
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2; // 核心线程数为 CPU数＊2
    private static final int MAXIMUM_POOL_SIZE = 64;    // 线程队列最大线程数
    private static final int KEEP_ALIVE_TIME = 1;    // 保持存活时间 1秒

    private final BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue<>(128);

    private final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, TAG + " #" + mCount.getAndIncrement());
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    };

    private ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, mWorkQueue, DEFAULT_THREAD_FACTORY,
            new ThreadPoolExecutor.DiscardOldestPolicy());

    private static volatile ThreadPoolManager mInstance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return mInstance;
    }

    public void addTask(Runnable runnable) {
        mExecutor.execute(runnable);
    }

    @Deprecated
    public void shutdownNow() {
        mExecutor.shutdownNow();
    }
}
