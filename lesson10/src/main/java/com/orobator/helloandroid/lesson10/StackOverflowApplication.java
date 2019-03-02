package com.orobator.helloandroid.lesson10;

import android.app.Activity;
import android.app.Application;
import com.orobator.helloandroid.lesson10.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

public class StackOverflowApplication extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

  @Override public void onCreate() {
    super.onCreate();

    DaggerAppComponent
        .builder()
        .application(this)
        .build()
        .inject(this);
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingAndroidInjector;
  }
}
