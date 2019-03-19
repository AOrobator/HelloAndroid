package com.orobator.helloandroid.lesson12;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
  private static final String TAG = "NotificationReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.i(TAG, "Notification Button Pressed");
  }
}
