package com.orobator.helloandroid.lesson9.di;

import com.orobator.helloandroid.lesson9.NumberFactApplication;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class,
    ActivityBindingModule.class
})
public interface AppComponent {
  void inject(NumberFactApplication app);

  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(NumberFactApplication application);

    AppComponent build();
  }
}
