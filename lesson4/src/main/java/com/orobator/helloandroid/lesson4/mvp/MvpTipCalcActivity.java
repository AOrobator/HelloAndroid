package com.orobator.helloandroid.lesson4.mvp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.orobator.helloandroid.lesson4.R;

public class MvpTipCalcActivity extends AppCompatActivity
    implements TipCalcPresenter.TipCalcPresentation {

  private final TipCalcPresenter presenter = new TipCalcPresenter();
  private EditText checkAmountEditText;
  private EditText tipPercentEditText;
  private TextView checkAmountTextView;
  private TextView tipAmountTextView;
  private TextView grandTotalTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mvc_mvp_tip_calc);

    Button calculateTipButton = findViewById(R.id.calculate_tip_Button);
    calculateTipButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        presenter.calculateTip();
      }
    });

    checkAmountEditText = findViewById(R.id.check_amount_EditText);
    tipPercentEditText = findViewById(R.id.tip_percent_EditText);
    checkAmountTextView = findViewById(R.id.check_amount_TextView);
    tipAmountTextView = findViewById(R.id.tip_amount_TextView);
    grandTotalTextView = findViewById(R.id.grand_total_TextView);
  }

  @Override
  protected void onStart() {
    super.onStart();

    presenter.attach(this);
  }

  @Override
  protected void onStop() {
    super.onStop();

    presenter.detach();
  }

  @Override public String getCheckAmount() {
    return checkAmountEditText.getText().toString();
  }

  @Override public String getTipPercent() {
    return tipPercentEditText.getText().toString();
  }

  @Override public void showError(String errMsg) {
    Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
  }

  @Override public void setCheckAmount(double checkAmount) {
    checkAmountTextView.setText(getString(R.string.money_template, checkAmount));
  }

  @Override public void setTipAmount(double tipAmount) {
    tipAmountTextView.setText(getString(R.string.money_template, tipAmount));
  }

  @Override public void setGrandTotal(double grandTotal) {
    grandTotalTextView.setText(getString(R.string.money_template, grandTotal));
  }
}
