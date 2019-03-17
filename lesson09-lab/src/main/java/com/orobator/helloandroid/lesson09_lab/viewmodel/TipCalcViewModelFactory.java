package com.orobator.helloandroid.lesson09_lab.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.orobator.helloandroid.tipcalc.model.Calculator;

public class TipCalcViewModelFactory implements ViewModelProvider.Factory {
  private final Application app;
  private final Calculator calculator;

  public TipCalcViewModelFactory(Application app, Calculator calculator) {
    this.app = app;
    this.calculator = calculator;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new TipCalcViewModel(app, calculator);
  }
}
