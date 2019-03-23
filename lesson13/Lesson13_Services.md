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

[music_player_ui]: music_player_ui.png "Simple Music Player UI"
