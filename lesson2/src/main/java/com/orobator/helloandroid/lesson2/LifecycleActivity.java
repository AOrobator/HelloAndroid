package com.orobator.helloandroid.lesson2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * As a user navigates through, out of, and back to your app, the Activity
 * instances in your app transition through different states in their
 * lifecycle. The Activity class provides a number of callbacks that allow the
 * activity to know that a state has changed: that the system is creating,
 * stopping, or resuming an activity, or destroying the process in which the
 * activity resides.
 *
 * Within the lifecycle callback methods, you can declare how your activity
 * behaves when the user leaves and re-enters the activity. For example, if
 * you're building a streaming video player, you might pause the video and
 * terminate the network connection when the user switches to another app.
 * When the user returns, you can reconnect to the network and allow the user
 * to resume the video from the same spot. In other words, each callback
 * allows you to perform specific work that's appropriate to a given change of
 * state. Doing the right work at the right time and handling transitions
 * properly make your app more robust and performant. For example, good
 * implementation of the lifecycle callbacks can help ensure that your app
 * avoids:
 *
 * - Crashing if the user receives a phone call or switches to another app
 * while using your app.
 *
 * - Consuming valuable system resources when the user is not actively
 * using it.
 *
 * - Losing the user's progress if they leave your app and return to it at a
 * later time.
 *
 * - Crashing or losing the user's progress when the screen rotates between
 * landscape and portrait orientation.
 *
 * See https://developer.android.com/guide/components/activities/activity-lifecycle
 * for more info
 */
public class LifecycleActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}
