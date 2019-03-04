package com.orobator.helloandroid.lesson10.questions.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.stackoverflow.questions.QuestionsRepository;

public class QuestionsViewModelFactory implements ViewModelProvider.Factory {
  private final QuestionsRepository questionsRepository;
  private final AppSchedulers schedulers;

  public QuestionsViewModelFactory(
      QuestionsRepository questionsRepository,
      AppSchedulers schedulers) {
    this.questionsRepository = questionsRepository;
    this.schedulers = schedulers;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new QuestionsViewModel(questionsRepository, schedulers);
  }
}
