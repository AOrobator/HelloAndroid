# Lesson 6: Instrumentation Testing

Before we get into the how of instrumentation tests a.k.a UI tests, we need to understand the test
pyramid and when to use UI tests.

![Test Pyramid][test-pyramid]

The test pyramid consists of three layers: Unit Tests, Integration Tests, and End to End Tests.
At the bottom of the pyramid we have our Unit Tests. These you should already be familiar with from
the lesson on architecture. Unit tests test all of your business and UI logic. Good unit tests are:

 * Thorough
 * Focused
 * Fast
 * Repeatable
 * Verifies Behavior
 * Concise
 
Unit tests are cheap and fast to run. It makes the most sense to have your test suite be composed
of unit tests to enable fast feedback.

Next up we have integration tests, a type of UI test. As we can see, integration tests are higher
up on the pyramid and smaller. Because they're higher up in the pyramid, it means that these tests
are more expensive in terms of execution time, maintenance, and debugging. Therefore, we should
have less of them. The benefit of being higher up in the pyramid is that integration and end to end
tests have more fidelity, because they are closer to what a real user experiences when using the app.

An integration test might consist of UI tests around a single screen. An end to end test would walk
through a particular flow in your app, traversing several screens, and making network requests to
**mock** web servers. Notice the emphasis on mock. You don't want to be hitting real servers in
your tests for a couple reasons:

  1. It'll slow down your tests. IO is slow and internet connections are unreliable.
  2. It'll make your tests flaky. A flaky test is one that can run multiple times without code
     changes and have different results from run to run. By hitting actual servers, your test
     results will be at the mercy of the backend. If it were to go down, your tests would as well,
     through no fault of their own. We want to aim for **hermetic** and **deterministic** tests
     that can run in complete isolation.
     
In general, it's suggested that you stick to a balance of 70% unit tests, 20% integration tests and
10% end to end tests.

Now that we've covered the when, let's talk about the how.

## Espresso Tests

[test-pyramid]: test_pyramid.png "test-pyramid"