# Lesson 15 Lab: Testing WorkManager

For this lab, you'll create a `Worker` and test it. First create a new project and add the 
`work-runtime` and `work-testing` dependencies, as well as `kluentAndroid`. Since you'll be writing 
tests in Kotlin, make sure to apply the `kotlin-android` plugin at the top of your application level
build.gradle.

```groovy
apply plugin: 'kotlin-android'

dependencies {
  def work_version = "2.0.0"
  
  implementation "androidx.work:work-runtime:$work_version"
  
  androidTestImplementation "androidx.work:work-testing:$work_version"
  androidTestImplementation "org.amshove.kluent:kluent-android:1.49"
}
```

In your main source directory, create a class `FibonacciWorker` that extends from the `Worker` 
class.

It should have the following methods:

```java
/**
* Returns a Data object with a key of KEY_NTH_FIB and a value of n
*/
public static Data makeInputData(int n);

/**
* Computes the nth Fibonacci number
*/
private int nthFib(int n);

/**
* Returns a Result.success(Data) where the outputData contains the 
* result of computing the nth Fibonacci number. Use the key KEY_NTH_FIB
* 
* Returns Result.failure() if n < 0
*/
@NonNull @Override public Result doWork();
```

Once you have created `FibonacciWorker`, write an Android test for it in Kotlin. In your setup 
method, make sure to configure `WorkManager` for testing. 

Write the following tests:

```kotlin
/**
* Pass an input of 3 and verify that the Worker succeeded and the output
* value is 2. When building the WorkRequest, make sure that this work 
* only happens when the battery is not low and the device is idle. Let's
* pretend that computing the nth Fibonacci number is very 
* computationally expensive.
*/
@Test
fun whenPositiveNumberGiven_WorkerComputesResult() {}

/**
* Pass an input of -1 and verify that the Worker returns a failed 
* result. The constraints should be the same as the previous test, 
* because computing the nth Fibonacci number is computationally 
* expensive.
*/
@Test
fun whenNegativeNumberGiven_WorkerReturnsFailure() {}
```