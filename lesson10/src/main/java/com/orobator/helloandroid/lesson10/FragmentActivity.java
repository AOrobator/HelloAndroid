package com.orobator.helloandroid.lesson10;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson10.answers.view.AnswersFragment;
import com.orobator.helloandroid.lesson10.questions.view.QuestionsFragment;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionsViewModel;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionsViewModelFactory;
import com.orobator.helloandroid.stackoverflow.questions.Question;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class FragmentActivity extends AppCompatActivity implements Observer<Question> {
  @Inject QuestionsViewModelFactory viewModelFactory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);

    AndroidInjection.inject(this);

    QuestionsFragment fragment = new QuestionsFragment();
    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commit();

    QuestionsViewModel viewModel =
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(QuestionsViewModel.class);

    viewModel.questionLiveData().observe(this, this);
  }

  @Override public void onChanged(Question question) {
    AnswersFragment fragment = AnswersFragment.newInstance(question);
    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(AnswersFragment.class.getSimpleName())
        .commit();
  }
}
