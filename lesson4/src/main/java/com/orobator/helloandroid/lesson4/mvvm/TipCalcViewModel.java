package com.orobator.helloandroid.lesson4.mvvm;

import androidx.lifecycle.ViewModel;
import com.orobator.helloandroid.lesson4.mvc.model.Calculator;

public class TipCalcViewModel extends ViewModel {
  private final Calculator calculator;

  public TipCalcViewModel() {
    calculator = new Calculator();
  }

  public void calculateTip() {

  }
}
