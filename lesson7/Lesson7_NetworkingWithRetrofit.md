# Lesson 7: Networking with Retrofit

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

[AndroidConnectionChecker.java]: src/main/java/com/orobator/helloandroid/lesson7/viewmodel/AndroidConnectionChecker.java
[view_model_scope]: view_model_scope.png "view_model_scope"
[observer_pattern]: observer_pattern.png "observer_pattern"
[ViewEvent.java]: src/main/java/com/orobator/helloandroid/lesson7/viewmodel/ViewEvent.java
[NumberFactViewModelUnitTest]: src/test/java/com/orobator/helloandroid/lesson7/viewmodel/NumberFactViewModelUnitTest.kt
[OkHttpNumberFactViewModel]: src/main/java/com/orobator/helloandroid/lesson7/viewmodel/OkHttpNumberFactViewModel.java