# Lesson 9 Lab: Dependency Injection with Dagger

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

### Code Cleanup Part 1: @BindsInstance

Technically, we have now implemented dependency injection with Dagger, but we can still clean this 
code up using some more features of Dagger.

The first way we'll clean up our code is by eliminating the constructor of `TipCalcModule`. It'll be
a bit cleaner if Dagger provides the `Application` dependency for us instead of passing it through 
the constructor. After eliminating the constructor, we'll have to update our 
provideTipCalcViewModelFactory method to take an `Application` as a method parameter.

```java
@Module
public class TipCalcModule {
  @Provides
  public TipCalcViewModelFactory provideTipCalcViewModelFactory(Application app, Calculator calc) {
    return new TipCalcViewModelFactory(app, calc);
  }
}
```

Next, we'll give `TipCalcComponent` an instance of `Application` to use by adding a method to our 
Builder and annotating it with `@BindsInstance` so it is included in our object graph. Since the 
`TipCalcModule` no longer has a constructor dependency, we can eliminate it from our Builder as 
Dagger will know how to construct it.

```java
public interface TipCalcComponent {
  interface Builder {
    
    @BindsInstance
    Builder application(Application app);
    
  }
}
```

Then we'll modify the component initialization in `onCreate` of `TipCalcApplication` to use this 
new builder method.

```java
component = DaggerTipCalcComponent
    .builder()
    .application(this)
    .build();
```

This way, all new modules will now have access to an `Application` object without explicitly 
requiring one in the constructor.

### Code Cleanup Part 2: AndroidInjection

Now for the second way we'll clean up this code. If you look at `TipCalcActivity` you'll find that
it's very tightly coupled to the implementation of its injection. It knows exactly who is injecting 
it (the Application class) and how it is done (via the `TipCalcComponent`).

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  
  ((TipCalcApplication) getApplication())
      .getComponent()
      .inject(this);
}
```

Such tight coupling would prevent us from using an alternate injection method via tests and 
decreases modularity as well. With Dagger's `AndroidInjection` we can abstract away how Activities 
are injected. We will essentially inject an injector using `AndroidInjection`.

![yo dawg]

To get started with `AndroidInjection`, we'll create another `Module` that specifies how 
`TipCalcActivity` will get injected. Create a new module that's an abstract class, name it 
`ActivityBindingModule`, and use the following implementation:

```java
@Module
public abstract class ActivityBindingModule {
  @ContributesAndroidInjector(modules = { TipCalcModule.class })
  abstract TipCalcActivity bindTipCalcActivity();
}
``` 

The `@ContributesAndroidInjector` will specify what modules are needed to inject a particular 
Activity. All the methods are abstract because the concrete implementation will be created by 
Dagger. The method name isn't important, just the return type to specify the Activity. However, for 
readability, it's recommended to name the methods as `bind<ThingYouAreBinding>()`.

Next we'll modify `TipCalcComponent` to include this `Module` as well as the 
`AndroidSupportInjectionModule` which will provide injectors for all Activities. We'll also inject 
our Application instead of our Activity. You'll see why soon enough.  

```java
@Component(modules = {
    TipCalcModule.class,
    ActivityBindingModule.class,
    AndroidSupportInjectionModule.class
})
public interface TipCalcComponent {
  void inject(TipCalcApplication target);
}
```

Now we'll have to update the TipCalcApplication class so it doesn't leak implementation details 
about how it achieves dependency injection. First we'll eliminate the component field and getter and
add the injected field `@Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;`. 
As its name suggests, this Dagger provided dependency will inject our Activities. Next we'll build 
our `TipCalcComponent` and immediately inject ourselves so we can get an instance of a 
`DispatchingAndroidInjector`. After that, we need our Application to implement the 
`HasActivityInjector` interface, and when we implement the single method `activityInjector()`, we'll
return the `DispatchingAndroidInjector` that was injected.  

```java
public class TipCalcApplication extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

  @Override public void onCreate() {
    super.onCreate();

    DaggerTipCalcComponent
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

Finally in `TipCalcActivity`, instead of referencing our Application class directly, we can simply 
call `AndroidInjection.inject(this)`

```java
public class TipCalcActivity extends AppCompatActivity {

  @Inject TipCalcViewModelFactory factory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    AndroidInjection.inject(this);
    
    // Use Dependencies
  }
}
```

[yo dawg]: yo_dawg.png "Yo Dawg, I heard you like dependency injection"