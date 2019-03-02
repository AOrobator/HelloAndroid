package com.orobator.helloandroid.lesson9.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.common.connectivity.ConnectionChecker;
import com.orobator.helloandroid.numbers.api.NumbersRepository;

public class NumberFactViewModelFactory implements ViewModelProvider.Factory {
  private final ConnectionChecker connectionChecker;
  private final NumbersRepository numbersRepo;
  private final AppSchedulers schedulers;

  public NumberFactViewModelFactory(
      ConnectionChecker connectionChecker,
      NumbersRepository numbersRepo,
      AppSchedulers schedulers) {
    this.connectionChecker = connectionChecker;
    this.numbersRepo = numbersRepo;
    this.schedulers = schedulers;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new NumberFactViewModel(connectionChecker, numbersRepo, schedulers);
  }
}
