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

## Make your first WorkRequest
In this step you will take an image in the res/drawable folder called test.jpg and run a few 
functions on it in the background. These functions will blur the image and save it to a temporary 
file.

### WorkManager Basics
There are a few WorkManager classes you need to know about:

 * [Worker]: This is where you put the code for the actual work you want to perform in the background.
   You'll extend this class and override the `doWork()` method.
 * [WorkRequest]: This represents a request to do some work. You'll pass in your `Worker` as part of 
   creating your `WorkRequest`. When making the `WorkRequest` you can also specify things like 
   [Constraints] on when the `Worker` should run.
 * [WorkManager]: This class actually schedules your `WorkRequest` and makes it run. It schedules 
   `WorkRequest`s in a way that spreads out the load on system resources, while honoring the 
   constraints you specify.
   
In your case, you'll define a new `BlurWorker` which will contain the code to blur an image. When the 
Go button is clicked, a `WorkRequest` is created and then enqueued by `WorkManager`.

### Step 1 - Make BlurWorker
In the package workers, create a new class called BlurWorker.

It should extend Worker.

Step 2 - Add a constructor
Add a constructor to the BlurWorker class:

public BlurWorker(
        @NonNull Context appContext,
        @NonNull WorkerParameters workerParams) {
    super(appContext, workerParams);
}
Step 3 - Override and implement doWork()
Your Worker will blur the res/test.jpg image.

Override the doWork() method and then implement the following:

Get a Context by calling getApplicationContext(). You'll need this for various bitmap manipulations you're about to do.
Create a Bitmap from the test image:
Bitmap picture = BitmapFactory.decodeResource(
    applicationContext.getResources(),
    R.drawable.test);
Get a blurred version of the bitmap by calling the static blurBitmap method from WorkerUtils.
Write that bitmap to a temporary file by calling the static writeBitmapToFile method from WorkerUtils. Make sure to save the returned URI to a local variable.
Make a Notification displaying the URI by calling the static makeStatusNotification method from WorkerUtils.
Return Result.success();
Wrap the code from steps 2-6 in a try/catch statement. Catch a generic Throwable.
In the catch statement, emit an error Log statement: Log.e(TAG, "Error applying blur", throwable);
In the catch statement then return Result.failure();
The completed code for this step is below.

BlurWorker.java
public class BlurWorker extends Worker {
    public BlurWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    private static final String TAG = BlurWorker.class.getSimpleName();

    @NonNull
    @Override
    public Worker.Result doWork() {

        Context applicationContext = getApplicationContext();

        try {

            Bitmap picture = BitmapFactory.decodeResource(
                    applicationContext.getResources(),
                    R.drawable.test);
        
            // Blur the bitmap
            Bitmap output = WorkerUtils.blurBitmap(picture, applicationContext);

            // Write bitmap to a temp file
            Uri outputUri = WorkerUtils.writeBitmapToFile(applicationContext, output);

            WorkerUtils.makeStatusNotification("Output is "
                  + outputUri.toString(), applicationContext);

            // If there were no errors, return SUCCESS
            return Result.success();
        } catch (Throwable throwable) {

            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error applying blur", throwable);
            return Result.failure();
        }
    }
}
Step 4 - Get WorkManager in the ViewModel
Create a variable for a WorkManager instance in your ViewModel and instantiate it in the ViewModel's constructor:

BlurViewModel.java
private WorkManager mWorkManager;

// BlurViewModel constructor
public BlurViewModel() {
  mWorkManager = WorkManager.getInstance();
  //...rest of the constructor
}
Step 5 - Enqueue the WorkRequest in WorkManager
Alright, time to make a WorkRequest and tell WorkManager to run it. There are two types of WorkRequests:

OneTimeWorkRequest: A WorkRequest that will only execute once.
PeriodicWorkRequest: A WorkRequest that will repeat on a cycle.
We only want the image to be blurred once when the Go button is clicked. The applyBlur method is called when the Go button is clicked, so create a OneTimeWorkRequest from BlurWorker there. Then, using your WorkManager instance enqueue your WorkRequest.

Add the following line of code into BlurViewModel's applyBlur() method:

BlurViewModel.java
void applyBlur(int blurLevel) {
   mWorkManager.enqueue(OneTimeWorkRequest.from(BlurWorker.class));
}
Step 6 - Run your code!
Run your code. It should compile and you should see the Notification when you press the Go button.




Optionally you can open the Device File Explorer in Android Studio:



Then navigate to data>data>com.example.background>files>blur_filter_outputs><URI> and confirm that the fish was in fact blurred:

[codelab]: https://codelabs.developers.google.com/codelabs/android-workmanager/#0
[blur-o-matic_1]: blur-o-matic_1.png "Background Work with WorkManager" 
[blur-o-matic_2]: blur-o-matic_2.png "Background Work with WorkManager"
[Worker]: https://developer.android.com/reference/androidx/work/Worker.html
[WorkRequest]: https://developer.android.com/reference/androidx/work/WorkRequest.html
[WorkManager]: https://developer.android.com/reference/androidx/work/WorkManager.html
[Constraints]: https://developer.android.com/reference/androidx/work/Constraints.html