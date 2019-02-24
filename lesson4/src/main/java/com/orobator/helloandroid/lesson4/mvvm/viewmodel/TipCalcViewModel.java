package com.orobator.helloandroid.lesson4.mvvm.viewmodel;

import android.app.Application;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import com.orobator.helloandroid.lesson4.BR;
import com.orobator.helloandroid.lesson4.R;
import com.orobator.helloandroid.lesson4.mvc.model.Calculator;
import com.orobator.helloandroid.lesson4.mvc.model.TipCalculation;

public class TipCalcViewModel extends BaseObservable {
  private final Calculator calculator;
  private final Application app;

  // Use strings instead of numbers because View expects strings
  public String inputCheckAmount = "";

  // Could use ObservableField, but has potential for clutter
  public String inputTipPercent = "";

  public String outputCheckAmount = "";
  public String outputTipAmount = "";
  public String outputGrandTotal = "";

  public TipCalcViewModel(Application app, Calculator calculator) {
    this.app = app;
    this.calculator = calculator;
    updateOutputs(new TipCalculation());
  }

  private void updateOutputs(TipCalculation calculation) {
    outputCheckAmount = app.getString(R.string.money_template, calculation.checkAmount);
    outputTipAmount = app.getString(R.string.money_template, calculation.tipAmount);
    outputGrandTotal = app.getString(R.string.money_template, calculation.grandTotal);
  }

  public void calculateTip() {
    Double checkAmount = toDoubleOrNull(inputCheckAmount);
    Integer tipPct = toIntOrNull(inputTipPercent);

    if (checkAmount != null && tipPct != null) {
      TipCalculation calculation = calculator.calculateTip(checkAmount, tipPct);
      updateOutputs(calculation);
    }

    clearInputs();
  }

  private void clearInputs() {
    inputCheckAmount = "0.00";
    inputTipPercent = "0";

    // Notify that all properties have changed
    // so that the view is rebound to the ViewModel.

    // This way view only processes 1 notification
    notifyPropertyChanged(BR._all);
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
