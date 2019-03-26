# Lesson 14: Broadcast Receivers

![BroadcastReceiver][broadcast_receiver]

- Android can broadcast messages system wide to indicate when system states change
- These messages are sent using the `Intent` structure
- Your app can request to receive events when these states change
  - Examples: Airplane Mode off/on, Battery "low" or "ok", Bootup Complete, etc.
  - See https://developer.android.com/reference/android/content/Intent.html#constants_2 for a list
  - See <https://developer.android.com/guide/components/broadcasts>
- Your app can also broadcast messages to itself. This is handy for Services or JobScheduler 
  Handler. These are Explicit Broadcasts
- System defined messages that are not sent to a specific app are Implicit Messages.
- API 26 and higher you cannot register for Implicit Messages using the AndroidManifest.xml, it has 
  to be done using the API
  - Many apps would register for an implicit broadcast like Connectivity Change.
  - When this broadcast was sent out, it would wake many apps on the user's phone.
  - In low memory situations, this can lead to RAM-thrashing as apps are switched in/out of memory.
  - Sometimes apps would be woken up and decide not to do anything with the broadcast. Wasteful!
  - Bad for battery as CPU & Radios can be woken up sporadically.
- JobScheduler/WorkManager can handle some of the use cases that were previously only available 
  through Broadcast Receivers. For example: Stop polling web service while on metered internet.

## Dynamically Register to Receive Broadcast Intents

1. Create a class that extends `BroadcastReceiver` and overrides `onReceive` 

   ```java
   public class AirplaneReceiver extends BroadcastReceiver {
       private static final String TAG = "AirplaneReceiver";
       @Override
       public void onReceive(Context context, Intent intent) {
           if (intent.hasExtra("state")) {
               boolean state = intent.getBooleanExtra("state", false);
               Log.i(TAG, "Airplane Mode: " + state);
           }
           Log.i(TAG, "Airplane Mode");
       }
   }
   ```

2. Create an object of your subclass

3. Create a filter for the type of action that is being requested

4. Register your Broadcast Receiver 

   ```java
   AirplaneReceiver receiver = new AirplaneReceiver();
   IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
   filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
   registerReceiver(receiver, filter);
   ```

Monitor the Logcat. Launch and turn airplane mode on and off. Notice the log indicates changes. 
Now comment out the call to unregister and hide the app by pressing the home button. Turn airplane 
mode on and off. Notice the logcat still updates.

## Unregister your receivers!

Always make sure to unregister your BroadcastReceivers. Failure to do so will cause a 
BroadcastReceiver leak.

```java
@Override protected void onStop() {
  super.onStop();

  unregisterReceiver(receiver);
}
```

Even if you don't unregister, your BroadcastReceiver won't receive broadcasts indefinitely.

Context-registered receivers receive broadcasts as long as their registering context is valid. 

For an example, if you register within an Activity context, you receive broadcasts as long as the 
activity is not destroyed. 

If you register with the Application context, you receive broadcasts as long as the app is running.

## Statically Register to Receive Broadcast Intents

It's also possible to register BroadcastReceivers in your AndroidManifest. If you declare a 
BroadcastReceiver in your manifest, the system launches your app (if the app is not already running) 
when the broadcast is sent.

If your app targets API level 26 or higher, you cannot use the manifest to declare a receiver for 
implicit broadcasts (broadcasts that do not target your app specifically), except for a few implicit
broadcasts that are exempted from that restriction. See the list [here][broadcast_exceptions].

Here's an example BroadcastReceiver declared in the manifest that listens for both the 
`BOOT_COMPLETED` event as well as the `INPUT_METHOD_CHANGED` event.

```xml
<application>
  <receiver android:name=".MyBroadcastReceiver" android:exported="true">
    <intent-filter>
      <action android:name="android.intent.action.BOOT_COMPLETED"/>
      <action android:name="android.intent.action.INPUT_METHOD_CHANGED"/>
    </intent-filter>
  </receiver>
</application>
```

The system package manager registers the receiver when the app is installed. The receiver then 
becomes a separate entry point into your app which means that the system can start the app and 
deliver the broadcast if the app is not currently running.

The system creates a new BroadcastReceiver component object to handle each broadcast that it 
receives. This object is valid only for the duration of the call to onReceive(Context, Intent). Once
your code returns from this method, the system considers the component no longer active.

## BroadcastReceivers and Background Threads

`onReceive` executes on the Main Thread, so don't take too long in this method.

It's also not advised to start background threads in a BroadcastReceiver's `onReceive` method. If 
your app was only brought to the foreground because of this broadcast, Android will kill your 
process soon after `onReceive` completes. For this reason, it's better to schedule a job with 
WorkManager.

---
Note: Do not start activities from broadcast receivers because the user experience is jarring; especially 
if there is more than one receiver. Instead, consider displaying a notification.


[broadcast_receiver]: broadcastReceiver.png "BroadcastReceiver"
[broadcast_exceptions]: https://developer.android.com/guide/components/broadcast-exceptions.html

