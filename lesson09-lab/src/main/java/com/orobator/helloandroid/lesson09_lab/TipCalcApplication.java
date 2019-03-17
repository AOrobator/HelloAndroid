package com.orobator.helloandroid.lesson09_lab;

import android.app.Application;
import com.orobator.helloandroid.lesson09_lab.di.DaggerTipCalcComponent;
import com.orobator.helloandroid.lesson09_lab.di.TipCalcComponent;
import com.orobator.helloandroid.lesson09_lab.di.TipCalcModule;

public class TipCalcApplication extends Application {
  private TipCalcComponent component;

  @Override public void onCreate() {
    super.onCreate();

    component = DaggerTipCalcComponent
        .builder()
        .tipCalcModule(new TipCalcModule(this))
        .build();
  }

  public TipCalcComponent getComponent() {
    return component;
  }
}
