package com.orobator.helloandroid.lesson13;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button playButton = findViewById(R.id.play_button);
    Button pauseButton = findViewById(R.id.pause_button);
    Button stopButton = findViewById(R.id.stop_button);

    playButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });
  }
}
