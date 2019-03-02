package com.orobator.helloandroid.lesson9.di;

import com.orobator.helloandroid.lesson9.view.NumberFactActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
  @ContributesAndroidInjector(modules = { NumberFactActivityModule.class })
  abstract NumberFactActivity bindNumberFactActivity();
}
