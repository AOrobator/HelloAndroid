package com.orobator.helloandroid.lesson5

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.orobator.helloandroid.lesson5.view.TipCalcActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Testing with Kotlin syntactic sugar
@RunWith(AndroidJUnit4ClassRunner::class)
class TipCalcActivityRobotTest {
  // When creating UI tests, it's helpful to have the XML layout in split view

  // The test rule will automatically start our Activity for us
  // The get applies @Rule annotation to property getter
  @get:Rule
  var activityTestRule = ActivityTestRule(TipCalcActivity::class.java)

  @Test
  fun whenValidInputsAreReceived_TipIsCalculated() {
    // Now test is focused on the what instead of the how

    onTipCalculator {
      // Setup View
      enterCheckAmount("10.00")
      enterTipPercent("25")
      calculateTip()

      // Assert Inputs Cleared
      hasInputs(
          checkAmount = "0.00",
          tipAmount = "0"
      )

      // Assert Outputs
      hasOutputs(
          checkAmount = "$10.00",
          tipAmount = "$2.50",
          grandTotal = "$12.50"
      )
    }
  }

  @Test
  fun whenInvalidTipAmountEntered_TipIsNotCalculated() {
    onTipCalculator {
      enterCheckAmount("10.00")
      enterTipPercent("1000")
      calculateTip()

      // Assert Inputs Cleared
      hasInputs(
          checkAmount = "0.00",
          tipAmount = "0"
      )

      // Assert Outputs
      hasOutputs(
          checkAmount = "$0.00",
          tipAmount = "$0.00",
          grandTotal = "$0.00"
      )
    }
  }
}
