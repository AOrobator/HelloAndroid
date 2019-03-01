# Lesson 7: Networking with Retrofit

In this lesson, we'll make an app that fetches random number facts from [numbersapi.com](http://numbersapi.com/).

![Number Fact App][number_fact]

## Offline First

The first thing to keep in mind while developing network connected applications, is that you should 
design and code for the offline experience first. An internet connection is a rather fickle thing 
and your users won't always be connected. Even if the user is connected, it's also a good idea to 
check what type of network you're connected to. If the user has a limited mobile data plan, it may 
be best to only do certain tasks when connected to Wifi.

In this lesson, we'll be creating an app that gets random facts about numbers. Like mentioned 
previously, after we create our layout, we'll want to check whether we are connected to the 
internet. Doing so requires that we declare the `ACCESS_NETWORK_STATE` permission in our 
AndroidManifest.xml.

```xml
<manifest>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
</manifest>
```

After declaring this permission, we'll create an interface called `ConnectionChecker`, which will 
abstract away the Android implementation of checking for connections. This improves testability, 
leak safety, and modularity. A general rule of thumb is to make sure there are no android.* imports 
in your ViewModels (with exceptions like android.arch.*). The Android implementation can be found here:

[AndroidConnectionChecker.java]

```java
public class AndroidConnectionChecker implements ConnectionChecker {
  private final Application app;

  public AndroidConnectionChecker(Application app) {
    this.app = app;
  }

  @Override
  public boolean isConnected() {
    ConnectivityManager cm =
        (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}

```

In our ViewModel, when the user clicks to get a random fact, we first check for an internet 
connection. If there is one, we'll do the network call. Otherwise, we'll alert the user that they 
have no internet connection. We want this to be an observable event, where the view can just 
subscribe to updates. We also want to be aware of the Activity lifecycle and not react to this event 
when the Activity no longer exists. Let's take a look at the scope of our ViewModel.

![ViewModel Scope][view_model_scope]

Passing a reference of the View (activity or fragment) to the ViewModel is a **serious risk**. Let’s 
assume the ViewModel requests data from the network and the data comes back some time later. At that
moment, the View reference might be destroyed or might be an old activity that is no longer visible,
generating a memory leak and, possibly, a crash.

## The Observer Pattern

![Observer Pattern][observer_pattern]

To get around this problem, we'll use the observer pattern and implement it with LiveData. 

LiveData is an observable data holder. Other components in your app can monitor changes to objects 
using this holder without creating explicit and rigid dependency paths between them. The LiveData 
component also respects the lifecycle state of your app's components—such as activities, fragments, 
and services—and includes cleanup logic to prevent object leaking and excessive memory consumption.

A very convenient way to design the presentation layer in Android is to have the View observe 
(subscribe to changes in) the ViewModel. Since the ViewModel doesn’t know about Android, it doesn’t 
know how Android likes to kill Views frequently. This has some advantages:

1. ViewModels are persisted over configuration changes, so there’s no need to re-query an external 
   source for data (such as a database or the network) when a rotation happens.

2. When long-running operations finish, the observables in the ViewModel are updated. It doesn’t 
   matter if the data is being observed or not. No null pointer exceptions happen when trying to update the nonexistent View.

3. ViewModels don’t reference views so there’s less risk of memory leaks.

In our ViewModel, we'll declare an instance of `MutableLiveData` to publish events on. We'll expose 
it as a `LiveData` (its parent class), so that Views are not able to post any events, only listen.

```java
public class NumberFactViewModel extends ObservableViewModel {
  private final MutableLiveData<NetworkState> networkStateMutableLiveData =
      new MutableLiveData<>();

  public LiveData<ViewEvent<NetworkState>> getNetworkStateLiveData() {
    return networkStateMutableLiveData;
  }
  
  public void getRandomFact() {
    if (connectionChecker.isConnected()) {
      // Do network call
    } else {
      networkStateMutableLiveData.setValue(DISCONNECTED);
    }
  }
  
  public enum NetworkState {
    DISCONNECTED
  }
}
```

## How LiveData works: Main Thread Intro
LiveData holds a value, this value may change by calling to one of the following methods setValue or
postValue, whenever the value changed the LiveData might notify its observers about the change. When
calling setValue the value is immediately change but you must be in the UI thread otherwise you 
should use postValue which write the new value asynchronously. In this case, we haven't done any 
threading, so by default we're on the main thread. This is why we call setValue instead of 
postValue. It's possible to always call postValue and not think about what thread you're on, but you
should always be very aware of how you're using threads.

I've mentioned main thread a couple of times, but I haven't actually said what that is. Android's 
Main thread a.k.a. the UI thread, as you might’ve guessed, is used to process the UI. Any time a 
user clicks a button or scrolls on their screen, the main thread processes that event and renders a 
new frame for the app at 60 frames per second (fps). 60 fps equates to a deadline of 16ms per frame.

You can think of the way that Android draws frames as a train that arrives every 16ms. Every time 
that train comes, you have to finish updating the UI so a new frame can be rendered, otherwise you 
miss the train that will draw that particular frame to the screen. The longer you take to finish 
updating the UI, the more trains you miss. More trains missed means more frames not drawn to the 
screen.

Another way of putting it is that the frame rate drops. A drop in frame rate is perceived by the 
user as the app being frozen, lagging, or janky. You don’t want a janky app.

Long running tasks on the main thread will result in frame drops, which is why any long running task
should be done off the main thread. Long running tasks includes any IO such as networking or reading 
from disk, as well as any long computations.


## Observing LiveData

In the View, we'll observe changes to this LiveData. In the observe method we pass in a 
LifecycleOwner, the current Activity, and an Observer, which also happens to be the current 
Activity. Then we'll implement the required `onChanged()` method since we're implementing the 
Observer interface. In `onChanged()` we'll check if the network is in the disconnected state, and if
it is, we'll show a snackbar.

Now if we run this, it all seems to work ... until you rotate your phone. LiveData will always emit the
last value when an Observer starts to observe. This means that when we rotate the phone, the 
Disconnected event gets fired again. This is not desired behavior and we to able to consume an event
only once. We'll model this behavior with the `ViewEvent` class.

[ViewEvent.java]

```java
public class ViewEvent<T> {
  private T data;

  public ViewEvent(T data) {
    this.data = data;
  }

  public @Nullable T consume() {
    if (data == null) {
      return null;
    } else {
      T tmp = data;
      data = null;
      return tmp;
    }
  }
} 
```

Now, if we pass `ViewEvent`s instead of directly passing a `NetworkState`, we get the behavior we 
want. We have to modify our ViewModel and Observer slightly but it's worth it. Here's a snippet of
`NetworkActivity.java`

```java
public class NetworkingActivity extends AppCompatActivity
    implements Observer<ViewEvent<NetworkState>> {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    viewModel.getNetworkStateLiveData().observe(this, this);
  }

  @Override 
  public void onChanged(ViewEvent<NetworkState> event) {
    NetworkState state = event.consume();

    if (state == NetworkState.DISCONNECTED) {
      Snackbar
        .make(binding.root, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
        .show();
    }
  }
}
```

## Testing LiveData

In order to unit test with LiveData, we'll have to add Android's Core Testing library to our
project.

```groovy
dependencies {
  testImplementation 'androidx.arch.core:core-testing:2.0.0'
}
```

Once we do that, we'll get access to a JUnit Test Rule called `InstantTaskExecutorRule`. This swaps
the background executor used by the Architecture Components with a different one which executes each
task synchronously. This relates to Android's main thread, which we will cover soon.

Once we have an `InstantTaskExecutorRule` as a TestRule we can perform assertions on the value of 
the LiveData.

[NumberFactViewModelUnitTest]

```kotlin
class NumberFactViewModelUnitTest {

  @get:Rule
  var rule: TestRule = InstantTaskExecutorRule()

  @Test
  fun `When disconnected and random fact clicked, user alerted of no connectivity`() {
    val viewModel = NumberFactViewModel()
    viewModel.networkStateLiveData.value `should equal` null
    val connectionChecker = mock<ConnectionChecker> {
      on { isConnected } doReturn false
    }

    viewModel.init(connectionChecker)
    viewModel.getRandomFact()

    viewModel.networkStateLiveData.value `should equal` ViewEvent(DISCONNECTED)
    verify(connectionChecker).isConnected
  }
}
```

## Making Network Requests

To make the actual network request, we'll first need to add the internet permission to our manifest.

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

Since we're using HTTP and not HTTPS for this demo, we're also going to set the 
`usesCleartextTraffic` attribute to true. 

```xml
<application
      android:usesCleartextTraffic="true">
```

On Android 9+ (API 28+) cleartext traffic is disabled by default to increase security.

For our networking library, we'll use OkHttp. Add this dependency to the app level build.gradle 
file.

```groovy
dependencies {
  implementation "com.squareup.okhttp3:okhttp:3.13.1"
}
```

Here's a snippet of what using OkHttp is like.

```java
OkHttpClient client = new OkHttpClient();
Request request = new Request.Builder()
    .url("http://numbersapi.com/" + inputNumber)
    .addHeader("Content-Type", "application/json")
    .build();

client.newCall(request).enqueue(new Callback() {
  @Override 
  public void onFailure(Call call, IOException e) {
    updateFact(e.getMessage());
  }

  @Override 
  public void onResponse(Call call, Response response) throws IOException {
    if (response.isSuccessful() && response.body() != null) {
      updateFact(response.body().string());
    }
  }
});
```

See [OkHttpNumberFactViewModel] for the full implementation.

## Using Retrofit

Retrofit is a library that eliminates a lot of boiler plate code by turning your HTTP API into a 
Java interface. This will be the preferred way of networking going forward. To get started with this
library, we'll have to add several dependencies to our build.gradle:

```groovy
dependencies {
  implementation "com.squareup.okhttp3:logging-interceptor:3.13.1"

  implementation 'io.reactivex.rxjava2:rxjava:2.2.7'
  implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
  
  implementation "com.squareup.retrofit2:retrofit:2.5.0"
  implementation "com.squareup.retrofit2:converter-gson:2.5.0"
  implementation "com.squareup.retrofit2:adapter-rxjava2:2.5.0"
}
```
The first dependency, logging-interceptor, will help us log all Retrofit calls to the Logcat.

```java
OkHttpClient client = new OkHttpClient.Builder()
  .addInterceptor(
    new HttpLoggingInterceptor(message -> Log.d("Retrofit", message))
      .setLevel(HttpLoggingInterceptor.Level.BODY)
  )
  .build();
```

Building our Retrofit client consists of several steps.

```java
Retrofit retrofit = new Retrofit.Builder()
  .client(client)
  .baseUrl("http://numbersapi.com")
  .addConverterFactory(GsonConverterFactory.create())
  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  .build();
```
First we set the OkHttpClient, and this makes sure that we'll be logging all API requests. Next, 
we'll set the base url, so that Retrofit knows what API to hit. After that we'll add a
`GsonConverterFactory` as a converter factory. This will use a library called Gson to parse received 
JSON into model objects. Instead of having to parse the JSON ourselves, this will allow our HTTP API
interface to return a `Call<NumberFact>` instead of a generic `Call<Response>`. 

To enable this parsing, we'll have to create the NumberFact class, and annotate it appropriately.

[NumberFact.java]

```java
public class NumberFact {
  @SerializedName("text")
  public final String text;

  @SerializedName("number")
  public final int number;

  @SerializedName("found")
  public final boolean found;

  @SerializedName("type")
  public final String type;

  NumberFact(String text, int number, boolean found, String type) {
    this.text = text;
    this.number = number;
    this.found = found;
    this.type = type;
  }
}
```

As you can see, each field is annotated with `@SerializedName`. This lets the converter factory know 
which JSON attributes should be assigned to what field.

After adding a Converter, we'll add an `RxJava2CallAdapter`. RxJava2 is a library that implements 
many aspects of the Observer pattern, similar to LiveData. Unlike LiveData, RxJava2 comes with many 
functions out of the box to help transform your data. By adding this call adapter, our network 
requests will be returned as Observable streams of data. 

Now that we've set up Retrofit correctly, we can implement our API Interface.

[NumbersApi.java]

```java
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NumbersApi {
  @GET("{number}/trivia?json")
  Single<NumberFact> getTriviaFact(@Path("number") int number);
}
```
First we'll define a function called getTriviaFact. This will return a `Single<NumberFact>`. A 
`Single` is an RxJava concept. It models an observable stream that only emits once. This perfectly 
represents our network call. After we've defined our method signature, we'll need to annotate the 
method with the particular path this method will be hitting. We'll use the `@Path` annotation to 
specify variables in the path. Now that our API interface is complete, we'll instantiate an instance
of it with Retrofit:

```java
NumbersApi numbersApi = retrofit.create(NumbersApi.class);
```

### The Repository Pattern

Instead of passing the NumbersApi directly to our ViewModel, we'll wrap it in an implementation of a
Repository. It's possible for apps to get their data from all types of sources: a file saved in 
internal storage, a database, or the network. The repository will figure out how to get that data, 
and will return it, potentially caching it.

### Schedulers

The next thing that we're passing into our ViewModel is a set of Schedulers. A Scheduler is an 
RxJava entity that defines what thread an operation should run on. We'll use schedulers to indicate 
that the network call should be done on a background thread, with the result being delivered on the 
main thread.

### Making the network call

Our ViewModel is now ready to make a Retrofit networking call.

```
// Do network call
Integer number = toIntOrNull(inputNumber);

if (number != null) {
  disposable = numbersRepo.getTriviaFact(number)
      .subscribeOn(schedulers.io)
      .observeOn(schedulers.main)
      .subscribe(this::onGetFactSuccess, this::onGetFactError);
}
```

First we'll call our repository to get an instance of `Single<NumberFact>`. We call subscribeOn with
the io scheduler to indicate that this work should be done using the IO scheduler/on a background 
thread. We call observeOn with the main thread scheduler to indicate that the result of this 
operation should be delivered on the main thread. Next, we'll call subscribe passing in a success 
and error handler. The call to subscribe will return a Disposable. When we call dispose on the 
Disposable, we will no longer receive updates for this network call. We call dispose in `onCleared`
when the ViewModel is destroyed to prevent leaking our ViewModel.

## Unit Testing Network Requests

We want to be able to unit test our networking code. At the same time, we also want our tests to be 
hermetic. We don't want our tests to fail because they're hitting the real API and the API happens 
to be down. To work around this, we'll use OkHttp's MockWebServer library. As its name suggests, 
this is a mock web server where we can tell it how to respond to certain requests. 

Whenever I find myself using MockWebServer, I like to write a DSL to make the testing a bit easier 
and more readable. We'll end up with something like this:

```kotlin
server `received request` ExpectedRequest(
    authorization = null,
    method = GET,
    contentType = null,
    path = "/2/trivia?json",
    body = ""
)
``` 

We'll create this in a separate Java library module so that it can be re-used by other modules in 
this project. Creating this in a separate module provides the benefit of not having to recompile the
code when a particular test changes. It also greatly improves modularity and re-usability.

After we create the module, we'll write several supporting data structures for our tests. The 
first is an enum called HttpMethod, which lists all 5 HTTP methods. Next we have a content type enum.
There are only 2 content types here although there are obviously many more that exist. I've found 
that the JSON and form-url-encoded are the most common types that I run into, but feel free to add 
your own as the need arises. Lastly, we'll make an ExpectedRequest data class that holds the 
attributes of the request we're interested in verifying.

For the actual assertion, we'll create an infix extension function on MockWebServer called 
`received request`. As you may have guessed, this testing DSL takes inspiration from the fluent 
assertions library Kluent. It looks like this:

[MockWebServerAssertions.kt]

```kotlin
infix fun MockWebServer.`received request`(expectedRequest: ExpectedRequest) {
  val actualRequest: RecordedRequest = takeRequest()

  actualRequest `has authorization` expectedRequest.authorization
  actualRequest `has method` expectedRequest.method.name
  actualRequest `has content type` expectedRequest.contentType?.type
  actualRequest `has path` expectedRequest.path
  actualRequest `has body` expectedRequest.body
}
```
The first thing we do is call `takeRequest()` which returns the last request received by 
MockWebServer. Then we assert that various elements of the RecordedRequest are what we expect them 
to be.

Now that we've declared a nicer way to check that we're sending out the right request, we'll make a 
base test class called BaseApiTest that will open and close the MockWebServer for us, as well as 
provide us with the base url of the mock server.

[BaseApiTest.kt] 

```kotlin
abstract class BaseApiTest {
  protected lateinit var server: MockWebServer
    private set

  protected val baseUrl: String
    get() = server.url("/").toString()

  @Before open fun setup() {
    server = MockWebServer()
    server.start()
  }

  @After open fun teardown() = server.shutdown()
}
```

Now we're ready to write our tests! I know this was a lot of setup, but it's only done once per 
project. You'll soon see that it will pay off.

In our setup() function for NumberRepositoryUnitTest, we initialize our Retrofit instance almost 
identically to how we did before, with two major differences. The first difference is that we are 
logging with println instead of the Logcat. On JVM tests we don't have access to the Android 
framework. The second and arguably bigger difference is that instead of pointing our Retrofit 
instance to the real Numbers API, we are pointing it to our MockWebServer. This will allow us to 
verify the requests. Now let's take a look at the actual test:

[NumberRepositoryUnitTest.kt]

```kotlin
  @Test
  fun getTriviaFact() {
    server.enqueue(triviaNumberFactResponse)

    val testObserver: TestObserver<NumberFact> =
      numbersRepository
          .getTriviaFact(2)
          .test()

    val expectedFact = NumberFact(
        "2 is the first magic number in physics.",
        2,
        true,
        "trivia"
    )

    testObserver `completed with single value` expectedFact
    server `received request` ExpectedRequest(
        authorization = null,
        method = GET,
        contentType = null,
        path = "/2/trivia?json",
        body = ""
    )
  }
```

The first thing we do in this test is enqueue the proper response. This sets up the server to return
the given response when it receives any request.  Next, we'll make our API call with a TestObserver 
which will allow us to verify the expected parsed response. Afterwards, we'll declare our 
expectedFact and verify that our TestObserver received the expectedFact. Finally we assert that our 
mock server received the expected request.

## Inspect network traffic with Network Profiler

Now that we're sending network requests, we'll do a bit of inspection with Android Studio's built in
Network Profiler.

The Network Profiler displays realtime network activity on a timeline, showing data sent and 
received, as well as the current number of connections. This lets you examine how and when your app 
transfers data, and optimize the underlying code appropriately.

To open the Network Profiler, follow these steps:

1. Click **View > Tool Windows > Profiler** (you can also click **Profile** in the toolbar).

2. Select the device and app process you want to profile from the Android Profiler toolbar. If 
   you've connected a device over USB but don't see it listed, ensure that you have enabled USB 
   debugging.
   
3. Click anywhere in the NETWORK timeline to open the Network Profiler.

To select a portion of the timeline, inspect a list of network requests sent and responses received, 
or view detailed information about a selected file, you must either be on API 26+ (8.0+) enable 
advanced profiling.

### Why Profile Network Activity?

When your app makes a request to the network, the device must use the power-hungry mobile or WiFi 
radios to send and receive packets. The radios not only use power to transfer data, but also use 
extra power to turn on and to stay awake.

Using the Network Profiler, you can look for frequent, short spikes of network activity, which mean 
that your app requires the radios to turn on frequently, or to stay awake for long periods to handle 
many short requests close together. This pattern indicates that you may be able to optimize your app 
for improved battery performance by batching network requests, thereby reducing the number of times 
the radios must turn on to send or receive data. This also allows the radios to switch into 
low-power mode to save battery in the longer gaps between batched requests.

### Network Profiler Overview

At the top of the window, you can see the event timeline and **`1`** radio power state (high/low) vs 
Wi-Fi. On the timeline, you can **`2`** click and drag to select a portion of the timeline to inspect 
the traffic.

![Network Profiler][network_profiler]

In the **`3`** pane below the timeline, select one of the following tabs for more detail about the 
network activity during the selected portion of the timeline:

  * Connection View: Lists files that were sent or received during the selected portion of the 
    timeline across all of your app's CPU threads. For each request, you can inspect the size, type, 
    status, and transmission duration. You can sort this list by clicking any of the column headers. 
    You also see a detailed breakdown of the selected portion of the timeline, showing when each 
    file was sent or received.
  
  * Thread View: Displays network activity of each of your app's CPU threads. As shown in the below,
    this view allows you to inspect which of your app's threads are responsible for each network 
    request. 
    
![Network Profiler Thread View][network_profiler_thread_view]

From either the Connection View or Thread View, click a request name to inspect **`4`** detailed 
information about the data sent or received. Click the tabs to view the response header and body, 
request header and body, or call stack.

On the Response and Request tabs, click the View Parsed link to display formatted text and click the 
View Source link to display raw text.

![Network Profiler Response][network-profiler-text]

[number_fact]: number_fact.jpg "number_fact" 
[AndroidConnectionChecker.java]: src/main/java/com/orobator/helloandroid/lesson7/viewmodel/AndroidConnectionChecker.java
[view_model_scope]: view_model_scope.png "view_model_scope"
[observer_pattern]: observer_pattern.png "observer_pattern"
[ViewEvent.java]: src/main/java/com/orobator/helloandroid/lesson7/viewmodel/ViewEvent.java
[NumberFactViewModelUnitTest]: src/test/java/com/orobator/helloandroid/lesson7/viewmodel/NumberFactViewModelUnitTest.kt
[OkHttpNumberFactViewModel]: src/main/java/com/orobator/helloandroid/lesson7/viewmodel/OkHttpNumberFactViewModel.java
[NumberFact.java]: src/main/java/com/orobator/helloandroid/lesson7/model/api/NumberFact.java
[NumbersApi.java]: src/main/java/com/orobator/helloandroid/lesson7/model/api/NumbersApi.java
[MockWebServerAssertions.kt]: ../mock-web-server-assertions/src/main/java/com/orobator/mockwebserverassertions/MockWebServerAssertions.kt
[BaseApiTest.kt]: ../mock-web-server-assertions/src/main/java/com/orobator/mockwebserverassertions/BaseApiTest.kt
[NumberRepositoryUnitTest.kt]: src/test/java/com/orobator/helloandroid/lesson7/model/api/NumberRepositoryUnitTest.kt
[network_profiler]: networkprofiler.png "Network Profiler"
[network_profiler_thread_view]: network_profiler_thread_view.png "Thread View"
[network-profiler-text]: network-profiler-text.png "Network Profiler Response"