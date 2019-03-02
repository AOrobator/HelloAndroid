package com.orobator.helloandroid.lesson7.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.orobator.helloandroid.lesson7.model.AppSchedulers
import com.orobator.helloandroid.lesson7.model.api.NumberFact
import com.orobator.helloandroid.lesson7.model.api.NumbersRepository
import com.orobator.helloandroid.lesson7.model.connectivity.ConnectionChecker
import com.orobator.helloandroid.lesson7.model.connectivity.NetworkState.DISCONNECTED
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.amshove.kluent.`should equal`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class NumberFactViewModelUnitTest {

  @get:Rule
  var rule: TestRule = InstantTaskExecutorRule()

  @Test
  fun `When disconnected and random fact clicked, user alerted of no connectivity`() {
    val viewModel = OkHttpNumberFactViewModel()
    viewModel.networkStateLiveData.value `should equal` null
    val connectionChecker = mock<ConnectionChecker> {
      on { isConnected } doReturn false
    }

    viewModel.init(connectionChecker)
    viewModel.getRandomFact()

    viewModel.networkStateLiveData.value `should equal` ViewEvent(DISCONNECTED)
    verify(connectionChecker).isConnected
  }

  @Test
  fun `When connected and random fact clicked, random fact is updated`() {
    val viewModel = RetrofitNumberFactViewModel()

    val connectionChecker = mock<ConnectionChecker> {
      on { isConnected } doReturn true
    }

    val numbersRepo: NumbersRepository = mock {
      on { getTriviaFact(2) } doReturn Single.fromCallable {
        (NumberFact(
            "Test Fact", 2, true, "Trivia"
        ))
      }
    }

    val testScheduler = TestScheduler()
    val schedulers = AppSchedulers(testScheduler, testScheduler)

    viewModel.init(
        connectionChecker,
        numbersRepo,
        schedulers
    )

    viewModel.inputNumber = "2"
    viewModel.getRandomFact()
    testScheduler.triggerActions()

    verify(numbersRepo).getTriviaFact(2)
    viewModel.outputFact `should equal` "Test Fact"
  }
}