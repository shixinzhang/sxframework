package top.shixinzhang.sxframework.imageload.glide.manager;

/**
 * A no-op {@link top.shixinzhang.sxframework.imageload.glide.manager.ConnectivityMonitor}.
 */
class NullConnectivityMonitor implements ConnectivityMonitor {

    @Override
    public void onStart() {
        // Do nothing.
    }

    @Override
    public void onStop() {
        // Do nothing.
    }

    @Override
    public void onDestroy() {
        // Do nothing.
    }
}
