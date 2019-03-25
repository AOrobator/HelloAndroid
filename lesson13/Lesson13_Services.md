# Lesson 13: Services

A Service is an application component that can perform long-running operations in the background, 
and it doesn't provide a user interface. Another application component (Activity, Application, 
Service, etc.) can start a service, and it continues to run in the background even if the user 
switches to another application. Additionally, a component can bind to a service to interact with it 
and even perform interprocess communication (IPC). For example, a service can handle network 
transactions, play music, perform file I/O, or interact with a content provider, all from the 
background.

## Threads and Services

Keep in mind that even though a Service doesn't have a UI, **all code written in a Service runs on 
the main thread by default!** If you're doing long running operations, you'll still need a background 
thread.

This raises the question: When do you use a Service vs. just using a background thread? You can use 
just a background thread if it makes sense to stop your work when the user leaves the 
Activity/your Application. However, if the work should be continued even if the user leaves your 
app, you should use a Service.

## Types of Services

There are three different types of Services:

### Foreground Services

A foreground service performs some operation that is noticeable to the user. For example, an audio 
app would use a foreground service to play an audio track. Foreground services must display a 
Notification. Foreground services continue running even when the user isn't interacting with the 
app.

### Background Services

A background service performs an operation that isn't directly noticed by the user. For example, if 
an app used a service to compact its storage, that would usually be a background service. If your 
app targets API level 26 (Android 8 Oreo) or higher, the system imposes restrictions on running 
background services when the app itself isn't in the foreground. Android Oreo was a very privacy 
focused update, so if Android "catches" you running something in the background, it will display a 
notification to the user that your app is working in the background. In most cases like this, your 
app should use a scheduled job with WorkManager instead.

### Bound Services

A service is bound when an application component binds to it by calling `bindService()`. A bound 
service offers a client-server interface that allows components to interact with the service, send 
requests, receive results, and even do so across processes with interprocess communication (IPC). A 
bound service runs only as long as another application component is bound to it. Multiple components
can bind to the service at once, but when all of them unbind, the service is destroyed.

## Service Demo

To get started with Services, we'll create a very simple music player that plays a song in a 
Service. Music is provided by https://www.bensound.com.

First we'll set up our UI that will manage playback of the music.

![music_player_ui]

For our UI, we'll just have three buttons that can control the playback: play, pause, and stop. 
For a real music app, you'd want to have something a little beefier, but this UI will do just fine 
for our purposes.

### Creating a Service

Next we'll want to create the Service that plays the music. In order to create this Service, we'll 
have to create the appropriate class, as well as register our Service in AndroidManifest.xml. Create
a class called `MusicPlayerService` and have it extend `android.app.Service`. Android Studio will 
then prompt you to implement the `onBind(Intent)` method. This method is used exclusively for bound 
services. It returns an `IBinder` which represents the communication channel to this Service. Since 
`MusicPlayerService` will not be bound, we can simply return null here. 

```java
public class MusicPlayerService extends Service {

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }
}
```

After we create our Service class, we'll have to register it in the AndroidManifest because this is 
an Android app component, just like an Activity. The simplest way of doing so is as follows:

```xml
<application>
  <service android:name=".MusicPlayerService"/>
</application>
```   

The `android:name` attribute is the only required attribute—it specifies the class name of the 
service. After you publish your application, leave this name unchanged to avoid the risk of breaking 
code due to dependence on explicit intents to start or bind the service (read the blog post, 
[Things That Cannot Change]).

There are other attributes that you can define for a Service in your manifest such as 
`android:exported` and `android:permission`. `android:exported` is a boolean attribute that 
specifies whether or not components from other applications can start this Service. For security 
reasons, you'll want to set this to false if you don't plan on your other apps using this Service. 
If you do want your other apps to be able to start this Service, but not any app, you would set 
`android:exported="true"` and set a value for `android:permission`, so that only your apps with a 
particular permission can start your Service. For a full list of Service attributes, see 
[here][service_attributes].

### Service Lifecycle

Just like an Activity, a Service also has a lifecycle.

![service_lifecycle]

The first method in a Service's lifecycle is `onCreate()`. The system invokes this method to perform 
one-time setup procedures when the service is initially created (before it calls either 
`onStartCommand()` or `onBind()`). If the service is already running, this method is not called.

For `MusicPlayerService`, the next method in its lifecycle is `onStartCommand()`. The system invokes
this method by calling `startService()` when another component (such as an activity) requests that 
the service be started. When this method executes, the service is started and can run in the 
background indefinitely. If you implement this, it is your responsibility to stop the service when 
its work is complete by calling `stopSelf()` or `stopService()`. If you only want to provide 
binding, you don't need to implement this method.

Next, we have the previously mentioned `onBind()` method, which is used for bound services.

And finally, the last lifecycle method for Services is `onDestroy()`. The system invokes this method
when the service is no longer used and is being destroyed. Your service should implement this to 
clean up any resources such as threads, registered listeners, or receivers. This is the last call 
that the service receives.

### Sending Service Commands

In order to send commands to our Service, we'll have to call `startService(Intent)`. The `Intent`s 
that we pass to `startService(...)` will have to have specific actions on them that we'll check in
`MusicPlayerService#onStartCommand(...)`. According to the Single Responsibility Principle, our objects 
should be well encapsulated, so we'll have static methods in `MusicPlayerService` that can generate 
the appropriate Intents.

```java
public class MusicPlayerService extends Service {
  private static final String TAG = "MusicPlayerService";
  private static final String ACTION_PLAY = "play";
  private static final String ACTION_PAUSE = "pause";
  private static final String ACTION_STOP = "stop";
  
  public static Intent getPlayIntent(Context context) {
    Intent intent = new Intent(context, MusicPlayerService.class);
    intent.setAction(ACTION_PLAY);
    return intent;
  }

  public static Intent getPauseIntent(Context context) {
    Intent intent = new Intent(context, MusicPlayerService.class);
    intent.setAction(ACTION_PAUSE);
    return intent;
  }

  public static Intent getStopIntent(Context context) {
    Intent intent = new Intent(context, MusicPlayerService.class);
    intent.setAction(ACTION_STOP);
    return intent;
  }
}
```

Once we can generate the right `Intent`s to send to our service, we'll hook up the OnClickListeners 
in our Activity to send these commands to the service.

```java
Button playButton = findViewById(R.id.play_button);
playButton.setOnClickListener((v) -> {
    Intent intent = MusicPlayerService.getPlayIntent(MainActivity.this);
    startService(intent);
  });
```

When `startService()` gets called for the first time, `MusicPlayerService#onCreate` gets called 
before `MusicPlayerService#onStartCommand`. Subsequent calls to `startService` after the service has 
been started will skip the call to `onCreate()` and go straight to `onStartCommand()`. In 
`onStartCommand()`, we first get the `Intent` used to start this service, then get its action to 
determine what command to perform.

```java
@Override public int onStartCommand(Intent intent, int flags, int startId) {
  String action = intent.getAction();

  Log.d(TAG, "onStartCommand(" + action + ")");

  switch (action) {
    case ACTION_PLAY:
      playMusic();
      break;
    case ACTION_PAUSE:
      pauseMusic();
      break;
    case ACTION_STOP:
      stopMusic();
      stopSelf();
      break;
    default:
      throw new IllegalArgumentException("Received unknown action: " + action);
  }

  return START_NOT_STICKY;
}
``` 

Note the `break;` statements after every case. In Java, if we don't use a break statement, our code 
execution will "fall through" to the next case. 

`onStartCommand` is supposed to return an int, so here we return `START_NOT_STICKY` to indicate that
we don't want our service automatically recreated if Android kills our process. Other constants that 
can be returned here include `START_STICKY` and `START_REDELIVER_INTENT` which give different 
behaviors after process death.

Notice that when we process the `ACTION_STOP` command, after stopping the music, we call 
`MusicPlayerService#stopSelf()`. It's important that we call `stopSelf()`, otherwise, our service 
would run indefinitely. This call stops the service and the `onDestroy()` method of 
`MusicPlayerService` will be called.

## [Click here for Services lab][services_lab]

## Android Oreo (API 26) Background Limitations

Many Android apps and services can be running simultaneously. For example, a user could be playing a 
game in one window while browsing the web in another window, and using a third app to play music. 
The more apps are running at once, the more load is placed on the system. If additional apps or 
services are running in the background, this places additional loads on the system, which could 
result in a poor user experience; for example, the music app might be suddenly shut down.

To lower the chance of these problems, Android 8.0 places limitations on what apps can do while 
users aren't directly interacting with them.

The system distinguishes between foreground and background apps. An app is considered to be in the 
foreground if any of the following is true:

 * It has a visible activity, whether the activity is started or paused.
 * It has a foreground service.
 * Another foreground app is connected to the app, either by binding to one of its services or by 
   making use of one of its content providers. For example, the app is in the foreground if another 
   app binds to its:
   * IME
   * Wallpaper service
   * Notification listener
   * Voice or text service
   
If none of those conditions is true, the app is considered to be in the background.

While an app is in the foreground, it can create and run both foreground and background services 
freely. When an app goes into the background, it has a window of several minutes in which it is 
still allowed to create and use services. At the end of that window, the app is considered to be 
idle. At this time, the system stops the app's background services, just as if the app had called 
the services' Service.stopSelf() methods.

Under certain circumstances, a background app is placed on a temporary whitelist for several 
minutes. While an app is on the whitelist, it can launch services without limitation, and its 
background services are permitted to run. An app is placed on the whitelist when it handles a task 
that's visible to the user, such as:

 * Handling a high-priority Firebase Cloud Messaging (FCM) message.
 * Receiving a broadcast, such as an SMS/MMS message.
 * Executing a PendingIntent from a notification.
 * Starting a VpnService before the VPN app promotes itself to the foreground.

In many cases, your app can replace background services with WorkManager jobs. For example, 
CoolPhotoApp needs to check whether the user has received shared photos from friends, even if the 
app isn't running in the foreground. Previously, the app used a background service which checked 
with the app's cloud storage. To migrate to Android 8.0 (API level 26), the developer replaces the 
background service with a scheduled job, which is launched periodically, queries the server, then 
quits.

Prior to Android 8.0, the usual way to create a foreground service was to create a background 
service, then promote that service to the foreground. With Android 8.0, there is a complication; 
the system doesn't allow a background app to create a background service. 

For this reason, Android 8.0 introduces the new method `startForegroundService()` to start a new 
service in the foreground. After the system has created the service, the app has five seconds to 
call the service's `startForeground()` method to show the new service's user-visible notification. 
If the app does not call `startForeground()` within the time limit, the system stops the service and
declares the app to be ANR (App Not Responding).

## Creating a foreground service

In order to create a foreground service, you'll need to add the following permission to your 
AndroidManifest: 

```xml
<manifest>

  <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

</manifest>
```

Then call `startForeground` and provide a notification.

```java
case ACTION_PLAY:
  playMusic();
  Notification notification =
      NotificationFactory.getPlaybackNotification(
          this,
          getPlayPendingIntent(this),
          getPausePendingIntent(this),
          getStopPendingIntent(this));
  notificationManager.notify(id, notification);
  startForeground(id, notification);
  break;
```
To stop a Service from being in the foreground, call stopForeground, and specify whether to remove 
the notification.

```java
case ACTION_STOP:
  stopMusic();
  stopForeground(true);
  stopSelf();
  break;
``` 

[music_player_ui]: music_player_ui.png "Simple Music Player UI"
[Things That Cannot Change]: https://android-developers.googleblog.com/2011/06/things-that-cannot-change.html
[service_attributes]: https://developer.android.com/guide/topics/manifest/service-element.html
[service_lifecycle]: service_lifecycle.png "Service Lifecycle"
[services_lab]: ../lesson13-lab/Lesson13_Lab_Services.md