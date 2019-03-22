# Android Review Questions

<details><summary>What is the default build system for Android apps?</summary>
<p>

The default build system for Android is Gradle. It fetches dependencies for you and can build 
multi-module applications. (Much) Older build systems include Maven and Ant, while companies like 
Uber use Buck.
</p>
</details>

<details><summary>What is the AndroidManifest?</summary>
<p>

AndroidManifest.xml is a required file for all Android apps. All major components of your app must 
be listed here, including Activities, your custom Application class, Services, and 
BroadcastReceivers. For more, see [Anatomy of an App][app_anatomy_android_manifest]. 
</p>
</details>

<details><summary>What kind of things go in the res folder of your app?</summary>
<p>

Most things that are not explicitly code go into the res folder. This includes images in the form of 
drawables, XML layouts, strings, and menu resource files. For more, see 
[Anatomy of an App][app_anatomy_res_folder].
</p>
</details>

<details><summary>What is a resource qualifier?</summary>
<p>

A resource qualifier is of the format “-\<qualifier\>” and goes on the end of a folder in `res/`. It 
tells the Android system what resources to use for specific configurations. For more info see 
[Anatomy of an App][app_anatomy_res_qualifier].
</p>   
</details>

<details><summary>What is the keyboard shortcut for invoking an action?</summary>
<p>

This is arguably the most important shortcut you should know: <kbd>⌘ Command</kbd> + 
<kbd>⇧ Shift</kbd> + <kbd>A</kbd>. For more useful shortcuts, see 
[Handy Shortcuts][keyboard_shortcuts].
</p>
</details>

<details><summary>What is an Activity?</summary>
<p>

You can think of an Activity as one screen on Android. Activities must be declared in the 
AndroidManifest.xml as they are a system component. They are usually associated with one xml layout.
</p>
</details>

<details><summary>What is the Activity lifecycle? What are some methods in the lifecycle?</summary>
<p>

The Activity lifecycle is a series of methods called by the framework to let you know what stage of 
life your Activity is in. You should only do certain work when your app is in certain stages of the 
lifecycle. Some lifecycle methods include onCreate, onDestroy, onResume, and onPause. For more info,
see [Activity Lifecycle][activity_lifecycle]. 
</p>
</details>

<details><summary>How are logging statements made in Android? Where can you find the logs?</summary>
<p>

Android logging calls eventually end up in the Logcat. You can make logging statements through 
various methods, such as Log.d(TAG, message) and Log.wtf(TAG, message, exception). See 
[LifecycleActivity][logging]'s onCreate method for more info.
</p>
</details>

Describe the difference between a UI test and a Unit test. Which one should you have more of? Why?

Is it a good idea to hit your real web servers in tests?

Why would someone use Retrofit for networking vs just using OkHttp?

What is Android's main thread? How is it different from the UI thread? When shouldn't you use the 
main thread?

What is a configuration change? What happens to your Activity during one?

Describe the 3 parts of MVVM and what each part is used for.

What is dependency injection? What is it used for? What advantages does it provide?

How does Dagger help with dependency injection? Provide implementation details.

What is a memory leak? Name 2 ways to cause a memory leak and how to fix it.

You'd like to display many items in a list. How would you go about doing so? What classes do you 
need?

What are the two most important methods for creating a menu on Android?

What are some differences between a Fragment and an Activity?

[app_anatomy_res_folder]: lesson01/Lesson1_AnatomyOfAnApp.md#module_namesrcres
[app_anatomy_android_manifest]: lesson01/Lesson1_AnatomyOfAnApp.md#module_namesrcandroidmanifestxml
[app_anatomy_res_qualifier]: lesson01/Lesson1_AnatomyOfAnApp.md#module_namesrcresres_type-resource_qualifier
[keyboard_shortcuts]: lesson03/Lesson3_HandyShortcuts.md
[activity_lifecycle]: lesson02/Lesson2_ActivityLifecycle.md
[logging]: lesson02/src/main/java/com/orobator/helloandroid/lesson2/LifecycleActivity.java
