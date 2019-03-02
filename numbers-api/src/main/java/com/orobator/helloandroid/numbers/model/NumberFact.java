package com.orobator.helloandroid.numbers.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class NumberFact {
  @SerializedName("text")
  public final String text;

  @SerializedName("number")
  public final int number;

  @SerializedName("found")
  public final boolean found;

  @SerializedName("type")
  public final String type;

  public NumberFact(String text, int number, boolean found, String type) {
    this.text = text;
    this.number = number;
    this.found = found;
    this.type = type;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NumberFact that = (NumberFact) o;
    return number == that.number &&
        found == that.found &&
        Objects.equals(text, that.text) &&
        Objects.equals(type, that.type);
  }

  @Override public int hashCode() {
    return Objects.hash(text, number, found, type);
  }
}
