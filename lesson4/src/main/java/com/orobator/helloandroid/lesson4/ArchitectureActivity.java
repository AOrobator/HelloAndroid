package com.orobator.helloandroid.lesson4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.orobator.helloandroid.lesson4.mvc.controller.MvcTipCalcActivity;

public class ArchitectureActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_architecture);

    Button mvcButton = findViewById(R.id.mvc_button);
    mvcButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(ArchitectureActivity.this, MvcTipCalcActivity.class);
        startActivity(intent);
      }
    });
  }
}
