package com.orobator.helloandroid.lesson09_lab.di;

import com.orobator.helloandroid.lesson09_lab.view.TipCalcActivity;
import dagger.Component;

@Component(modules = TipCalcModule.class)
public interface TipCalcComponent {
  void inject(TipCalcActivity target);

  @Component.Builder
  interface Builder {
    Builder tipCalcModule(TipCalcModule module);

    TipCalcComponent build();
  }
}
