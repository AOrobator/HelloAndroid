# Broadcast Receivers

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
- JobScheduler can handle some of the use cases that were previously only available through 
  Broadcast Receivers. For example: Stop polling web service while on metered internet.

## Register to Receive Broadcast Intents

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
Hide app — press the Home button. Turn airplane mode on and off. Notice the logcat still updates.

[broadcast_receiver]: broadcastReceiver.png "BroadcastReceiver"

