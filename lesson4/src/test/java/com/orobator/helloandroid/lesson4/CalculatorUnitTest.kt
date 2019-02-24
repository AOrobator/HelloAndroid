package com.orobator.helloandroid.lesson4

import com.orobator.helloandroid.lesson4.mvc.model.Calculator
import com.orobator.helloandroid.lesson4.mvc.model.TipCalculation
import org.amshove.kluent.`should equal`
import org.junit.Test

class CalculatorUnitTest {

  @Test
  fun `Tip calculation is correct`() {
    val calc = Calculator()

    calc.calculateTip(100.00, 20) `should equal` TipCalculation(
        20,
        100.00,
        20.00,
        120.00
    )
  }
}