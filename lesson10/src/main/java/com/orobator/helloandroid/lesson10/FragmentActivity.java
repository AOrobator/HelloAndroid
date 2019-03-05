package com.orobator.helloandroid.lesson10;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.orobator.helloandroid.lesson10.questions.view.QuestionsFragment;

public class FragmentActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);

    QuestionsFragment fragment = new QuestionsFragment();
    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commit();
  }
}
