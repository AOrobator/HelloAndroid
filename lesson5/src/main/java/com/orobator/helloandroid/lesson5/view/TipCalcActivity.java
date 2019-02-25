package com.orobator.helloandroid.lesson5.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson5.R;
import com.orobator.helloandroid.lesson5.databinding.ActivityTipCalcBinding;
import com.orobator.helloandroid.lesson5.viewmodel.TipCalcViewModel;

public class TipCalcActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityTipCalcBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_tip_calc);
    setSupportActionBar(binding.toolbar);

    // Creates new instance of vm when first launched
    // Returns same instance across configuration changes
    // Will get same instance until Activity finishes.
    TipCalcViewModel vm = ViewModelProviders.of(this).get(TipCalcViewModel.class);
    binding.setVm(vm);
  }
}
