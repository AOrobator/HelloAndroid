package com.orobator.helloandroid.lesson13;

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

    Button playButton = findViewById(R.id.play_button);
    playButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = MusicPlayerService.getPlayIntent(MainActivity.this);
        startService(intent);
      }
    });

    Button pauseButton = findViewById(R.id.pause_button);
    pauseButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = MusicPlayerService.getPauseIntent(MainActivity.this);
        startService(intent);
      }
    });

    Button stopButton = findViewById(R.id.stop_button);
    stopButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = MusicPlayerService.getStopIntent(MainActivity.this);
        startService(intent);
      }
    });
  }
}
