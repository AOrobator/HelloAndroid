package com.orobator.helloandroid.lesson10.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import com.orobator.helloandroid.lesson10.R;
import com.orobator.helloandroid.lesson10.databinding.ActivityQuestionsBinding;
import com.orobator.helloandroid.lesson10.viewmodel.QuestionsViewModel;
import com.orobator.helloandroid.lesson10.viewmodel.QuestionsViewModelFactory;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class QuestionsActivity extends AppCompatActivity {

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

    QuestionsRecyclerAdapter adapter = new QuestionsRecyclerAdapter();
    binding.questionsRecyclerView.setAdapter(adapter);

    viewModel.questionViewModelLiveData().observe(this, adapter::updateList);

    viewModel.loadQuestions();
  }
}
