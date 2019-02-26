package com.orobator.helloandroid.lesson5

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.orobator.helloandroid.lesson5.view.TipCalcActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class TipCalcActivityTest {
  // When creating UI tests, it's helpful to have the XML layout in split view

  // The test rule will automatically start our Activity for us
  // The get applies @Rule annotation to property getter
  @get:Rule
  var activityTestRule = ActivityTestRule(TipCalcActivity::class.java)

  @Test
  fun testTipCalculator() {
    // Calculate Tip
    // Matching to id will always find view regardless of language vs withHint() or withText()
    onView(withId(R.id.check_amount_EditText))
        .perform(replaceText("10.00"))

    onView(withId(R.id.tip_percent_EditText))
        .perform(replaceText("25"))

    onView(withId(R.id.calculate_fab))
        .perform(click())

    // Assert Inputs Cleared
    onView(withId(R.id.check_amount_EditText))
        .check(matches(withText("0.00")))

    onView(withId(R.id.tip_percent_EditText))
        .check(matches(withText("0")))

    // Assert Outputs
    onView(withId(R.id.check_amount_TextView))
        .check(matches(withText("$10.00")))

    onView(withId(R.id.tip_amount_TextView))
        .check(matches(withText("$2.50")))

    onView(withId(R.id.grand_total_TextView))
        .check(matches(withText("$12.50")))
  }
}