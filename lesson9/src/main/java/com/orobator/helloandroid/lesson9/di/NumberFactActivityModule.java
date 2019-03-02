package com.orobator.helloandroid.lesson9.di;

import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.common.connectivity.ConnectionChecker;
import com.orobator.helloandroid.lesson9.viewmodel.NumberFactViewModelFactory;
import com.orobator.helloandroid.numbers.api.NumbersRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class NumberFactActivityModule {
  @Provides
  public NumberFactViewModelFactory provideNumberFactViewModelFactory(
      NumbersRepository repository,
      ConnectionChecker checker,
      AppSchedulers schedulers
  ) {
    return new NumberFactViewModelFactory(checker, repository, schedulers);
  }
}
