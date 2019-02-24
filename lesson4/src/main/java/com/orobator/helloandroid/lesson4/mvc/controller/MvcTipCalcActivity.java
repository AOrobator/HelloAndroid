package com.orobator.helloandroid.lesson4.mvc.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.orobator.helloandroid.lesson4.R;
import com.orobator.helloandroid.lesson4.mvc.model.Calculator;
import com.orobator.helloandroid.lesson4.mvc.model.TipCalculation;

public class MvcTipCalcActivity extends AppCompatActivity {
  private final Calculator calculator = new Calculator();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mvc_mvp_tip_calc);

    Button calculateTipButton = findViewById(R.id.calculate_tip_Button);
    final EditText checkAmountEditText = findViewById(R.id.check_amount_EditText);
    final EditText tipAmountEditText = findViewById(R.id.tip_amount_EditText);
    final TextView checkAmountTextView = findViewById(R.id.check_amount_TextView);
    final TextView tipAmountTextView = findViewById(R.id.tip_amount_TextView);
    final TextView grandTotalTextView = findViewById(R.id.grand_total_TextView);

    calculateTipButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String checkAmountString = checkAmountEditText.getText().toString();
        double checkAmount = Double.parseDouble(checkAmountString);

        String tipPercentString = tipAmountEditText.getText().toString();
        int tipPercent = Integer.parseInt(tipPercentString);

        if (tipPercent > 100) {
          Toast
              .makeText(
                  MvcTipCalcActivity.this,
                  "Tip can't be greater than 100%",
                  Toast.LENGTH_SHORT
              ).show();
        } else {
          TipCalculation c = calculator.calculateTip(checkAmount, tipPercent);

          checkAmountTextView.setText(getString(R.string.money_template, c.checkAmount));
          tipAmountTextView.setText(getString(R.string.money_template, c.tipAmount));
          grandTotalTextView.setText(getString(R.string.money_template, c.grandTotal));
        }
      }
    });
  }
}
