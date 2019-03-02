package com.orobator.helloandroid.lesson10.di;

import com.orobator.helloandroid.lesson10.QuestionsActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
  @ContributesAndroidInjector(modules = { QuestionsActivityModule.class })
  abstract QuestionsActivity bindNumberFactActivity();
}
