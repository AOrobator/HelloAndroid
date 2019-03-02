package com.orobator.helloandroid.lesson10.di;

import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.lesson10.viewmodel.QuestionsViewModelFactory;
import com.orobator.helloandroid.stackoverflow.questions.QuestionsApi;
import com.orobator.helloandroid.stackoverflow.questions.QuestionsDownloader;
import com.orobator.helloandroid.stackoverflow.questions.QuestionsRepository;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class QuestionsActivityModule {
  @Provides
  public QuestionsApi provideQuestionsApi(Retrofit retrofit) {
    return retrofit.create(QuestionsApi.class);
  }

  @Provides
  public QuestionsRepository provideQuestionsRepository(QuestionsApi questionsApi) {
    return new QuestionsDownloader(questionsApi);
  }

  @Provides
  public QuestionsViewModelFactory provideViewModelFactory(
      QuestionsRepository questionsRepository,
      AppSchedulers schedulers
  ) {
    return new QuestionsViewModelFactory(questionsRepository, schedulers);
  }
}
