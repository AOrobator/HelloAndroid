# Lesson 8: Memory Leaks

## What is a memory leak?

Android is a managed memory environment. This means that as a developer you don’t need to take care 
of allocating or de-allocating memory by yourself. The Garbage Collector takes care of freeing up 
objects that are no longer needed. But when the GC can’t free up objects which have strong 
references, it results in precious memory not getting freed. These non freed objects are called 
leaks. Initially, this might not be a problem. But imagine if you don’t take the trash out for 2 
weeks! The house starts to smell right?

Similarly, as the user keeps on using our app, the memory also keeps on increasing and if the memory
leaks are not plugged, then the unused memory cannot be freed up by the Garbage Collection. So the 
memory usage of our app will constantly increase until no more memory can be allocated to our app, 
leading to an OutOfMemoryError which ultimately crashes the app.

## Finding memory leaks

LeakCanary is a great tool for finding memory leaks. It sends notifications to you when leaks occur
and provides the stack trace of the leak.

![Leak Canary][leak_canary]

You can add LeakCanary to your project like so

```groovy
dependencies {
  debugImplementation 'com.squareup.leakcanary:leakcanary-android:1.6.3'
  releaseImplementation 'com.squareup.leakcanary:leakcanary-android-no-op:1.6.3'
  // Optional, if you use support library fragments:
  debugImplementation 'com.squareup.leakcanary:leakcanary-support-fragment:1.6.3'
}
```

Then in your application class:

```java
public class ExampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    // Normal app init code...
  }
}
```

## Sources of Memory Leaks

### Inner Classes

Inner classes that outlive an Activity can be a major source of memory leaks. For example, AsyncTask
is a threading mechanism used to do work on a background thread. When an AsyncTask is an inner class
of an Activity, it holds an implicit reference to the Activity. Rotating the device or leaving the 
Activity causes the Activity to be destroyed, but the AsyncTask is still running and holding on to 
the Activity. I usually advise staying away from AsyncTasks for this and other reasons. However, if
we really wanted to use AsyncTask, we should cancel the AsyncTask when the Activity is destroyed. 
Just know that calling cancel doesn't kill the thread the AsyncTask is running on. AsyncTasks also 
run serially, not in parallel unless a custom executor is specified, so true concurrency with 
multiple AsyncTasks is difficult to achieve.

See [LeakyAsyncTaskActivity.java] for an example.

### Static References

Another way to leak memory is with a static reference to an Activity, demo'd in 
[LeakyInnerClassActivity.java]. In general, there is hardly ever a good reason to use inner classes at all.
Activities should also not be kept in static references as static variables have a longer lifecycle
than Activities.

### Singleton Reference

Consider the below example — you have defined a Singleton class as displayed below and you need to 
pass the context in order to fetch some files from the local storage.

```java
public class SingletonLeakExampleActivity extends AppCompatActivity {

    private SingletonSampleClass singletonSampleClass;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* 
         * Option 1: Do not pass activity context to the Singleton class. 
         * Instead pass application Context
         */      
        singletonSampleClass = SingletonSampleClass.getInstance(this);
    }
  
    
   @Override
   protected void onDestroy() {
        super.onDestroy();

    /* 
     * Option 2: Unregister the singleton class here i.e. if you pass 
     * activity context to the Singleton class, then ensure that when 
     * the activity is destroyed, the context in the singleton class is 
     * set to null.
     */
     singletonSampleClass.onDestroy();
   }
}
```

and here is the Singleton implementation:

```java
public class SingletonSampleClass {
  
    private Context context;
    private static SingletonSampleClass instance;
  
    private SingletonSampleClass(Context context) {
        this.context = context;
    }

    public synchronized static SingletonSampleClass getInstance(Context context) {
        if (instance == null) instance = new SingletonSampleClass(context);
        return instance;
    }
  
    public void onDestroy() {
       if (context != null) {
          context = null; 
       }
    }
}
```

### Handler Reference

A Handler allows you to send and process Message and Runnable objects associated with a thread's 
MessageQueue. Each Handler instance is associated with a single thread and that thread's message 
queue. When you create a new Handler, it is bound to the thread / message queue of the thread that 
is creating it -- from that point on, it will deliver messages and Runnables to that message queue 
and execute them as they come out of the message queue.

There are two main uses for a Handler: 
1. To schedule messages and Runnables to be executed at some point in the future.
2. To enqueue an action to be performed on a different thread than your own.

Now consider the following example — You are using a Handler to redirect to a new screen after 5 
seconds.

```java
public class HandlersReferenceLeakActivity extends AppCompatActivity {

    private TextView textView;

    /*
     * Mistake Number 1
     */
     private Handler leakyHandler = new Handler();
     
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /*
         * Mistake Number 2
         */
        leakyHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(getString(R.string.text_handler_1));
            }
        }, 5000);
    }
}
```

We can fix this in 2 ways:

1. NEVER reference a class inside the activity. If we definitely need to, we should set the class as 
   static. This is because when a Handler is instantiated on the main thread, it is associated with 
   the Looper’s message queue. Messages posted to the message queue will hold a reference to the 
   Handler so that the framework can call Handler#handleMessage(Message) when the Looper eventually 
   processes the message.
   
2. Use a WeakReference of the Activity.

Here's the fix:

```java
public class HandlersReferenceLeakActivity extends AppCompatActivity {

    private TextView textView;

    /*
     * Fix number 1
     */
    private final LeakyHandler leakyHandler = new LeakyHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        leakyHandler.postDelayed(leakyRunnable, 5000);
    }

    /*
     * Fix number 2 - define as static
     */
    private static class LeakyHandler extends Handler {
      
        /*
         * Fix number 3 - Use WeakReferences
         */      
        private WeakReference<HandlersReferenceLeakActivity> weakReference;
        public LeakyHandler(HandlersReferenceLeakActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlersReferenceLeakActivity activity = weakReference.get();
            if (activity != null) {
                activity.textView.setText(activity.getString(R.string.text_handler_2));
            }
        }
    }

    private static final Runnable leakyRunnable = new Runnable() {
        @Override
        public void run() { /* ... */ }
    };
}
```


In summary:

1. Use applicationContext() instead of activity context when possible. If you really have to use an 
   activity context, then when the activity is destroyed, ensure that the context you passed to the 
   class is set to null.
   
2. Never use static variables to declare views or activity context.

3. Always use a weakReference of the activity or view when needed.

4. Never reference a class inside the Activity. If we need to, we should declare it as static, 
   whether it is a thread or a handler or a timer or an AsyncTask.


[leak_canary]: leak-canary.png "Leak Canary"
[LeakyAsyncTaskActivity.java]: src/main/java/com/orobator/helloandroid/lesson8/LeakyAsyncTaskActivity.java
[LeakyInnerClassActivity.java]: src/main/java/com/orobator/helloandroid/lesson8/LeakyInnerClassActivity.java
