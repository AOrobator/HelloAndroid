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

The AndroidManifest is a required file for all Android apps. All major components of your app must 
be listed here, including Activities, your custom Application class, Services, and 
BroadcastReceivers.
</p>
</details>

What kind of things go in the res folder of your app?

What is a resource qualifier?

What is the keyboard shortcut for invoking an action?

What is an Activity?

What is the Activity lifecycle? What are some methods in the lifecycle?

Describe the difference between a UI test and a Unit test. Which one should you have more of?

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
