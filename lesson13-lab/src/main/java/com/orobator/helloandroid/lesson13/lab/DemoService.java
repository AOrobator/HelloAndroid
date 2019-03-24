package com.orobator.helloandroid.lesson13.lab;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

public class DemoService extends Service {
  private static final String TAG = DemoService.class.getSimpleName();
  private static final String ACTION_START = "start";
  private static final String ACTION_STOP = "stop";

  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, DemoService.class);
    intent.setAction(ACTION_START);
    return intent;
  }

  public static Intent getStopIntent(Context context) {
    Intent intent = new Intent(context, DemoService.class);
    intent.setAction(ACTION_STOP);
    return intent;
  }

  @Override public void onCreate() {
    super.onCreate();

    Log.d(TAG, "onCreate()");
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    Log.d(TAG, "onBind()");
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    String action = intent.getAction();

    Log.d(TAG, "onStartCommand(" + action + ")");

    if (ACTION_START.equals(action)) {
      Log.d(TAG, "Service Started");
    } else if (ACTION_STOP.equals(action)) {
      Log.d(TAG, "Stopping Service...");
      stopSelf();
    }

    return super.onStartCommand(intent, flags, startId);
  }

  @Override public void onDestroy() {
    super.onDestroy();

    Log.d(TAG, "onDestroy()");
  }
}
