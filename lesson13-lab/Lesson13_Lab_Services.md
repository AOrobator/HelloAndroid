# Lesson 13 Lab: Services

For this lab, you'll create a UI with two buttons that will start and stop a service. Then log all 
the Service lifecycle methods and observe the statements in the logcat.

Start out with the following UI, a start service button, and a stop service button:

![service_lab_ui]

Next create a class called DemoService, and make sure that it logs the Service's lifecycle methods:

```java
public class DemoService extends Service {
  private static final String TAG = DemoService.class.getSimpleName();

  @Override public void onCreate() {
    super.onCreate();

    Log.d(TAG, "onCreate()");
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    Log.d(TAG, "onBind()");
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
```

Now that all the lifecycle methods are logged, create two actions that you can send to this service,
start and stop.

```java
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
```

Then in `onStartCommand()` log the received action, and if you receive `ACTION_STOP` make sure to 
call `stopSelf()`:

```java
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
```

Don't forget to add the service to your AndroidManifest.xml:

```xml
<service
  android:name=".DemoService"
  android:exported="false"/>
```

Back in MainActivity, add `onClickListener`s to your buttons to get the start and stop Intents 
respectively and call `startService()` with the appropriate Intents.

```java
Button startServiceButton = findViewById(R.id.startServiceButton);
startServiceButton.setOnClickListener(new View.OnClickListener() {
  @Override public void onClick(View v) {
    Intent intent = DemoService.getStartIntent(MainActivity.this);
    startService(intent);
  }
});

Button stopServiceButton = findViewById(R.id.stopServiceButton);
stopServiceButton.setOnClickListener(new View.OnClickListener() {
  @Override public void onClick(View v) {
    Intent intent = DemoService.getStopIntent(MainActivity.this);
    startService(intent);
  }
});
``` 

Now that everything is wired up, run the app and click the Start Service button several times. Note 
that `DemoService#onCreate` is only called the first time the button is clicked. Now click the 
Stop Service Button. Observe that `DemoService#onDestroy` is called.

[service_lab_ui]: service_lab_ui.png "Service Lab UI"