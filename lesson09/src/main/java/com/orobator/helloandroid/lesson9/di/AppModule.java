package com.orobator.helloandroid.lesson9.di;

import android.util.Log;
import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.common.connectivity.AndroidConnectionChecker;
import com.orobator.helloandroid.common.connectivity.ConnectionChecker;
import com.orobator.helloandroid.lesson9.NumberFactApplication;
import com.orobator.helloandroid.numbers.api.NumbersApi;
import com.orobator.helloandroid.numbers.api.NumbersRepository;
import com.orobator.helloandroid.numbers.api.NumbersRepositoryImpl;
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
        .client(client)
        .baseUrl("http://numbersapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }

  @Provides
  @Singleton
  public NumbersApi provideNumbersApi(Retrofit retrofit) {
    return retrofit.create(NumbersApi.class);
  }

  @Provides
  @Singleton
  public NumbersRepository provideNumbersRepository(NumbersApi api) {
    return new NumbersRepositoryImpl(api);
  }

  @Provides
  @Singleton
  public ConnectionChecker provideConnectionChecker(NumberFactApplication app) {
    return new AndroidConnectionChecker(app);
  }
}
