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

[music_player_ui]: music_player_ui.png "Simple Music Player UI"
[Things That Cannot Change]: https://android-developers.googleblog.com/2011/06/things-that-cannot-change.html
[service_attributes]: https://developer.android.com/guide/topics/manifest/service-element.html
[service_lifecycle]: service_lifecycle.png "Service Lifecycle"