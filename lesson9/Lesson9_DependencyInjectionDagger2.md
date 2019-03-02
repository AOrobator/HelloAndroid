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