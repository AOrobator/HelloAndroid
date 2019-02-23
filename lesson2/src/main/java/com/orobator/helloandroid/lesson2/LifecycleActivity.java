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
    // which goes to the error level.

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

  /**
   * When the activity enters the Started state, the system invokes this callback. The onStart()
   * call makes the activity visible to the user, as the app prepares for the activity to enter the
   * foreground and become interactive. For example, this method is where the app initializes the
   * code that maintains the UI.
   *
   * The onStart() method completes very quickly and, as with the Created state, the activity does
   * not stay in the Started state for long. Once this callback finishes, the activity enters the
   * Resumed state, and the system invokes the onResume() method.
   */
  @Override
  protected void onStart() {
    super.onStart();

    Log.d(TAG, "onStart()");
  }

  /**
   * When the activity enters the Resumed state, it comes to the foreground, and then the system
   * invokes the onResume() callback. This is the state in which the app interacts with the user.
   * The app stays in this state until something happens to take focus away from the app. Such an
   * event might be, for instance, receiving a phone call, the user’s navigating to another
   * activity, or the device screen’s turning off.
   *
   * When the activity is Resumed, enable any functionality that is needed to run while visible and
   * in the foreground, such as starting a camera preview.
   *
   * When an interruptive event occurs, the activity enters the Paused state, and the system
   * invokes the onPause() callback.
   *
   * If the activity returns to the Resumed state from the Paused state, the system once again
   * calls onResume() method. For this reason, you should implement onResume() to initialize
   * components that you release during onPause(), and perform any other initializations that must
   * occur each time the activity enters the Resumed state.
   *
   * Note that with multi-window, it's possible for your app to be fully visible in the Paused
   * state.
   *
   * Be aware of how you want to interact with other apps. A video player may want to setup and
   * teardown in onStart() and onStop() instead of onResume() and onPause() so the video doesn't
   * stop in multi-window mode. Regardless of what startup event you use to initialize, it should
   * always be paired to the corresponding lifecycle event to tear it down.
   *
   * If you initialize in onStart(), teardown in onStop().
   * If you initialize in onResume(), teardown in onPause().
   */
  @Override
  protected void onResume() {
    super.onResume();

    Log.d(TAG, "onResume()");
  }

  /**
   * The system calls this method as the first indication that the user is leaving your activity
   * (though it does not always mean the activity is being destroyed); it indicates that the
   * activity is no longer in the foreground (though it may still be visible if the user is in
   * multi-window mode). Use the onPause() method to pause or adjust operations that should not
   * continue (or should continue in moderation) while the Activity is in the Paused state, and
   * that you expect to resume shortly. There are several reasons why an activity may enter this
   * state.
   *
   * For example:
   *
   * - Some event interrupts app execution, as described in the onResume() section. This is the
   * most common case.
   *
   * - In Android 7.0 (API level 24) or higher, multiple apps run in multi-window mode. Because
   * only one of the apps (windows) has focus at any time, the system pauses all of the other apps.
   *
   * - A new, semi-transparent activity (such as a dialog) opens. As long as the activity is still
   * partially visible but not in focus, it remains paused.
   *
   * In this method you should stop any functionality that does not need to run while the component
   * is not in the foreground, such as stopping a camera preview.
   *
   * You can also use the onPause() method to release system resources, handles to sensors (like
   * GPS), or any resources that may affect battery life while your activity is paused and the user
   * does not need them. However, as mentioned above in the onResume() section, a Paused activity
   * may still be fully visible if in multi-window mode. As such, you should consider using
   * onStop() instead of onPause() to fully release or adjust UI-related resources and operations
   * to better support multi-window mode.
   *
   * onPause() execution is very brief, and does not necessarily afford enough time to perform save
   * operations. For this reason, you should not use onPause() to save application or user data,
   * make network calls, or execute database transactions; such work may not complete before the
   * method completes. Instead, you should perform heavy-load shutdown operations during onStop().
   */
  @Override
  protected void onPause() {
    super.onPause();

    Log.d(TAG, "onPause()");
  }

  /**
   * When your activity is no longer visible to the user, it has entered the Stopped state, and the
   * system invokes the onStop() callback. This may occur, for example, when a newly launched
   * activity covers the entire screen. The system may also call onStop() when the activity has
   * finished running, and is about to be terminated.
   *
   * In the onStop() method, the app should release or adjust resources that are not needed while
   * the
   * app is not visible to the user. For example, your app might pause animations or switch from
   * fine-grained to coarse-grained location updates. Using onStop() instead of onPause() ensures
   * that UI-related work continues, even when the user is viewing your activity in multi-window
   * mode.
   *
   * You should also use onStop() to perform relatively CPU-intensive shutdown operations. For
   * example, if you can't find a more opportune time to save information to a database, you might
   * do so during onStop().
   *
   * When your activity enters the Stopped state, the Activity object is kept resident in memory:
   * It maintains all state and member information, but is not attached to the window manager. When
   * the activity resumes, the activity recalls this information. You don’t need to re-initialize
   * components that were created during any of the callback methods leading up to the Resumed
   * state. The system also keeps track of the current state for each View object in the layout, so
   * if the user entered text into an EditText widget, that content is retained so you don't need
   * to save and restore it.
   *
   * From the Stopped state, the activity either comes back to interact with the user, or the
   * activity is finished running and goes away. If the activity comes back, the system invokes
   * onRestart(). If the Activity is finished running, the system calls onDestroy().
   */
  @Override
  protected void onStop() {
    super.onStop();

    Log.d(TAG, "onStop()");
  }
}
