package com.orobator.helloandroid.lesson5

import androidx.test.rule.ActivityTestRule
import com.orobator.helloandroid.lesson5.view.TipCalcActivity
import org.junit.Rule

class TipCalcActivityTest {

  // The test rule will automatically start our Activity for us
  @get:Rule
  var activityTestRule = ActivityTestRule(TipCalcActivity::class.java)
}