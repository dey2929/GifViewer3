package com.int403.jabong.gifviewer3.glide.manager;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * A factory class that produces a functional
 * {@link com.int403.jabong.gifviewer3.glide.manager.ConnectivityMonitor}.
 */
public interface ConnectivityMonitorFactory {

  @NonNull
  ConnectivityMonitor build(
          @NonNull Context context,
          @NonNull ConnectivityMonitor.ConnectivityListener listener);
}
