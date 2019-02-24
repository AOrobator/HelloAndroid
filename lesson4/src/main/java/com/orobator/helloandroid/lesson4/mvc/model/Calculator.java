package com.orobator.helloandroid.lesson4.mvc.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {
  public TipCalculation calculateTip(double checkAmount, int tipPercent) {
    double tipAmount = new BigDecimal((checkAmount * 100) / tipPercent)
        .setScale(2, RoundingMode.HALF_UP)
        .doubleValue();

    double grandTotal = checkAmount + tipAmount;

    return new TipCalculation(tipPercent, checkAmount, tipAmount, grandTotal);
  }
}
