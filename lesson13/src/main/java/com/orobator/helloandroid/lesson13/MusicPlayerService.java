package com.orobator.helloandroid.lesson13;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

public class MusicPlayerService extends Service {
  private static final String TAG = "MusicPlayerService";

  @Override public void onCreate() {
    super.onCreate();

    Log.d(TAG, "onCreate()");
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand()");

    return super.onStartCommand(intent, flags, startId);
  }

  @Override public void onDestroy() {
    super.onDestroy();

    Log.d(TAG, "onDestroy()");
  }
}
