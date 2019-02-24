package com.orobator.helloandroid.lesson4.mvp;

import com.orobator.helloandroid.lesson4.mvc.model.Calculator;
import com.orobator.helloandroid.lesson4.mvc.model.TipCalculation;

public class TipCalcPresenter {
  private TipCalcPresentation presentation;
  private final Calculator calculator = new Calculator();

  public void attach(TipCalcPresentation presentation) {
    this.presentation = presentation;
  }

  public void detach() {
    presentation = null;
  }

  public void calculateTip() {
    if (presentation != null) {
      String checkAmountString = presentation.getCheckAmount();
      String tipPercentString = presentation.getTipPercent();

      double checkAmount = Double.parseDouble(checkAmountString);
      int tipPercent = Integer.parseInt(tipPercentString);

      if (tipPercent > 100) {
        presentation.showError("Tip can't be greater than 100%");
      } else {
        TipCalculation c = calculator.calculateTip(checkAmount, tipPercent);

        presentation.setCheckAmount(c.checkAmount);
        presentation.setTipPercent(c.tipPercent);
        presentation.setTipAmount(c.tipAmount);
        presentation.setGrandTotal(c.grandTotal);
      }
    }
  }

  public interface TipCalcPresentation {
    String getCheckAmount();

    String getTipPercent();

    void showError(String errMsg);

    void setCheckAmount(double checkAmount);

    void setTipPercent(int tipPercent);

    void setTipAmount(double tipAmount);

    void setGrandTotal(double grandTotal);
  }
}
