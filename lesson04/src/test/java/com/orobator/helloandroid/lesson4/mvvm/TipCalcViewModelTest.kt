package com.orobator.helloandroid.lesson4.mvvm

import android.app.Application
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.orobator.helloandroid.lesson4.R
import com.orobator.helloandroid.lesson4.mvvm.viewmodel.TipCalcViewModel
import com.orobator.helloandroid.tipcalc.model.Calculator
import com.orobator.helloandroid.tipcalc.model.TipCalculation
import org.amshove.kluent.`should equal`
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TipCalcViewModelTest {
  lateinit var viewModel: TipCalcViewModel

  @Mock
  lateinit var mockCalculator: Calculator

  @Mock
  lateinit var app: Application

  @Before
  fun setup() {
    // Initializes all mocks with the @Mock annotation
    MockitoAnnotations.initMocks(this)

    // Resource needed for all tests
    stubResource(0.0, "$0.00")

    // Initialize viewModel before every test
    viewModel = TipCalcViewModel(app, mockCalculator)
  }

  private fun stubResource(
    given: Double,
    returnStub: String
  ) {
    whenever(app.getString(R.string.money_template, given)).thenReturn(returnStub)
  }

  @Test
  fun testCalculateTip() {
    // Assert state of the view, vs interactions with the view
    viewModel.inputCheckAmount = "10.00"
    viewModel.inputTipPercent = "15"

    val stub = TipCalculation(15, 10.00, 1.50, 11.50)
    whenever(mockCalculator.calculateTip(10.00, 15)).thenReturn(stub)

    // Verifies that we are calling Application#getString
    // Verifies we are giving the expected inputs to getString
    stubResource(10.0, "$10.00")
    stubResource(1.5, "$1.50")
    stubResource(11.5, "$11.50")

    viewModel.calculateTip()

    // Verify outputs
    viewModel.outputCheckAmount `should equal` "$10.00"
    viewModel.outputTipAmount `should equal` "$1.50"
    viewModel.outputGrandTotal `should equal` "$11.50"

    // Verify inputs reset
    viewModel.inputTipPercent `should equal` "0"
    viewModel.inputCheckAmount `should equal` "0.00"
  }

  @Test
  fun testCalculateBadTip() {
    viewModel.inputCheckAmount = "10.00"
    viewModel.inputTipPercent = ""

    viewModel.calculateTip()

    verify(mockCalculator, never()).calculateTip(anyDouble(), anyInt())
  }
}