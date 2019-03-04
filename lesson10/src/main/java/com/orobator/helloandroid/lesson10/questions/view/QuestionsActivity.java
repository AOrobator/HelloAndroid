package com.orobator.helloandroid.lesson10.questions.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson10.R;
import com.orobator.helloandroid.lesson10.answers.view.AnswersActivity;
import com.orobator.helloandroid.lesson10.databinding.ActivityQuestionsBinding;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionsViewModel;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionsViewModelFactory;
import com.orobator.helloandroid.stackoverflow.questions.Question;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class QuestionsActivity extends AppCompatActivity implements Observer<Question> {

  @Inject QuestionsViewModelFactory viewModelFactory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidInjection.inject(this);

    ActivityQuestionsBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_questions);

    QuestionsViewModel viewModel = ViewModelProviders
        .of(this, viewModelFactory)
        .get(QuestionsViewModel.class);

    binding.setVm(viewModel);

    QuestionsRecyclerAdapter adapter = new QuestionsRecyclerAdapter(viewModel);
    binding.questionsRecyclerView.setAdapter(adapter);

    viewModel.questionViewModelsLiveData().observe(this, adapter::updateList);
    viewModel.questionLiveData().observe(this, this);

    viewModel.loadQuestions();
  }

  @Override public void onChanged(Question question) {
    Intent intent = AnswersActivity.getIntent(this, question);
    startActivity(intent);
  }
}
