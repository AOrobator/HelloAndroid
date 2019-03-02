package com.orobator.helloandroid.lesson8;

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

    Button asyncTaskLeakButton = findViewById(R.id.asyncTaskLeakButton);
    asyncTaskLeakButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), LeakyAsyncTaskActivity.class);
        startActivity(intent);
      }
    });

    Button innerClassLeakButton = findViewById(R.id.innerClassLeakButton);
    innerClassLeakButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), LeakyInnerClassActivity.class);
        startActivity(intent);
      }
    });
  }
}
