package com.orobator.helloandroid.lesson13;

import android.app.Application;

public class MusicApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();

    NotificationFactory.createNotificationChannel(this);
  }
}
