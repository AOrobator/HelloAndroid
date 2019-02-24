package com.orobator.helloandroid.lesson4.mvvm

import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.orobator.helloandroid.lesson4.mvc.model.Calculator
import com.orobator.helloandroid.lesson4.mvc.model.TipCalculation
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

  @Before
  fun setup() {
    // Initializes all mocks with the @Mock annotation
    MockitoAnnotations.initMocks(this)

    // Initialize viewModel before every test
    viewModel = TipCalcViewModel(mockCalculator)
  }

  @Test
  fun testCalculateTip() {
    // Assert state of the view, vs interactions with the view
    viewModel.inputCheckAmount = "10.00"
    viewModel.inputTipPercent = "15"

    val stub = TipCalculation(15, 10.00, 1.50, 11.50)
    whenever(mockCalculator.calculateTip(10.00, 15)).thenReturn(stub)

    viewModel.calculateTip()

    viewModel.calculation `should equal` stub
  }

  @Test
  fun testCalculateBadTip() {
    viewModel.inputCheckAmount = "10.00"
    viewModel.inputTipPercent = ""

    viewModel.calculateTip()

    verify(mockCalculator, never()).calculateTip(anyDouble(), anyInt())
  }
}