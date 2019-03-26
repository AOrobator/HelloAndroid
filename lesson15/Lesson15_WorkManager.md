# Lesson 15: WorkManager

So far, we've talked about background tasks with Services and BroadcastReceivers. These are both
good tools to have up your sleeve as an Android developer, but they both have their limitations. For
BroadcastReceivers, they can only be registered and called while your app is running on API 26+. For
Services, you might not want to implement all the logic needed to run certain code when certain 
conditions are met. 

## WorkManager to the rescue!

The WorkManager API makes it easy to schedule deferrable, asynchronous tasks that are expected to 
run even if the app exits or device restarts.

Key features:

 * Backwards compatible up to API 14
 * Uses JobScheduler on devices with API 23+
 * Uses a combination of BroadcastReceiver + AlarmManager on devices with API 14-22
 * Add work constraints like network availability or charging status
 * Schedule asynchronous one-off or periodic tasks
 * Monitor and manage scheduled tasks
 * Chain tasks together
 * Guarantees task execution, even if the app or device restarts

WorkManager is intended for tasks that are deferrable—that is, not required to run immediately—and 
required to run reliably even if the app exits or the device restarts. For example:

 * Sending logs or analytics to backend services
 * Periodically syncing application data with a server
 
WorkManager is not intended for in-process background work that can safely be terminated if the app 
process goes away or for tasks that require immediate execution.

To learn more about WorkManager, we'll work through Google's provided 
[WorkManager code lab][codelab] together.

## What you will build

These days, smartphones are almost too good at taking pictures. Gone are the days a photographer 
could take a reliably blurry picture of something mysterious.

In this codelab you'll be working on Blur-O-Matic, an app that blurs photos and images and saves the
result to a file. Was that the Loch Ness monster or evelopera toy submarine? With Blur-O-Matic, 
no-one will ever know.

![blur-o-matic_1] ![blur-o-matic_2]

[codelab]: https://codelabs.developers.google.com/codelabs/android-workmanager/#0
[blur-o-matic_1]: blur-o-matic_1.png "Background Work with WorkManager" 
[blur-o-matic_2]: blur-o-matic_2.png "Background Work with WorkManager" 