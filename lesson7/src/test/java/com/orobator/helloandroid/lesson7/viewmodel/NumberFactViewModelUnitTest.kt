package com.orobator.helloandroid.lesson7.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.orobator.helloandroid.lesson7.model.ConnectionChecker
import com.orobator.helloandroid.lesson7.viewmodel.NumberFactViewModel.NetworkState.DISCONNECTED
import org.amshove.kluent.`should equal`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

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