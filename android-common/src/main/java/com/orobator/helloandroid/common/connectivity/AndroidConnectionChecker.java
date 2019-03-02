package com.orobator.helloandroid.common.connectivity;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidConnectionChecker implements ConnectionChecker {
  // Require an Application instead of an Activity. There's rarely ever a case
  // where we want to store a reference to an Activity. Since we don't control
  // the lifecycle, if we hold onto an Activity after it has been destroyed, we
  // will leak memory.
  private final Application app;

  public AndroidConnectionChecker(Application app) {
    this.app = app;
  }

  @Override
  public boolean isConnected() {
    ConnectivityManager cm =
        (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
