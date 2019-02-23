package com.orobator.helloandroid.lesson2;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
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
  private static final String TAG = LifecycleActivity.class.getSimpleName();

  /**
   * onCreate() is the first lifecyle method. It signals that system has first created the Activity.
   * Now the Activity is in the Created state. In the onCreate() method, you perform basic
   * application startup logic that should happen only once for the entire life of the activity.
   * For example, your implementation of onCreate() might bind data to lists, associate the activity
   * with a ViewModel, and instantiate some class-scope variables/inject dependencies.
   *
   * This method receives the parameter savedInstanceState, which is a Bundle object containing the
   * activity's previously saved state. If the activity has never existed before, the value of the
   * Bundle object is null.
   */
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // To see all these lifecycle methods in action, we'll log each of them as they happen.
    // Logging statements in Android use the Logcat. The Logcat contains system messages,
    // including stack traces and messages we write with the Log class.
    Log.d(TAG, "onCreate(" + savedInstanceState + ")");

    // Logcat messages contain a tag for grouping, a message, and an optional exception.

    // There are several different levels/severities of logs
    // V -> Verbose (Lowest Priority)
    // D -> Debug
    // I -> Info
    // W -> Warning
    // E -> Error
    // F -> Fatal
    // S -> Silent (highest priority, on which nothing is ever printed)

    // And just for fun there is
    Log.wtf(TAG, "What a terrible failure!");

    // By default the logcat prints every log level as white. This can make it
    // a lot harder to parse through logs, so I'd recommend changing the color
    // of each log level. This way it will be easy to pick out which level
    // you're interested in at a glance.

    // To do this, go to Preferences > Editor > Color Scheme > Console Colors
    // I recommend the following color scheme:

    // Debug  : 6897BB
    // Info   : 6A8759
    // Warn   : BBB529
    // Error  : FF6B68
    // Assert : 9876AA

    // If you prefer using the command line, check out https://github.com/JakeWharton/pidcat
  }
}
