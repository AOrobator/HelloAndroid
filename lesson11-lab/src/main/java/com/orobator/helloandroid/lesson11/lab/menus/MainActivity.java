package com.orobator.helloandroid.lesson11.lab.menus;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.lab_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.option_archive) {
      Toast.makeText(this, "Archive clicked!", Toast.LENGTH_SHORT).show();
    } else if (item.getItemId() == R.id.option_snooze) {
      Toast.makeText(this, "Snooze clicked!", Toast.LENGTH_SHORT).show();
    }

    return true;
  }
}
