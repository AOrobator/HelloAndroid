package com.orobator.helloandroid.lesson09_lab.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson09_lab.R;
import com.orobator.helloandroid.lesson09_lab.databinding.ActivityTipCalcBinding;
import com.orobator.helloandroid.lesson09_lab.viewmodel.TipCalcViewModel;
import com.orobator.helloandroid.lesson09_lab.viewmodel.TipCalcViewModelFactory;
import com.orobator.helloandroid.tipcalc.model.Calculator;

public class TipCalcActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityTipCalcBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_tip_calc);
    setSupportActionBar(binding.toolbar);

    TipCalcViewModelFactory factory =
        new TipCalcViewModelFactory(getApplication(), new Calculator());
    TipCalcViewModel vm = ViewModelProviders.of(this, factory).get(TipCalcViewModel.class);
    binding.setVm(vm);
  }
}
