package com.orobator.helloandroid.lesson09_lab.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.orobator.helloandroid.lesson09_lab.BR;
import com.orobator.helloandroid.lesson09_lab.R;
import com.orobator.helloandroid.observableviewmodel.AndroidObservableViewModel;
import com.orobator.helloandroid.tipcalc.model.Calculator;
import com.orobator.helloandroid.tipcalc.model.TipCalculation;


public class TipCalcViewModel extends AndroidObservableViewModel {
  private final Calculator calculator;

  // Use strings instead of numbers because View expects strings
  public String inputCheckAmount = "";

  // Could use ObservableField, but has potential for clutter
  public String inputTipPercent = "";

  public String outputCheckAmount = "";
  public String outputTipAmount = "";
  public String outputGrandTotal = "";

  public TipCalcViewModel(@NonNull Application app, Calculator calculator) {
    super(app);
    this.calculator = calculator;
    updateOutputs(new TipCalculation());
  }

  private void updateOutputs(TipCalculation calculation) {
    outputCheckAmount =
        getApplication().getString(R.string.money_template, calculation.checkAmount);
    outputTipAmount = getApplication().getString(R.string.money_template, calculation.tipAmount);
    outputGrandTotal = getApplication().getString(R.string.money_template, calculation.grandTotal);
  }

  public void calculateTip() {
    Double checkAmount = toDoubleOrNull(inputCheckAmount);
    Integer tipPct = toIntOrNull(inputTipPercent);

    if (checkAmount != null && tipPct != null && tipPct < 100) {
      TipCalculation calculation = calculator.calculateTip(checkAmount, tipPct);
      updateOutputs(calculation);
    } else {
      updateOutputs(new TipCalculation());
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
