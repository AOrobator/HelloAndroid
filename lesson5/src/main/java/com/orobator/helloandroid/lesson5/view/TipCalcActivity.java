package com.orobator.helloandroid.lesson5.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.orobator.helloandroid.lesson5.R;
import com.orobator.helloandroid.lesson5.databinding.ActivityTipCalcBinding;
import com.orobator.helloandroid.lesson5.model.Calculator;
import com.orobator.helloandroid.lesson5.viewmodel.TipCalcViewModel;

public class TipCalcActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityTipCalcBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_tip_calc);
    setSupportActionBar(binding.toolbar);

    binding.setVm(new TipCalcViewModel(getApplication(), new Calculator()));
  }
}
