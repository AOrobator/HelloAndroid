package com.orobator.helloandroid.lesson10.answers.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.stackoverflow.answers.AnswersRepository;

public class AnswersViewModelFactory implements ViewModelProvider.Factory {
  private final AnswersRepository answersRepository;
  private final AppSchedulers schedulers;

  public AnswersViewModelFactory(
      AnswersRepository answersRepository,
      AppSchedulers schedulers) {
    this.answersRepository = answersRepository;
    this.schedulers = schedulers;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new AnswersViewModel(answersRepository, schedulers);
  }
}
