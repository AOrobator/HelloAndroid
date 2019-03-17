package com.orobator.helloandroid.lesson09_lab.di;

import android.app.Application;
import com.orobator.helloandroid.lesson09_lab.TipCalcApplication;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
    TipCalcModule.class,
    ActivityBindingModule.class,
    AndroidSupportInjectionModule.class
})
public interface TipCalcComponent {
  void inject(TipCalcApplication target);

  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(Application app);

    TipCalcComponent build();
  }
}
