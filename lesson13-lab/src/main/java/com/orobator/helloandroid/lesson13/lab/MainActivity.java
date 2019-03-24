package com.orobator.helloandroid.lesson13.lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button startServiceButton = findViewById(R.id.startServiceButton);
    startServiceButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = DemoService.getStartIntent(MainActivity.this);
        startService(intent);
      }
    });

    Button stopServiceButton = findViewById(R.id.stopServiceButton);
    stopServiceButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = DemoService.getStopIntent(MainActivity.this);
        startService(intent);
      }
    });
  }
}
