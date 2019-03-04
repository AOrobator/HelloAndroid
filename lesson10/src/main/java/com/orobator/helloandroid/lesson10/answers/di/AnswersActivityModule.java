package com.orobator.helloandroid.lesson10.answers.di;

import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.lesson10.answers.viewmodel.AnswersViewModelFactory;
import com.orobator.helloandroid.stackoverflow.answers.AnswersApi;
import com.orobator.helloandroid.stackoverflow.answers.AnswersRepository;
import com.orobator.helloandroid.stackoverflow.answers.AnswersRepositoryImpl;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AnswersActivityModule {
  @Provides
  public AnswersApi provideAnswersApi(Retrofit retrofit) {
    return retrofit.create(AnswersApi.class);
  }

  @Provides
  public AnswersRepository provideAnswersRepository(AnswersApi answersApi) {
    return new AnswersRepositoryImpl(answersApi);
  }

  @Provides
  public AnswersViewModelFactory provideAnswersViewModelFactory(
      AnswersRepository answersRepository,
      AppSchedulers schedulers) {
    return new AnswersViewModelFactory(answersRepository, schedulers);
  }
}
