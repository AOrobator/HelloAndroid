package com.orobator.mockwebserverassertions

import io.reactivex.observers.TestObserver
import java.util.concurrent.TimeUnit

infix fun <T> TestObserver<T>.`has value`(value: T?) {
  assertValuesOnly(value)
}

infix fun <T> TestObserver<in T>.`completed with single value`(value: T?) {
  awaitTerminalEvent(1, TimeUnit.SECONDS)
  assertComplete()
  assertNoErrors()
  assertValueCount(1)
  assertValue(value)
}

fun <T> TestObserver<T>.`has completed`() {
  awaitTerminalEvent(1, TimeUnit.SECONDS)
  assertComplete()
  assertNoErrors()
  assertNoValues()
}