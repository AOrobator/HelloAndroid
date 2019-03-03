package com.orobator.helloandroid.stackoverflow;

public class ApiConstants {
  public static final String STACK_OVERFLOW_SITE = "stackoverflow";

  public enum Order {
    ASC("asc"),
    DESC("desc");

    public final String val;

    Order(String val) {
      this.val = val;
    }
  }

  public enum Sort {
    ACTIVITY("activity"),
    VOTES("votes"),
    CREATION("creation"),
    HOT("hot"),
    WEEK("week"),
    MONTH("month");

    public final String val;

    Sort(String val) {
      this.val = val;
    }
  }

  private ApiConstants() {
    // No Instances
  }
}
