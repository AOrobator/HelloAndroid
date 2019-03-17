package com.orobator.helloandroid.lesson09_lab.di;

import android.app.Application;
import com.orobator.helloandroid.lesson09_lab.view.TipCalcActivity;
import dagger.BindsInstance;
import dagger.Component;

@Component(modules = TipCalcModule.class)
public interface TipCalcComponent {
  void inject(TipCalcActivity target);

  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(Application app);

    Builder tipCalcModule(TipCalcModule module);

    TipCalcComponent build();
  }
}
