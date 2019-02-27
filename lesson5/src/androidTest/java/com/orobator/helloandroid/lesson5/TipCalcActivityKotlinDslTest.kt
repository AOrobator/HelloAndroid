package com.orobator.helloandroid.lesson5

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.orobator.helloandroid.lesson5.view.TipCalcActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Testing with Kotlin syntactic sugar
@RunWith(AndroidJUnit4ClassRunner::class)
class TipCalcActivityKotlinDslTest {
  // When creating UI tests, it's helpful to have the XML layout in split view

  // The test rule will automatically start our Activity for us
  // The get applies @Rule annotation to property getter
  @get:Rule
  var activityTestRule = ActivityTestRule(TipCalcActivity::class.java)

  @Test
  fun whenValidInputsAreReceived_TipIsCalculated() {
    // Calculate Tip
    // Matching to id will always find view regardless of language vs withHint() or withText()
    R.id.check_amount_EditText typeText "10.00"

    R.id.tip_percent_EditText typeText "25"

    R.id.calculate_fab.click()

    // Assert Inputs Cleared
    R.id.check_amount_EditText hasText "0.00"

    R.id.tip_percent_EditText hasText "0"

    // Assert Outputs
    R.id.check_amount_TextView hasText "$10.00"

    R.id.tip_amount_TextView hasText "$2.50"

    R.id.grand_total_TextView hasText "$12.50"
  }
}