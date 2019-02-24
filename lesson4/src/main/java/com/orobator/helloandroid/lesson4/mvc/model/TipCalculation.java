package com.orobator.helloandroid.lesson4.mvc.model;

import java.util.Objects;

public class TipCalculation {
  public final int tipPercent;
  public final double checkAmount;
  public final double tipAmount;
  public final double grandTotal;

  public TipCalculation(int tipPercent, double checkAmount, double tipAmount, double grandTotal) {
    this.tipPercent = tipPercent;
    this.checkAmount = checkAmount;
    this.tipAmount = tipAmount;
    this.grandTotal = grandTotal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TipCalculation that = (TipCalculation) o;
    return tipPercent == that.tipPercent &&
        Double.compare(that.checkAmount, checkAmount) == 0 &&
        Double.compare(that.tipAmount, tipAmount) == 0 &&
        Double.compare(that.grandTotal, grandTotal) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipPercent, checkAmount, tipAmount, grandTotal);
  }
}
