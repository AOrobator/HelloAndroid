package com.orobator.helloandroid.lesson4.mvvm;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.orobator.helloandroid.lesson4.R;
import com.orobator.helloandroid.lesson4.databinding.ActivityMvvmTipCalcBinding;
import com.orobator.helloandroid.lesson4.mvc.model.Calculator;

public class MvvmTipCalcActivity extends AppCompatActivity {
  private ActivityMvvmTipCalcBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm_tip_calc);
    setSupportActionBar(binding.toolbar);

    binding.setVm(new TipCalcViewModel(getApplication(), new Calculator()));
  }
}
