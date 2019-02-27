package com.orobator.helloandroid.lesson7.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import com.orobator.helloandroid.observableviewmodel.ObservableViewModel;

public class NumberFactViewModel extends ObservableViewModel {
  public NumberFactViewModel(@NonNull Application application) {
    super(application);
  }

  public String inputNumber = "";

  public String outputFact = "";

  public void getRandomFact() {

  }
}
