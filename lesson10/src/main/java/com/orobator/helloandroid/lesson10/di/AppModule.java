package com.orobator.helloandroid.lesson10.di;

import android.util.Log;
import com.orobator.helloandroid.common.AppSchedulers;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
  @Provides
  public AppSchedulers provideAppSchedulers() {
    return new AppSchedulers(AndroidSchedulers.mainThread(), Schedulers.io());
  }

  @Provides
  @Singleton
  public OkHttpClient provideOkHttpClient() {
    return new OkHttpClient.Builder()
        .addInterceptor(
            new HttpLoggingInterceptor(message -> Log.d("Retrofit", message))
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build();
  }

  @Provides
  @Singleton
  public Retrofit provideRetrofit(OkHttpClient client) {
    return new Retrofit.Builder()
        .baseUrl("https://api.stackexchange.com")
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

}
