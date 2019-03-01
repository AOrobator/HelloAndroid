package com.orobator.helloandroid.lesson8;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class LeakyInnerClassActivity extends AppCompatActivity {

  /*
   * Mistake Number 1:
   * Never create a static variable of an inner class
   * Fix 1:
   * private LeakyClass leakyClass;
   */
  private static LeakyClass leakyClass;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leaky_inner_class);

    leakyClass = new LeakyClass(this);
    leakyClass.finish();
  }

  /*
   * Mistake Number 2:
   * 1. Never create an inner variable of an inner class
   * 2. Never pass an instance of the activity to the inner class
   */
  private class LeakyClass {

    private Activity activity;

    public LeakyClass(Activity activity) {
      this.activity = activity;
    }

    public void finish() {
      activity.finish();
    }
  }
}
