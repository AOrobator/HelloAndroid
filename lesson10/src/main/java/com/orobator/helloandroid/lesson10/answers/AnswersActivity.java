package com.orobator.helloandroid.lesson10.answers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.orobator.helloandroid.lesson10.R;
import com.orobator.helloandroid.stackoverflow.questions.Question;

public class AnswersActivity extends AppCompatActivity {
  private static final String KEY_QUESTION = "question";

  public static Intent getIntent(Context context, Question question) {
    Intent intent = new Intent(context, AnswersActivity.class);
    intent.putExtra(KEY_QUESTION, question);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_answers);

    Question question = getIntent().getParcelableExtra(KEY_QUESTION);
  }
}
