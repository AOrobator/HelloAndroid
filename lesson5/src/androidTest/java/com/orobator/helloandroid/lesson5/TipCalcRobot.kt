package com.orobator.helloandroid.lesson5

import androidx.test.espresso.ViewInteraction
import com.orobator.helloandroid.lesson5.R.id

fun onTipCalculator(func: TipCalcRobot.() -> Unit): TipCalcRobot = TipCalcRobot().apply { func() }

class TipCalcRobot {
  fun enterCheckAmount(checkAmount: String) {
    id.check_amount_EditText typeText checkAmount
  }

  fun enterTipPercent(tipAmount: String) {
    id.tip_percent_EditText typeText tipAmount
  }

  fun calculateTip(): ViewInteraction = id.calculate_fab.click()

  fun hasInputs(
    checkAmount: String,
    tipAmount: String
  ) {
    id.check_amount_EditText hasText checkAmount
    id.tip_percent_EditText hasText tipAmount
  }

  fun hasOutputs(
    checkAmount: String,
    tipAmount: String,
    grandTotal: String
  ) {
    id.check_amount_TextView hasText checkAmount

    id.tip_amount_TextView hasText tipAmount

    id.grand_total_TextView hasText grandTotal
  }
}