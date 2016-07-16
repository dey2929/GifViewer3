package com.int403.jabong.gifviewer3.glide.manager;

/**
 * A no-op {@link com.int403.jabong.gifviewer3.glide.manager.ConnectivityMonitor}.
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
