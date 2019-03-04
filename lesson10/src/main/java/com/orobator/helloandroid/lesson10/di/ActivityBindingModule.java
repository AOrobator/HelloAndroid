package com.orobator.helloandroid.lesson10.di;

import com.orobator.helloandroid.lesson10.answers.di.AnswersActivityModule;
import com.orobator.helloandroid.lesson10.answers.view.AnswersActivity;
import com.orobator.helloandroid.lesson10.questions.di.QuestionsActivityModule;
import com.orobator.helloandroid.lesson10.questions.view.QuestionsActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {
  @ContributesAndroidInjector(modules = { QuestionsActivityModule.class })
  abstract QuestionsActivity bindQuestionsActivity();

  @ContributesAndroidInjector(modules = { AnswersActivityModule.class })
  abstract AnswersActivity bindAnswersActivity();
}
