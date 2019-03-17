# Lesson 9: Dependency Injection / Dagger 2

## What is a Dependency?
 
Let's imagine that we're making chocolate chip pancakes. We'll need pancake batter, and chocolate 
chips. Since we need these two things to make breakfast, we can say that Chocolate Chip Pancakes has
two direct **dependencies**. One on chocolate chips, and one on the batter. Chocolate chips depend 
on cocoa beans, which need soil and sunlight. Therefore Chocolate Chip Pancakes has an **indirect 
dependency** on cocoa beans, soil, and sunlight.

Say we have Class A. In code, a dependency is any class needed by Class A in order to perform the 
desired functionality.

Looking at some code we wrote earlier, we can say that `RetrofitNumberFactViewModel` has several
dependencies, some of which we pass in through an init function, and others we instantiate 
ourselves.

```java
public class RetrofitNumberFactViewModel extends ObservableViewModel {
  // These are the dependencies
  public String inputNumber = "";
  public String outputFact = "";
  private MutableLiveData<ViewEvent<NetworkState>> networkStateMutableLiveData = new MutableLiveData<>();
  private ConnectionChecker connectionChecker;
  private NumbersRepository numbersRepo;
  private AppSchedulers schedulers;
  private Disposable disposable;
}
``` 

## Potential Dependency Problems: Tight vs. Loose Coupling

When Class A depends on Class B, we say the 2 classes are coupled. A and B are loosely coupled if an
instance of B is passed into A's constructor, or a method, or B's implementation is hidden behind an
interface. A and B are tightly coupled if A directly creates an instance of B itself. If we don't 
handle the way dependencies are managed well, and have a lot of tightly coupled dependencies, we can 
run into some sticky situations down the road.

For a concrete example, let's compare the OkHttp and Retrofit NumberFactViewModels. 

```java
public class OkHttpNumberFactViewModel extends ObservableViewModel {
  public void getRandomFact() {
    if (connectionChecker.isConnected()) {
      // Do network call
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
          .url("http://numbersapi.com/" + inputNumber)
          .addHeader("Content-Type", "application/json")
          .build();
    } 
  }
}
```

As we can see, OkHttpNumberFactViewModel has a direct dependency on OkHttpClient, and the real 
Numbers API. That second dependency is more concerning. Because this ViewModel has a direct 
dependency on the real API we're using, that means that we can't unit test this class without the 
ViewModel hitting the real API. This ViewModel is **tightly coupled** to the real Numbers API. 

Compare this to RetrofitNumberFactViewModel, which has a dependency on the NumbersRepository 
interface, which gets passed in via a method.

```java
public class RetrofitNumberFactViewModel extends ObservableViewModel {

  private NumbersRepository numbersRepo;

  public void init(NumbersRepository repository) {
    numbersRepo = repository;
  }
}
```

RetrofitNumberFactViewModel is **loosely coupled** to the Numbers API. In our test for checking that 
clicking the random fact button will update the UI with a NumberFact, we don't even pass in an 
implementation of NumbersRepository that connects to the internet! Loose coupling allows us to mock 
out dependencies and focus on testing only one class.

In addition to loose coupling improving testability, it also improves modularity, reusability, and 
maintainability.

## What is Dependency Injection?

Dependency injection is a technique where one object supplies the dependencies of another object. 
A dependency is an object that can be used (a service). An injection is the passing of a dependency 
to a dependent object (a client) that would use it. The service is made part of the client’s state. 
Passing the service to the client, rather than allowing a client to build or find the service, is 
the fundamental requirement of the pattern.

In the RetrofitNumberFactViewModel, the Activity passes in the required dependencies via the init 
method. This is a form of dependency injection. Dependency Injection builds upon the concept of 
Inversion of Control by letting another object create your dependencies instead of creating them 
yourself. In simple words, no class should instantiate another class. Instead, it should get the 
instances from a configuration class.

In an application that relies on dependency injection, the objects never have to hunt around for 
dependencies or construct them themselves. All the dependencies are provided to them or injected 
into them so that they are ready to be used.

## Dagger 2

The app we made in Lesson 7 works, and NetworkingActivity passes in all dependencies to our 
ViewModel. But what if we had several Activities that all needed an instance of either Retrofit or 
the NumbersApi? We wouldn't want to create a new instance every time, and we'd need some way of 
storing all those dependencies. Our dependency graph (what depends on what) is also very simple, but
it has the potential to become very complex as our application grows. We don't want to manage our 
dependency graph by hand manually, as that will be quite error prone. Luckily, Dagger 2 can solve 
these problems for us.

Dagger is a fully static, compile-time dependency injection framework for both Java and Android. It 
is the only DI framework which generates fully traceable source code in Java which mimics the code 
that a developer may write by hand.

To get started with Dagger 2, we'll have to add the appropriate dependencies (ha) to our 
build.gradle: 

```groovy
dependencies {
  implementation 'com.google.dagger:dagger:2.x'
  annotationProcessor 'com.google.dagger:dagger-compiler:2.x'
  implementation 'com.google.dagger:dagger-android-support:2.x' // if you use the support libraries
  annotationProcessor 'com.google.dagger:dagger-android-processor:2.x'
}
```

Next, we'll want to do some refactoring. Instead of passing our dependencies for NumberFactViewModel
through a method, we'll get them through the constructor.

```java
public NumberFactViewModel(
    ConnectionChecker connectionChecker,
    NumbersRepository numbersRepo,
    AppSchedulers schedulers) {
  this.connectionChecker = connectionChecker;
  this.numbersRepo = numbersRepo;
  this.schedulers = schedulers;
}
```

After that, we'll tell Android how to create our ViewModel by creating NumberFactViewModelFactory.

```java
public class NumberFactViewModelFactory implements ViewModelProvider.Factory {
  private final ConnectionChecker connectionChecker;
  private final NumbersRepository numbersRepo;
  private final AppSchedulers schedulers;

  public NumberFactViewModelFactory(
      ConnectionChecker connectionChecker,
      NumbersRepository numbersRepo, AppSchedulers schedulers) {
    this.connectionChecker = connectionChecker;
    this.numbersRepo = numbersRepo;
    this.schedulers = schedulers;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new NumberFactViewModel(connectionChecker, numbersRepo, schedulers);
  }
}
```

Now this is where the actual usage of Dagger will start. We declare a member variable for 
NumberFactViewModelFactory, but instead of instantiating it ourselves, we annotate it with 
`@Inject`. This tells Dagger that it should provide this dependency for us. In `onCreate()`, we do 
the actual injection of dependencies by calling `AndroidInjection.inject(this)`. Now our 
viewModelFactory has been instantiated. The last thing we'll change in the Activity is how our 
ViewModel is created. We'll want to pass in an instance of our ViewModelFactory so Android knows how
to instantiate it. 

```java
public class NumberFactActivity extends AppCompatActivity {

  @Inject NumberFactViewModelFactory viewModelFactory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidInjection.inject(this);

    NumberFactViewModel viewModel =
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(NumberFactViewModel.class);
  }
}
```

So how does Dagger know how to create our ViewModelFactory? We'll have to make a Module called 
NumberFactActivityModule. 

```java
@Module
public class NumberFactActivityModule {
  @Provides
  public NumberFactViewModelFactory provideNumberFactViewModelFactory(
      NumbersRepository repository,
      ConnectionChecker checker,
      AppSchedulers schedulers
  ) {
    return new NumberFactViewModelFactory(checker, repository, schedulers);
  }
}
```

In Dagger, a Module is a class that contributes to the object graph. We 
indicate that a class is a module by annotating it with `@Module`. Each method that provides an 
object in the graph should be annotated with `@Provides`. The name of the method doesn't really 
matter. By convention, we use `provideObjectToProvide()`. What's most important is the method's 
return type, as this is what Dagger uses to create the object graph. Now that we've created 
NumberFactActivityModule, we'll need to associate it with the right Activity. We'll do this by 
creating ActivityBindingModule, which will bind modules to their appropriate Activity.

```java
@Module
public abstract class ActivityBindingModule {
  @ContributesAndroidInjector(modules = { NumberFactActivityModule.class })
  abstract NumberFactActivity bindNumberFactActivity();
}
``` 

Here we're defining an abstract method bindNumberFactActivity() that returns NumberFactActivity. We 
associate it with the appropriate module by using the annotation `@ContributesAndroidInjector` and 
passing in the class of our module.

Now you may have been wondering about `NumberFactActivityModule`'s provide method. Where do the 
method parameters come from? They'll come from another module we'll create, `AppModule`. When 
constructing our object graph, we'll want to keep each module small and focused to increase 
modularity and maintain low coupling between dependencies. In `AppModule` we'll want to create 
objects that are relevant to the entire app.

Here's a snippet of `AppModule.java`:

```java
@Module
public class AppModule {
  @Provides
  @Singleton
  public NumbersApi provideNumbersApi(Retrofit retrofit) {
    return retrofit.create(NumbersApi.class);
  }
}
``` 

We're already familiar with the `@Module` and `@Provides` annotations. The `@Singleton` annotation 
specifies what's known as the scope for this dependency. You can think of the scope as the lifecycle
for a particular dependency. A scope of Singleton tells Dagger to only create one instance of 
this object, and to keep it around for the lifetime of the app.

Now that we've declared all of our dependencies, it's time to put everything together. We'll start 
by creating `AppComponent`. A Component in Dagger allows for a fully-formed, dependency-injected 
implementation to be generated from a set of modules. The generated class will have the name of the 
type annotated with `@Component` prepended with `Dagger`. For example, 
`@Component interface MyComponent {...}` will produce an implementation named `DaggerMyComponent`.

[AppComponent.java](src/main/java/com/orobator/helloandroid/lesson9/di/AppComponent.java)

```java
@Singleton
@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class,
    ActivityBindingModule.class
})
public interface AppComponent {
  void inject(NumberFactApplication app);

  @Component.Builder
  interface Builder {
    @BindsInstance 
    Builder application(NumberFactApplication application);

    AppComponent build();
  }
}
``` 

As you can see, `AppComponent` is annotated with `@Component` and the modules it depends on are 
passed into the annotation. `AndroidSupportInjectModule` allows us to use dagger.android classes 
like AndroidInjection.

Our component has a single method which injects our Application class. This is a way of letting 
Dagger know that it will have to provide dependencies to `NumberFactApplication`.

We also have an inner interface called Builder and annotated with `@Component.Builder`. This 
provides a builder for our component. The application method lets the builder know that our 
component requires a `NumberFactApplication`. The `@BindsInstance` annotation gives our underlying 
modules access to `NumberFactApplication` without having to specify a dependency in the constructor 
of the module.

The last thing we'll need to do to get Dagger working is to update our Application class and 
actually create the object graph.

```java
public class NumberFactApplication extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

  @Override public void onCreate() {
    super.onCreate();

    DaggerAppComponent
        .builder()
        .application(this)
        .build()
        .inject(this);
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingAndroidInjector;
  }
}
```

In `onCreate()`, we get an instance of AppComponent.Builder via the generated class 
`DaggerAppComponent`. In the builder, we pass in the application, build it, then inject our 
application. By injecting our application, we receive an instance of `DispatchingAndroidInjector`.

`DispatchingAndroidInjector` performs members-injection on instances of core Android types 
(e.g. Activity). 

Finally, we need to implement the `HasActivityInjector` interface. This will return the 
`DispatchingAndroidInjector` that gets injected in `onCreate()`. This is the object that performs 
injection when we call `AndroidInjection.inject(...)`.

## Dagger's generated code

Now we're using Dagger to inject dependencies into our Activities instead of creating them 
ourselves. Dagger might seem like a bit of magic right now, so let's take a look at the generated 
code to see what's going on. In `onCreate` of `NumberFactApplication` put your cursor on 
`DaggerAppComponent` and press Command + B to go to definition.

Below is a snippet of `DaggerAppComponent`. 

```java
// Generated by Dagger (https://google.github.io/dagger).
public final class DaggerAppComponent implements AppComponent {
  private final AppModule appModule;

  private Provider<OkHttpClient> provideOkHttpClientProvider;

  private DaggerAppComponent(AppModule appModuleParam, NumberFactApplication applicationParam) {
    this.appModule = appModuleParam;
    initialize(appModuleParam, applicationParam);
  }

  public static AppComponent.Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(
      final AppModule appModuleParam, final NumberFactApplication applicationParam) {
    this.provideOkHttpClientProvider =
        DoubleCheck.provider(AppModule_ProvideOkHttpClientFactory.create(appModuleParam));
  }

  @Override
  public void inject(NumberFactApplication app) {
    injectNumberFactApplication(app);
  }

  private NumberFactApplication injectNumberFactApplication(NumberFactApplication instance) {
    NumberFactApplication_MembersInjector.injectDispatchingAndroidInjector(
        instance, getDispatchingAndroidInjectorOfActivity());
    return instance;
  }
}
```

As we can see, our component has an instance of AppModule, which makes sense. It also has several 
Providers which provide the individual dependencies. If we dig into how a Provider is created, we'll
see that it eventually calls the appropriate method on AppModule to instantiate it. In the `inject` 
method of `DaggerAppComponent` we can see the dependency of dispatchingAndroidInjector being set.

## Dagger Lab

In this lab, we'll refactor our TipCalculator app from lesson 5 to use dependency injection.

First thing we'll want to do is to add the relevant dependencies to our build.gradle:

```groovy
dependencies {
  implementation 'com.google.dagger:dagger:2.x'
  annotationProcessor 'com.google.dagger:dagger-compiler:2.x'
  implementation 'com.google.dagger:dagger-android-support:2.x' // if you use the support libraries
  annotationProcessor 'com.google.dagger:dagger-android-processor:2.x'
}
```

Next, we'll refactor `TipCalcViewModel` to only have 1 constructor that takes in an `Application` 
and a `Calculator`. This is the basis for dependency injection. Instead of instantiating something 
ourselves, it will get created created by an external entity, then passed to us.

```java
public TipCalcViewModel(@NonNull Application app, Calculator calculator) {
  super(app);
  this.calculator = calculator;
  updateOutputs(new TipCalculation());
}
```

Now that we have additional arguments in the ViewModel's constructor, Android no longer knows how to
construct a `TipCalcViewModel` for us. Where will it get the `Calculator` instance from? To solve 
this problem, we'll create a `ViewModelProvider.Factory` that tells Android how to create an 
instance of `TipCalcViewModel`. Android will use this factory whenever it needs to create a new 
instance. Implement `TipCalcViewModelFactory` like so:

```java
package com.orobator.helloandroid.lesson09_lab.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.orobator.helloandroid.tipcalc.model.Calculator;

public class TipCalcViewModelFactory implements ViewModelProvider.Factory {
  private final Application app;
  private final Calculator calculator;

  public TipCalcViewModelFactory(Application app, Calculator calculator) {
    this.app = app;
    this.calculator = calculator;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new TipCalcViewModel(app, calculator);
  }
}
```

In our `TipCalcActivity` we'll have to tell the `ViewModelsProviders` to use our factory when 
creating an instance of `TipCalcViewModel`. For now, we'll instantiate an instance of our factory
directly in the Activity, but after that, we'll use Dagger to inject our Factory into our Activity.

```java
TipCalcViewModelFactory factory =
  new TipCalcViewModelFactory(getApplication(), new Calculator());
TipCalcViewModel vm = ViewModelProviders.of(this, factory).get(TipCalcViewModel.class);
```

Now that we've refactored our ViewModel to have its dependencies injected, we'll start to use Dagger
to inject all the dependencies into our Activity. The first thing we'll do is create a `Module`. A 
`Module` in Dagger is responsible for actually creating the dependencies. Create `TipCalcModule` 
using the following implementation:

```java
package com.orobator.helloandroid.lesson09_lab.di;

import android.app.Application;
import com.orobator.helloandroid.lesson09_lab.viewmodel.TipCalcViewModelFactory;
import com.orobator.helloandroid.tipcalc.model.Calculator;
import dagger.Module;
import dagger.Provides;

@Module
public class TipCalcModule {
  private final Application app;

  public TipCalcModule(Application application) {
    this.app = application;
  }
  
  @Provides
  public Calculator provideCalculator() {
    return new Calculator();
  }

  @Provides
  public TipCalcViewModelFactory provideTipCalcViewModelFactory(Calculator calc) {
    return new TipCalcViewModelFactory(app, calc);
  }
}
```

After we've created the `Module`, we'll need to create a `Component`. A `Component` in Dagger will 
do the actual injection. It will pass the required dependencies to `TipCalcActivity` after 
`TipCalcModule` creates the dependencies. Create `TipCalcComponent` using the following 
implementation:

```java
package com.orobator.helloandroid.lesson09_lab.di;

import com.orobator.helloandroid.lesson09_lab.view.TipCalcActivity;
import dagger.Component;

@Component(modules = TipCalcModule.class)
public interface TipCalcComponent {
  void inject(TipCalcActivity target);
  
  @Component.Builder
  interface Builder {
    Builder tipCalcModule(TipCalcModule module);
    TipCalcComponent build();
  }
}
```

When we build our application, Dagger will inspect our code for any Dagger annotations 
(`@Component`, `@Module`, etc). For all components that it sees, it will generate a real 
implementation that we can use. The name of the generated `Component` will be 
`Dagger<YourComponentName>`. For this specific lab, `DaggerTipCalcComponent` will be generated. Go 
ahead and build the app (using Command+Shift+A) in order to generate an implementation of 
`TipCalcComponent`.


We now have the entity that will provide dependencies, as well as the entity that will inject the 
dependencies, so we're ready to wire everything together. However, there's one question we need to 
answer: Where will we store our component? For more complicated object graphs, it could be very 
expensive to initialize, so we don't want to keep it in an Activity. Storing it in an Activity, 
would mean that the object graph would get recreated every time we rotate the device, and we don't 
want that. Instead, we'll store our object graph (a.k.a. Component) in our Application class which 
will be around for the entire lifecycle of our app. To do this, we'll need to create a custom 
Application class, instead of using the default implementation that Android provides. Create a new 
class called `TipCalcApplication`.

```java
package com.orobator.helloandroid.lesson09_lab;

import android.app.Application;

public class TipCalcApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    
  }
}
```

In order for Android to recognize that we want to use this custom implementation, we have to declare
it in the AndroidManifest.xml.

```xml
<application
    android:name=".TipCalcApplication">
  ...
</application>
```

Back in TipCalcApplication, we'll create our object graph and store it in an instance variable.
We'll also write a method to retrieve this component.

```java
package com.orobator.helloandroid.lesson09_lab;

import android.app.Application;
import com.orobator.helloandroid.lesson09_lab.di.DaggerTipCalcComponent;
import com.orobator.helloandroid.lesson09_lab.di.TipCalcComponent;
import com.orobator.helloandroid.lesson09_lab.di.TipCalcModule;

public class TipCalcApplication extends Application {
  private TipCalcComponent component;

  @Override public void onCreate() {
    super.onCreate();
    
    component = DaggerTipCalcComponent
        .builder()
        .tipCalcModule(new TipCalcModule(this))
        .build();
  }

  public TipCalcComponent getComponent() {
    return component;
  }
}
```

Now we're ready to inject our TipCalcActivity! The first thing we'll want to do is convert our 
ViewModel Factory to an instance field, and annotate it with `@Inject` so Dagger knows this field 
will be injected. We'll want to remove the initialization of the factory in onCreate as well, as it
will be created and injected by Dagger.

```java
public class TipCalcActivity extends AppCompatActivity {
  @Inject TipCalcViewModelFactory factory;
}
```

When we finish refactoring our factory to an instance field, we'll grab the component from our 
application and inject our dependencies into our activity.

```java
@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    ((TipCalcApplication) getApplication())
        .getComponent()
        .inject(this);
  }
```

After calling inject(), we now have an instance of `TipCalcViewModelFactory` to use.

Technically, we have now implemented dependency injection with Dagger, but we can still clean this 
code up using some more features of Dagger.