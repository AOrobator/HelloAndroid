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
In the package workers, create a new class called `BlurWorker`.

It should extend Worker.

### Step 2 - Add a constructor
Add a constructor to the BlurWorker class:

```java
public BlurWorker(
        @NonNull Context appContext,
        @NonNull WorkerParameters workerParams) {
    super(appContext, workerParams);
}
```

### Step 3 - Override and implement doWork()
Your `Worker` will blur the `res/test.jpg` image.

Override the `doWork()` method and then implement the following:

1. Get a `Context` by calling `getApplicationContext()`. You'll need this for various bitmap 
   manipulations you're about to do.
2. Create a Bitmap from the test image:

```java
Bitmap picture = BitmapFactory.decodeResource(
    applicationContext.getResources(),
    R.drawable.test);
```
3. Get a blurred version of the bitmap by calling the static `blurBitmap` method from `WorkerUtils`.

4. Write that bitmap to a temporary file by calling the static `writeBitmapToFile` method from 
   `WorkerUtils`. Make sure to save the returned URI to a local variable.
   
5. Make a Notification displaying the URI by calling the static `makeStatusNotification` method from
   `WorkerUtils`.
   
6. Return `Result.success();`

7. Wrap the code from steps 2-6 in a try/catch statement. Catch a generic `Throwable`.

8. In the catch statement, emit an error Log statement: `Log.e(TAG, "Error applying blur", throwable);`

9. In the catch statement then return `Result.failure();`

The completed code for this step is below.

[BlurWorker.java]
```java
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
```

### Step 4 - Get WorkManager in the ViewModel
Create a variable for a `WorkManager` instance in your `ViewModel` and instantiate it in the 
`ViewModel`'s constructor:

[BlurViewModel.java]
```java
private WorkManager mWorkManager;

// BlurViewModel constructor
public BlurViewModel() {
  mWorkManager = WorkManager.getInstance();
  //...rest of the constructor
}
```

### Step 5 - Enqueue the WorkRequest in WorkManager
Alright, time to make a `WorkRequest` and tell `WorkManager` to run it. There are two types of 
`WorkRequests`:

 * `OneTimeWorkRequest`: A `WorkRequest` that will only execute once.
 * `PeriodicWorkRequest`: A `WorkRequest` that will repeat on a cycle.

We only want the image to be blurred once when the **Go** button is clicked. The `applyBlur` method 
is called when the **Go** button is clicked, so create a `OneTimeWorkRequest` from `BlurWorker` 
there. Then, using your `WorkManager` instance enqueue your `WorkRequest`.

Add the following line of code into BlurViewModel's applyBlur() method:

[BlurViewModel.java]
```java
void applyBlur(int blurLevel) {
   mWorkManager.enqueue(OneTimeWorkRequest.from(BlurWorker.class));
}
```

### Step 6 - Run your code!
Run your code. It should compile and you should see the Notification when you press the Go button.

![first_request_step6]

Optionally you can open the **Device File Explorer** in Android Studio. Using Command+Shift+A will 
be fastest, but if your prefer your mouse:

![device_file_explorer1]

Then navigate to **data>data>com.example.background>files>blur_filter_outputs><URI>** and confirm 
that the fish was in fact blurred:

![device_file_explorer2]

## Add Input and Output
Blurring that test image is all well and good, but for Blur-O-Matic to really be the revolutionary 
image editing app it's destined to be, you'll need to let users blur their own images.

To do this, we'll provide the URI of the user's selected image as input to our `WorkRequest`.

### Step 1 - Create Data input object
Input and output is passed in and out via [Data] objects. `Data` objects are lightweight containers 
for key/value pairs. They are meant to store a small amount of data that might pass into and out from 
`WorkRequest`s.

You're going to pass in the URI for the user's image into a bundle. That URI is stored in a variable 
called `mImageUri`.

Create a private method called `createInputDataForUri`. This method should:

1. Create a `Data.Builder` object.

2. If `mImageUri` is a non-null URI, then add it to the `Data` object using the `putString` method. 
   This method takes a key and a value. You can use the String constant `KEY_IMAGE_URI` from the 
   `Constants` class.
   
3. Call `build()` on the `Data.Builder` object to make your `Data` object, and return it.

Below is the completed `createInputDataForUri` method:

[BlurViewModel.java]
```java
/**
 * Creates the input data bundle which includes the Uri to operate on
 * @return Data which contains the Image Uri as a String
 */
private Data createInputDataForUri() {
    Data.Builder builder = new Data.Builder();
    if (mImageUri != null) {
        builder.putString(KEY_IMAGE_URI, mImageUri.toString());
    }
    return builder.build();
}
```
### Step 2 - Pass the Data object to WorkRequest
You're going to want to change the `applyBlur` method so that it:

1. Creates a new `OneTimeWorkRequest.Builder`.

2. Calls `setInputData`, passing in the result from `createInputDataForUri`.

3. Builds the `OneTimeWorkRequest`.

4. Enqueues that request using `WorkManager`.

Below is the completed `applyBlur` method:

[BlurViewModel.java]
```java
void applyBlur(int blurLevel) {
   OneTimeWorkRequest blurRequest =
                new OneTimeWorkRequest.Builder(BlurWorker.class)
                        .setInputData(createInputDataForUri())
                        .build();

   mWorkManager.enqueue(blurRequest);
}
```

### Step 3 - Update BlurWorker's doWork() to get the input
Now let's update `BlurWorker`'s `doWork()` method to get the URI we passed in from the `Data` 
object:

[BlurWorker.java]
```java
public Worker.Result doWork() {
     
  Context applicationContext = getApplicationContext();
   
   // ADD THIS LINE
  String resourceUri = getInputData().getString(Constants.KEY_IMAGE_URI);
    
   //... rest of doWork()
}
```

### Step 4 - Blur the given URI
With the URI, you can blur the image the user selected:

[BlurWorker.java]
```java
public Worker.Result doWork() {
       Context applicationContext = getApplicationContext();
        
       String resourceUri = getInputData().getString(Constants.KEY_IMAGE_URI);

    try {

        // REPLACE THIS CODE:
        // Bitmap picture = BitmapFactory.decodeResource(
        //        applicationContext.getResources(),
        //        R.drawable.test);
        // WITH
        if (TextUtils.isEmpty(resourceUri)) {
            Log.e(TAG, "Invalid input uri");
            throw new IllegalArgumentException("Invalid input uri");
        }

        ContentResolver resolver = applicationContext.getContentResolver();
        // Create a bitmap
        Bitmap picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)));
        //...rest of doWork
```

### Step 5 - Output temporary URI
You won't be using this yet, but let's go ahead and provide an **output Data** for the temporary URI
of our blurred photo. To do this:

1. Create a new `Data`, just as you did with the input, and store `outputUri` as a String. Use the 
   same key, `KEY_IMAGE_URI`.
   
2. Pass this to `Worker`'s `Result.success()` method.

#### [BlurWorker.java]

This line should follow the `Uri outputUri` line and replace `Result.success(outputData)` in 
`doWork()`:

```java
Data outputData = new Data.Builder()
    .putString(Constants.KEY_IMAGE_URI, outputUri.toString())
    .build();
return Result.success(outputData);
```

### Step 6 - Run your app
At this point you should run your app. It should compile and have the same behavior.

Optionally, you can open the **Device File Explorer** in Android Studio and navigate to 
**data>data>com.example.background>files>blur_filter_outputs><URI>** as you did in the last step.

Note that you might need to **Synchronize** to see your images:

![device_file_explorer_sync]

Great work! You've blurred an input image using WorkManager!

## Chain your Work
Right now you're doing a single work task: blurring the image. This is a great first step, but is 
missing some core functionality:

 * It doesn't clean up temporary files.
 * It doesn't actually save the image to a permanent file.
 * It always blurs the picture the same amount.
 
We'll use a `WorkManager` chain of work to add this functionality.

`WorkManager` allows you to create separate `WorkerRequest`s that run in order or parallel. In this 
step you'll create a chain of work that looks like this:

![work_request_chain]

The `WorkRequests` are represented as boxes.

Another really neat feature of chaining is that the output of one `WorkRequest` becomes the input of
the next `WorkRequest` in the chain. The input and output that is passed between each `WorkRequest` 
is shown as blue text.

### Step 1 - Create Cleanup and Save Workers
First, you'll define all the `Worker` classes you need. You already have a `Worker` for blurring an 
image, but you also need a `Worker` which cleans up temp files and a `Worker` which saves the image 
permanently.

Create two new classes in the `worker` package which extend `Worker`.

The first should be called `CleanupWorker`, the second should be called `SaveImageToFileWorker`.

### Step 2 - Add a constructor
Add a constructor to the `CleanupWorker` class:

```java
public CleanupWorker(
        @NonNull Context appContext,
        @NonNull WorkerParameters workerParams) {
    super(appContext, workerParams);
}
```

### Step 3 - Override and implement doWork() for CleanupWorker
`CleanupWorker` doesn't need to take any input or pass any output. It always deletes the temporary 
files if they exist. Because this is not a codelab about file manipulation, you can copy the code 
for the `CleanupWorker` below:

[CleanupWorker.java]
```java
public class CleanupWorker extends Worker {
    public CleanupWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    private static final String TAG = CleanupWorker.class.getSimpleName();

    @NonNull
    @Override
    public Worker.Result doWork() {
        Context applicationContext = getApplicationContext();
        
        try {
            File outputDirectory = new File(applicationContext.getFilesDir(),
                    Constants.OUTPUT_PATH);
            if (outputDirectory.exists()) {
                File[] entries = outputDirectory.listFiles();
                if (entries != null && entries.length > 0) {
                    for (File entry : entries) {
                        String name = entry.getName();
                        if (!TextUtils.isEmpty(name) && name.endsWith(".png")) {
                            boolean deleted = entry.delete();
                            Log.i(TAG, String.format("Deleted %s - %s",
                                    name, deleted));
                        }
                    }
                }
            }

            return Worker.Result.success();
        } catch (Exception exception) {
            Log.e(TAG, "Error cleaning up", exception);
            return Worker.Result.failure();
        }
    }
}
```

### Step 4 - Override and implement doWork() for SaveImageToFileWorker
`SaveImageToFileWorker` will take input and output. The input is a String stored with the key 
`KEY_IMAGE_URI`. And the output will also be a String stored with the key KEY_IMAGE_URI.

![save_image_to_file_worker]

Since this is still not a codelab about file manipulations, the code is below, with two TODOs for 
you to fill in the appropriate code for input and output. This is very similar to the code you wrote
in the last step for input and output (it uses all the same keys).

[SaveImageToFileWorker.java]
```java
public class SaveImageToFileWorker extends Worker {
    public SaveImageToFileWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    private static final String TAG = SaveImageToFileWorker.class.getSimpleName();

    private static final String TITLE = "Blurred Image";
    private static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault());

    @NonNull
    @Override
    public Worker.Result doWork() {
        Context applicationContext = getApplicationContext();

        ContentResolver resolver = applicationContext.getContentResolver();
        try {
            String resourceUri = ;// TODO get the input Uri from the Data object
            Bitmap bitmap = BitmapFactory.decodeStream(
                   resolver.openInputStream(Uri.parse(resourceUri)));
            String outputUri = MediaStore.Images.Media.insertImage(
                    resolver, bitmap, TITLE, DATE_FORMATTER.format(new Date()));
            if (TextUtils.isEmpty(outputUri)) {
                Log.e(TAG, "Writing to MediaStore failed");
                return Result.failure();
            }
            // TODO create and set the output Data object with the imageUri.
            return Worker.Result.success();
        } catch (Exception exception) {
            Log.e(TAG, "Unable to save image to Gallery", exception);
            return Worker.Result.failure();
        }
    }
}
```

### Step 5 - Create a WorkRequest Chain
You need to modify the `BlurViewModel`'s `applyBlur` method to execute a chain of `WorkRequests` 
instead of just one. Currently the code looks like this:

[BlurViewModel.java]
```java
OneTimeWorkRequest blurRequest =
     new OneTimeWorkRequest.Builder(BlurWorker.class)
             .setInputData(createInputDataForUri())
             .build();

mWorkManager.enqueue(blurRequest);
```

Instead of calling `WorkManager.enqueue()`, call `WorkManager.beginWith()`. This returns a 
[WorkContinuation], which defines a chain of WorkRequests. You can add to this chain of work 
requests by calling `then()` method, for example, if you have three `WorkRequest` objects, `workA`, 
`workB`, and `workC`, you could do the following:

```java
WorkContinuation continuation = mWorkManager.beginWith(workA);

continuation.then(workB) // FYI, then() returns a new WorkContinuation instance
        .then(workC)
        .enqueue(); // Enqueues the WorkContinuation which is a chain of work
```
      
This would produce and run the following chain of `WorkRequest`s:

![work_requests_chain]

Create a chain of a `CleanupWorker` `WorkRequest`, a `BlurImage` `WorkRequest` and a 
`SaveImageToFile` `WorkRequest` in `applyBlur`. Pass input into the `BlurImage` `WorkRequest`.

The code for this is below:

[BlurViewModel.java]
```java
void applyBlur(int blurLevel) {

    // Add WorkRequest to Cleanup temporary images
    WorkContinuation continuation =
        mWorkManager.beginWith(OneTimeWorkRequest.from(CleanupWorker.class));

    // Add WorkRequest to blur the image
    OneTimeWorkRequest blurRequest = new OneTimeWorkRequest.Builder(BlurWorker.class)
                    .setInputData(createInputDataForUri())
                    .build();
    continuation = continuation.then(blurRequest);


    // Add WorkRequest to save the image to the filesystem
    OneTimeWorkRequest save =
        new OneTimeWorkRequest.Builder(SaveImageToFileWorker.class)
            .build();
    continuation = continuation.then(save);

    // Actually start the work
    continuation.enqueue();
}
```

This should **compile** and **run**. You should be able to see whatever image you choose to blur now
saved in your **Pictures** folder:

![saved_blurred_image]

### Step 6 - Repeat the BlurWorker
Time to add the ability to blur the image different amounts. Take the `blurLevel` parameter passed 
into `applyBlur` and add that many blur `WorkRequest` operations to the chain. Only the first 
`WorkRequest` needs and should take in the uri input.


Note that this is a bit contrived for learning purposes. Calling our blur code three times is less 
efficient than having `BlurWorker` take in an input that controls the "level" of blur. But it's good
practice and shows the flexibility of `WorkManager` chaining.

Try it yourself and then compare with the code below:

[BlurViewModel.java]
```java
void applyBlur(int blurLevel) {

    // Add WorkRequest to Cleanup temporary images
    WorkContinuation continuation = mWorkManager.beginWith(OneTimeWorkRequest.from(CleanupWorker.class));

    // Add WorkRequests to blur the image the number of times requested
    for (int i = 0; i < blurLevel; i++) {
        OneTimeWorkRequest.Builder blurBuilder =
                new OneTimeWorkRequest.Builder(BlurWorker.class);

        // Input the Uri if this is the first blur operation
        // After the first blur operation the input will be the output of previous
        // blur operations.
        if ( i == 0 ) {
            blurBuilder.setInputData(createInputDataForUri());
        }

        continuation = continuation.then(blurBuilder.build());
    }

    // Add WorkRequest to save the image to the filesystem
    OneTimeWorkRequest save = new OneTimeWorkRequest.Builder(SaveImageToFileWorker.class)
            .build();
    continuation = continuation.then(save);

    // Actually start the work
    continuation.enqueue();
}
```

Superb "work"! Now you can blur an image as much or as little as you want! How mysterious!

![very_blurry]

## Ensure Unique Work
Now that you've used chains, it's time to tackle another powerful feature of WorkManager - unique 
work chains.

Sometimes you only want one chain of work to run at a time. For example, perhaps you have a work 
chain that syncs your local data with the server - you probably want to let the first data sync 
finish before starting a new one. To do this, you would use `beginUniqueWork` instead of 
`beginWith`; and you provide a unique `String` name. This names the entire chain of work requests so 
that you can refer to and query them together.

Ensure that your chain of work to blur your file is unique by using `beginUniqueWork`. Pass in 
`Constants.IMAGE_MANIPULATION_WORK_NAME` as the key. You'll also need to pass in an 
[ExistingWorkPolicy]. Your options are `REPLACE`, `KEEP` or `APPEND`.

You'll use `REPLACE` because if the user decides to blur another image before the current one is 
finished, we want to stop the current one and start blurring the new image.

The code for starting your unique work continuation is below:

[BlurViewModel.java]
```java
// REPLACE THIS CODE:
// WorkContinuation continuation = 
// mWorkManager.beginWith(OneTimeWorkRequest.from(CleanupWorker.class));
// WITH
WorkContinuation continuation = 
  mWorkManager
    .beginUniqueWork(IMAGE_MANIPULATION_WORK_NAME,
           ExistingWorkPolicy.REPLACE,
           OneTimeWorkRequest.from(CleanupWorker.class));
```

Blur-O-Matic will now only ever blur one picture at a time.

## Tag and Display Work Status
This section uses [LiveData] heavily, so to fully grasp what's going on you should be familiar with 
`LiveData`. `LiveData` is an observable, lifecycle-aware data holder.

You can check out the documentation or the [Android Lifecycle-aware components Codelab] if this is 
your first time working with `LiveData` or observables.

The next big change you'll do is to actually change what's showing in the app as the Work executes.

You can get the status of any `WorkRequest` by getting a `LiveData` that holds a [WorkInfo] object. 
`WorkInfo` is an object that contains details about the current state of a `WorkRequest`, including:

 * Whether the work is `BLOCKED`, `CANCELLED`, `ENQUEUED`, `FAILED`, `RUNNING` or `SUCCEEDED`
 * If the `WorkRequest` is finished, any output data from the work.
 
The following table shows three different ways to get `LiveData<WorkInfo>` or 
`LiveData<List<WorkInfo>>` objects and what each does.

| Type                 | WorkManager Method  | Description |
|----------------------|---------------------|-------------|
| Get work using **id**| getInfoByIdLiveData | Each `WorkRequest` has a unique ID generated by `WorkManager`; you can use this to get a single `LiveData<WorkInfo>` for that exact `WorkRequest`.|
| Get work using unique chain name | getWorkInfosForUniqueWorkLiveData | As you've just seen, `WorkRequest`s can be part of a unique chain. This returns `LiveData<List<WorkInfo>>` for all work in a single, unique chain of `WorkRequest`s.|
| Get work using a tag | getWorkInfosByTagLiveData | Finally, you can optionally tag any `WorkRequest` with a String. You can tag multiple `WorkRequest`s with the same tag to associate them. This returns the `LiveData<List<WorkInfo>>` for any single tag.|

You'll be tagging the `SaveImageToFileWorker` `WorkRequest`, so that you can get it using 
`getWorkInfosByTagLiveData`. You'll use a tag to label your work instead of using the `WorkManager` 
ID, because if your user blurs multiple images, all of the saving image `WorkRequest`s will have the
same tag but not the same ID. Also you are able to pick the tag.

You would not use `getWorkInfosForUniqueWorkLiveData` because that would return the `WorkInfo` for 
all of the blur `WorkRequests` and the cleanup `WorkRequest` as well; it would take extra logic to 
find the save image `WorkRequest`.

### Step 1 - Tag your work
In `applyBlur`, when creating the `SaveImageToFileWorker`, tag your work using the `String` constant
`TAG_OUTPUT` :

[BlurViewModel.java]
```java
OneTimeWorkRequest save = new OneTimeWorkRequest.Builder(SaveImageToFileWorker.class)
        .addTag(TAG_OUTPUT) // This adds the tag
        .build();
```

### Step 2 - Get the WorkInfo
Now that you've tagged the work, you can get the `WorkInfo`:

1. Declare a new variable called `mSavedWorkInfo` which is a `LiveData<List<WorkInfo>>`

2. In the `BlurViewModel` constructor, get the `WorkInfo` using 
   `WorkManager.getWorkInfosByTagLiveData`
   
3. Add a getter for `mSavedWorkInfo`

The code you need is below:

[BlurViewModel.java]
```java
// New instance variable for the WorkInfo
private LiveData<List<WorkInfo>> mSavedWorkInfo;

//In the BlurViewModel constructor
mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_OUTPUT);  

// Add a getter method for mSavedWorkInfo
LiveData<List<WorkInfo>> getSavedWorkInfo() { return mSavedWorkInfo; }
```

### Step 3 - Display the WorkInfo
Now that you have a `LiveData` for your `WorkInfo`, you can observe it in the `BlurActivity`. In the
observer:

1. Check if the list of `WorkInfo` is not null and if it has any `WorkInfo` objects in it - if not 
   then the Go button has not been clicked yet, so return.
   
2. Get the first `WorkInfo` in the list; there will only ever be one `WorkInfo` tagged with 
   `TAG_OUTPUT` because we made the chain of work unique.
   
3. Check whether the work status is finished, using `workInfo.getState().isFinished();`

4. If it's not finished, then call `showWorkInProgress()` which hides and shows the appropriate 
   views.
   
5. If it's finished then call `showWorkFinished()` which hides and shows the appropriate views.

Here's the code:

[BlurActivity.java]
```java
// Show work status, added in onCreate()
mViewModel.getSavedWorkInfo().observe(this, listOfWorkInfos -> {

    // If there are no matching work info, do nothing
    if (listOfWorkInfos == null || listOfWorkInfos.isEmpty()) {
        return;
    }

    // We only care about the first output status.
    // Every continuation has only one worker tagged TAG_OUTPUT
    WorkInfo workInfo = listOfWorkInfos.get(0);

    boolean finished = workInfo.getState().isFinished();
    if (!finished) {
        showWorkInProgress();
    } else {
        showWorkFinished();
    }
});
```

### Step 4 - Run your app
Run your app - it should compile and run, and now show a progress bar when it's working, as well as 
the cancel button:

![display_work_status]

## Show Final Output
Each `WorkInfo` also has a [getOutputData] method which allows you to get the output `Data` object 
with the final saved image. Let's display a button that says **See File** whenever there's a blurred 
image ready to show.

### Step 1 - Create mOutputUri
Create a variable in `BlurViewModel` for the final URI and provide getters and setters for it. To 
turn a `String` into a `Uri`, you can use the `uriOrNull` method.

You can use the code below:

[BlurViewModel.java]
```java
// New instance variable for the WorkInfo
private Uri mOutputUri;

// Add a getter and setter for mOutputUri
void setOutputUri(String outputImageUri) {
    mOutputUri = uriOrNull(outputImageUri);
}

Uri getOutputUri() { return mOutputUri; }
```

### Step 2 - Create the See File button
There's already a button in the `activity_blur.xml` layout that is hidden. It's in `BlurActivity` 
and called `mOutputButton`.

Setup the click listener for that button. It should get the URI and then open up an activity to view
that URI. You can use the code below:

[BlurActivity.java]
```java
// Inside onCreate()
mOutputButton.setOnClickListener(view -> {
    Uri currentUri = mViewModel.getOutputUri();
    if (currentUri != null) {
        Intent actionView = new Intent(Intent.ACTION_VIEW, currentUri);
        if (actionView.resolveActivity(getPackageManager()) != null) { 
            startActivity(actionView);
        }
    }
});
```

### Step 3 - Set the URI and show the button
There are a few final tweaks you need to apply to the `WorkInfo` observer to get this to work (no 
pun intended):

1. If the `WorkInfo` is finished, get the output data, using `workInfo.getOutputData()`.

2. Then get the output URI, remember that it's stored with the `Constants.KEY_IMAGE_URI` key.

3. Then if the URI isn't empty, it saved properly; show the `mOutputButton` and call `setOutputUri` 
   on the view model with the uri.

[BlurActivity.java]
```java
// Show work info, goes inside onCreate()
mViewModel.getOutputWorkInfo().observe(this, listOfWorkInfo -> {

    // If there are no matching work info, do nothing
    if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
        return;
    }

    // We only care about the first output status.
    // Every continuation has only one worker tagged TAG_OUTPUT
    WorkInfo workInfo = listOfWorkInfo.get(0);

    boolean finished = workInfo.getState().isFinished();
    if (!finished) {
        showWorkInProgress();
    } else {
        showWorkFinished();
        Data outputData = ;// TODO get the output Data from the workInfo

        String outputImageUri = ;// TODO get the Uri from the Data using the 
        // Constants.KEY_IMAGE_URI key

        // If there is an output file show "See File" button
        if (!TextUtils.isEmpty(outputImageUri)) {
            // TODO set the output Uri in the ViewModel
            // TODO show mOutputButton
        }
    }
});
``` 

### Step 4 - Run your code
Run your code. You should see your new, clickable **See File** button which takes you to the 
outputted file:

![see_file_button]

## Cancel work

You added the **Cancel Work** button, so let's add the code to make it do something. With 
`WorkManager`, you can cancel work using the id, by tag and by unique chain name.

In this case, you'll want to cancel work by unique chain name, because you want to cancel all work 
in the chain, not just a particular step.

### Step 1 - Cancel the work by name
In the view model, write the method to cancel the work:

[BlurViewModel.java]
```java
/**
 * Cancel work using the work's unique name
 */
void cancelWork() {
    mWorkManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME);
}
```

### Step 2 - Call cancel method
Then, hook up the button mCancelButton to call cancelWork:

[BlurActivity.java]
```java
// In onCreate()
        
// Hookup the Cancel button
mCancelButton.setOnClickListener(view -> mViewModel.cancelWork());
```
### Step 3 - [OPTIONAL] Slow down work and show notifications
Optionally, there are two static methods in the `WorkerUtils` class which you can call to show a 
notification when the work starts, and to artificially slow down the speed of the work. This is 
helpful to see the work actually get cancelled and to slow things down on emulated devices which run
`WorkRequest`s very quickly.

Place at the start of onWork for All Workers

```java
WorkerUtils.makeStatusNotification("Doing <WORK_NAME>", applicationContext);
WorkerUtils.sleep();
```

### Step 4 - Run and cancel your work
Run your app. It should compile just fine. Start blurring a picture and then click the cancel 
button. The whole chain is cancelled!

![cancelled_work]


[codelab]: https://codelabs.developers.google.com/codelabs/android-workmanager/#0
[blur-o-matic_1]: blur-o-matic_1.png "Background Work with WorkManager" 
[blur-o-matic_2]: blur-o-matic_2.png "Background Work with WorkManager"
[Worker]: https://developer.android.com/reference/androidx/work/Worker.html
[WorkRequest]: https://developer.android.com/reference/androidx/work/WorkRequest.html
[WorkManager]: https://developer.android.com/reference/androidx/work/WorkManager.html
[Constraints]: https://developer.android.com/reference/androidx/work/Constraints.html
[BlurWorker.java]: src/main/java/com/example/background/workers/BlurWorker.java
[BlurViewModel.java]: src/main/java/com/example/background/BlurViewModel.java
[BlurActivity.java]: src/main/java/com/example/background/BlurActivity.java
[first_request_step6]: first_request_step6.png "Step 6 Result"
[device_file_explorer1]: device_file_explorer1.png "Locating Device File Explorer"
[device_file_explorer2]: device_file_explorer2.png "Confirm blurred fish"
[Data]: https://developer.android.com/reference/androidx/work/Data
[device_file_explorer_sync]: device_file_explorer_sync.png "Synchronize Device File Explorer"
[work_request_chain]: work_request_chain.png "Work request chain"
[CleanupWorker.java]: src/main/java/com/example/background/workers/CleanupWorker.java
[save_image_to_file_worker]: save_image_to_file_worker.png "SaveImageToFileWorker will take input and output"
[SaveImageToFileWorker.java]: src/main/java/com/example/background/workers/SaveImageToFileWorker.java
[WorkContinuation]: https://developer.android.com/reference/androidx/work/WorkContinuation
[work_requests_chain]: work_requests_chain.png "WorkRequests Chain"
[saved_blurred_image]: saved_blurred_image.png "Blurred images are now saved"
[very_blurry]: very_blurry.png "Mysterious!"
[ExistingWorkPolicy]: https://developer.android.com/reference/androidx/work/ExistingWorkPolicy
[LiveData]: https://developer.android.com/topic/libraries/architecture/livedata
[Android Lifecycle-aware components Codelab]: https://codelabs.developers.google.com/codelabs/android-lifecycles/#0
[WorkInfo]: https://developer.android.com/reference/androidx/work/WorkInfo
[display_work_status]: display_work_status.png "Display work status"
[getOutputData]: https://developer.android.com/reference/androidx/work/WorkInfo.html#getOutputData()
[see_file_button]: see_file_button.png "See File Button"
[cancelled_work]: cancelled_work.png "Cancelled Work"