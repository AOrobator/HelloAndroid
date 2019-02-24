package com.orobator.helloandroid.lesson4.mvvm;

import androidx.annotation.Nullable;
import com.orobator.helloandroid.lesson4.mvc.model.Calculator;
import com.orobator.helloandroid.lesson4.mvc.model.TipCalculation;

public class TipCalcViewModel {
  private final Calculator calculator;

  // Use strings instead of numbers because View expects strings
  public String inputCheckAmount = "";
  public String inputTipPercent = "";
  public TipCalculation calculation = new TipCalculation(0, 0.0, 0.0, 0.0);

  public TipCalcViewModel() {
    calculator = new Calculator();
  }

  public TipCalcViewModel(Calculator calculator) {
    this.calculator = calculator;
  }

  public void calculateTip() {
    Double checkAmount = toDoubleOrNull(inputCheckAmount);
    Integer tipPct = toIntOrNull(inputTipPercent);

    if (checkAmount != null && tipPct != null) {
      calculation = calculator.calculateTip(checkAmount, tipPct);
    }
  }

  private @Nullable Double toDoubleOrNull(String s) {
    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private @Nullable Integer toIntOrNull(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
