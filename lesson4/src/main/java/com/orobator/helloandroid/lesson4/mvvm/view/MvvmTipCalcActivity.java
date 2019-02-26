package com.orobator.helloandroid.lesson4.mvvm.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.orobator.helloandroid.lesson4.R;
import com.orobator.helloandroid.lesson4.databinding.ActivityMvvmTipCalcBinding;
import com.orobator.helloandroid.lesson4.mvvm.viewmodel.TipCalcViewModel;
import com.orobator.helloandroid.tipcalc.model.Calculator;

public class MvvmTipCalcActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Automatically generated binding class.
    // Naming: <LayoutName>Binding
    // Contains references to all views with ids in the specified layout.
    ActivityMvvmTipCalcBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_mvvm_tip_calc);
    setSupportActionBar(binding.toolbar);

    binding.setVm(new TipCalcViewModel(getApplication(), new Calculator()));
  }
}
