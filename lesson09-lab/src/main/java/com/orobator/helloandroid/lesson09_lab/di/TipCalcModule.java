package com.orobator.helloandroid.lesson09_lab.di;

import android.app.Application;
import com.orobator.helloandroid.lesson09_lab.viewmodel.TipCalcViewModelFactory;
import com.orobator.helloandroid.tipcalc.model.Calculator;
import dagger.Module;
import dagger.Provides;

@Module
public class TipCalcModule {
  private final Application app;

  public TipCalcModule(Application application) {
    this.app = application;
  }

  @Provides
  public Calculator provideCalculator() {
    return new Calculator();
  }

  @Provides
  public TipCalcViewModelFactory provideTipCalcViewModelFactory(Calculator calc) {
    return new TipCalcViewModelFactory(app, calc);
  }
}
