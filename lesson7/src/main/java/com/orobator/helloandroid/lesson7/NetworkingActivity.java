package com.orobator.helloandroid.lesson7;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.snackbar.Snackbar;
import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.common.connectivity.AndroidConnectionChecker;
import com.orobator.helloandroid.common.connectivity.NetworkState;
import com.orobator.helloandroid.common.view.ViewEvent;
import com.orobator.helloandroid.lesson7.databinding.ActivityNetworkingBinding;
import com.orobator.helloandroid.lesson7.viewmodel.RetrofitNumberFactViewModel;
import com.orobator.helloandroid.numbers.api.NumbersApi;
import com.orobator.helloandroid.numbers.api.NumbersRepositoryImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkingActivity extends AppCompatActivity
    implements Observer<ViewEvent<NetworkState>> {

  private ActivityNetworkingBinding binding;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_networking);
    //OkHttpNumberFactViewModel viewModel =
    //    ViewModelProviders.of(this).get(OkHttpNumberFactViewModel.class);
    //viewModel.init(new AndroidConnectionChecker(getApplication()));

    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(
            new HttpLoggingInterceptor(message -> Log.d("Retrofit", message))
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl("http://numbersapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    NumbersApi numbersApi = retrofit.create(NumbersApi.class);

    RetrofitNumberFactViewModel viewModel =
        ViewModelProviders.of(this).get(RetrofitNumberFactViewModel.class);

    viewModel.init(
        new AndroidConnectionChecker(getApplication()),
        new NumbersRepositoryImpl(numbersApi),
        new AppSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
    );

    binding.setVm(viewModel);

    viewModel.getNetworkStateLiveData().observe(this, this);
  }

  @Override public void onChanged(ViewEvent<NetworkState> event) {
    NetworkState state = event.consume();

    if (state == NetworkState.DISCONNECTED) {
      Snackbar.make(binding.root, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
    }
  }
}
