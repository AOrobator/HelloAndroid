package com.orobator.helloandroid.lesson09_lab.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson09_lab.R;
import com.orobator.helloandroid.lesson09_lab.TipCalcApplication;
import com.orobator.helloandroid.lesson09_lab.databinding.ActivityTipCalcBinding;
import com.orobator.helloandroid.lesson09_lab.viewmodel.TipCalcViewModel;
import com.orobator.helloandroid.lesson09_lab.viewmodel.TipCalcViewModelFactory;
import javax.inject.Inject;

public class TipCalcActivity extends AppCompatActivity {

  @Inject TipCalcViewModelFactory factory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((TipCalcApplication) getApplication())
        .getComponent()
        .inject(this);

    ActivityTipCalcBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_tip_calc);
    setSupportActionBar(binding.toolbar);

    TipCalcViewModel vm = ViewModelProviders.of(this, factory).get(TipCalcViewModel.class);
    binding.setVm(vm);
  }
}
