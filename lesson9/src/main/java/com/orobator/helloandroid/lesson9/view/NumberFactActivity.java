package com.orobator.helloandroid.lesson9.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson9.R;
import com.orobator.helloandroid.lesson9.databinding.ActivityNumberFactBinding;
import com.orobator.helloandroid.lesson9.viewmodel.NumberFactViewModel;
import com.orobator.helloandroid.lesson9.viewmodel.NumberFactViewModelFactory;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class NumberFactActivity extends AppCompatActivity {

  @Inject NumberFactViewModelFactory viewModelFactory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidInjection.inject(this);

    ActivityNumberFactBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_number_fact);

    NumberFactViewModel viewModel =
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(NumberFactViewModel.class);

    binding.setVm(viewModel);
  }
}
