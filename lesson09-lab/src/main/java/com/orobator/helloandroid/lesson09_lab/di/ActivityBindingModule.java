package com.orobator.helloandroid.lesson09_lab.di;

import com.orobator.helloandroid.lesson09_lab.view.TipCalcActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
  @ContributesAndroidInjector(modules = { TipCalcModule.class })
  abstract TipCalcActivity bindTipCalcActivity();
}
