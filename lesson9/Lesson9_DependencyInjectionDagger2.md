# Lesson 9: Dependency Injection / Dagger 2

## What is a dependency?
 
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

## Potential Dependency Problems