package com.orobator.helloandroid.lesson14;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BroadcastActivity extends AppCompatActivity {
  private AirplaneReceiver receiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_broadcast);
  }

  @Override protected void onStart() {
    super.onStart();

    receiver = new AirplaneReceiver();
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
    registerReceiver(receiver, filter);
  }

  @Override protected void onStop() {
    super.onStop();

    unregisterReceiver(receiver);
  }
}
