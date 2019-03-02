package com.orobator.helloandroid.lesson4.mvp.view;

public interface TipCalcPresentation {
  String getCheckAmount();

  String getTipPercent();

  void showError(String errMsg);

  void setCheckAmount(double checkAmount);

  void setTipAmount(double tipAmount);

  void setGrandTotal(double grandTotal);
}
