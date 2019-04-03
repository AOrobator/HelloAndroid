package com.orobator.helloandroid.lesson17;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends AppCompatActivity implements
    NameDialogFragment.NameDialogListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button showDialogButton = findViewById(R.id.showDialogButton);
    showDialogButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        DialogFragment newFragment = new NameDialogFragment();
        newFragment.show(getSupportFragmentManager(), "name_dialog");
      }
    });
  }

  @Override public void onNameEntered(String name) {
    Toast.makeText(this, "Name Entered: " + name, Toast.LENGTH_SHORT).show();
  }

  @Override public void onNegativeButtonClicked() {
    Toast.makeText(this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
  }
}
