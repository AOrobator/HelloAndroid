package com.orobator.helloandroid.lesson10.answers.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson10.R;
import com.orobator.helloandroid.lesson10.answers.viewmodel.AnswerViewModel;
import com.orobator.helloandroid.lesson10.answers.viewmodel.AnswersViewModel;
import com.orobator.helloandroid.lesson10.answers.viewmodel.AnswersViewModelFactory;
import com.orobator.helloandroid.lesson10.databinding.ActivityAnswersBinding;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionViewModel;
import com.orobator.helloandroid.stackoverflow.questions.Question;
import dagger.android.AndroidInjection;
import java.util.List;
import javax.inject.Inject;

public class AnswersActivity extends AppCompatActivity implements Observer<List<AnswerViewModel>> {
  private static final String KEY_QUESTION = "question";

  public static Intent getIntent(Context context, Question question) {
    Intent intent = new Intent(context, AnswersActivity.class);
    intent.putExtra(KEY_QUESTION, question);
    return intent;
  }

  private AnswersRecyclerAdapter adapter;

  @Inject AnswersViewModelFactory viewModelFactory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidInjection.inject(this);

    ActivityAnswersBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_answers);

    AnswersViewModel viewModel =
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(AnswersViewModel.class);

    Question question = getIntent().getParcelableExtra(KEY_QUESTION);
    viewModel.setQuestion(question);

    viewModel.getAnswerViewModelsLiveData().observe(this, this);

    adapter = new AnswersRecyclerAdapter(new QuestionViewModel(question));
    binding.answersRecyclerView.setAdapter(adapter);
  }

  @Override public void onChanged(List<AnswerViewModel> answerViewModels) {
    adapter.updateList(answerViewModels);
  }
}
