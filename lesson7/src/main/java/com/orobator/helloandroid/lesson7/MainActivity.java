package com.orobator.helloandroid.lesson7;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson7.databinding.ActivityMainBinding;
import com.orobator.helloandroid.lesson7.viewmodel.NumberFactViewModel;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    NumberFactViewModel viewModel = ViewModelProviders.of(this).get(NumberFactViewModel.class);
    binding.setVm(viewModel);
  }
}
