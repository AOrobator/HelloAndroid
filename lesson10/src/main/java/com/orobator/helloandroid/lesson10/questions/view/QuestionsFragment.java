package com.orobator.helloandroid.lesson10.questions.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.orobator.helloandroid.lesson10.R;
import com.orobator.helloandroid.lesson10.databinding.FragmentQuestionsBinding;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionsViewModel;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionsViewModelFactory;
import com.orobator.helloandroid.stackoverflow.questions.Question;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

public class QuestionsFragment extends Fragment implements Observer<Question> {
  private FragmentQuestionsBinding binding;
  @Inject QuestionsViewModelFactory viewModelFactory;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    AndroidSupportInjection.inject(this);
  }

  @Nullable @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_questions,
        container,
        false);

    return binding.getRoot();
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);

    QuestionsViewModel viewModel =
        ViewModelProviders.of(this, viewModelFactory).get(QuestionsViewModel.class);
    viewModel.loadQuestions();
    binding.setVm(viewModel);

    QuestionsRecyclerAdapter adapter = new QuestionsRecyclerAdapter(viewModel);
    RecyclerView questionsRecyclerView =
        binding.getRoot().findViewById(R.id.questions_recycler_view);
    questionsRecyclerView.setAdapter(adapter);

    viewModel.questionViewModelsLiveData().observe(this, adapter::updateList);
    viewModel.questionLiveData().observe(this, this);

    viewModel.loadQuestions();
  }

  @Override public void onChanged(Question question) {

  }
}
