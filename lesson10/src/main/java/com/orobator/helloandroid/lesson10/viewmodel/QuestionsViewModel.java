package com.orobator.helloandroid.lesson10.viewmodel;

import com.orobator.helloandroid.lesson10.BR;
import com.orobator.helloandroid.observableviewmodel.ObservableViewModel;

public class QuestionsViewModel extends ObservableViewModel {
  private boolean isLoading = false;
  private boolean isEmpty = false;

  public boolean isEmpty() {
    return isEmpty;
  }

  public boolean hasError() {
    return false;
  }

  public boolean isLoading() {
    return isLoading;
  }

  public void loadQuestions() {
    isLoading = true;
    isEmpty = false;

    notifyPropertyChanged(BR._all);
  }
}
