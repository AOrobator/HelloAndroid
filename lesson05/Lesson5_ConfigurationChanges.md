# Lesson 5: Configuration Changes

Take any of the MV* Activities we created in the previous lesson, calculate the tip, then rotate
the screen. What happens? The device loses all of its state as if the app had just restarted! This
doesn't lead to a very good user experience. We'll definitely fix this, but first we have to
understand what's going on.

A device configuration is a set of system settings that Android uses to render our app. It includes
things such as device orientation, language settings, screen size, and more. Android will use all
the settings in this configuration to make sure our app is laid out appropriately. 

Whenever this configuration changes, (with the most common config change being rotating the device)
Android will destroy the current Activity and recreate it, making sure to use the appropriate
resources for the new configuration.

Now that we have a grasp on what's going on, we can work towards persisting our data across
configuration changes to prevent a UI reset.

There are a couple different ways we can handle this. If we were using MVP or MVC, we would use 
`onSaveInstanceState(Bundle)` and `onRestoreInstanceState(Bundle)` to maintain our view state.
These are 2 additional lifecyle methods that happen in addition to the core 6 mentioned earlier.

```java
import androidx.annotation.Nullable;

class TipCalcActivity extends Activity {
  /**
   * Called to retrieve per-instance state from an activity before being killed
   * so that the state can be restored in {@link #onCreate} or
   * {@link #onRestoreInstanceState} (the {@link Bundle} populated by this method
   * will be passed to both).
   *
   * Called after onStop()
   */
  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putString("KEY_GRAND_TOTAL", grandTotalTextView.getText().toString());
    // Save other views
  }

  /**
   * This method is called after {@link #onStart} when the activity is
   * being re-initialized from a previously saved state, given here in
   * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
   * to restore their state, but it is sometimes convenient to do it here
   * after all of the initialization has been done or to allow subclasses to
   * decide whether to use your default implementation.  The default
   * implementation of this method performs a restore of any view state that
   * had previously been frozen by {@link #onSaveInstanceState}.
   *
   * <p>This method is called between {@link #onStart} and
   * {@link #onPostCreate}.
   */
  @Override protected void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    if (savedInstanceState != null) {
      grandTotalTextView.setText(savedInstanceState.getString("KEY_GRAND_TOTAL"));}
      // Restore other views
  }
}
```

Since we're using MVVM, Google provides a library called Architecture Components which include base
ViewModels to extend from and a mechanism to persist across configuration changes.

First, we'll grab the following dependency and add it to our build.gradle

```groovy
dependencies {
  implementation 'androidx.lifecycle:lifecycle-extensions:2.0.0'
}
```
This provides ViewModel and AndroidViewModel. Both can be persisted across configuration changes.
AndroidViewModel provides access to an Application object.

Then we use ViewModelProviders to obtain our ViewModel.

```java
TipCalcViewModel vm = ViewModelProviders.of(this).get(TipCalcViewModel.class);
```

See [TipCalcActivity] for an example.

[TipCalcActivity]: src/main/java/com/orobator/helloandroid/lesson5/view/TipCalcActivity.java 