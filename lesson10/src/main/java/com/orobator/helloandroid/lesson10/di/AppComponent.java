package com.orobator.helloandroid.lesson10.di;

import com.orobator.helloandroid.lesson10.StackOverflowApplication;
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
  void inject(StackOverflowApplication app);

  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(StackOverflowApplication application);

    AppComponent build();
  }
}