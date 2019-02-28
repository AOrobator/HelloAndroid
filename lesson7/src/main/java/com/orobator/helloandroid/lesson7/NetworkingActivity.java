package com.orobator.helloandroid.lesson7;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.snackbar.Snackbar;
import com.orobator.helloandroid.lesson7.databinding.ActivityNetworkingBinding;
import com.orobator.helloandroid.lesson7.model.AndroidConnectionChecker;
import com.orobator.helloandroid.lesson7.viewmodel.OkHttpNumberFactViewModel;
import com.orobator.helloandroid.lesson7.viewmodel.OkHttpNumberFactViewModel.NetworkState;
import com.orobator.helloandroid.lesson7.viewmodel.ViewEvent;

public class NetworkingActivity extends AppCompatActivity
    implements Observer<ViewEvent<NetworkState>> {

  private ActivityNetworkingBinding binding;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_networking);
    OkHttpNumberFactViewModel viewModel =
        ViewModelProviders.of(this).get(OkHttpNumberFactViewModel.class);
    viewModel.init(new AndroidConnectionChecker(getApplication()));

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
