package com.orobator.helloandroid.lesson10.di;

import com.orobator.helloandroid.lesson10.viewmodel.QuestionsViewModelFactory;
import dagger.Module;
import dagger.Provides;

@Module
public class QuestionsActivityModule {
  @Provides
  public QuestionsViewModelFactory provideViewModelFactory() {
    return new QuestionsViewModelFactory();
  }
}
