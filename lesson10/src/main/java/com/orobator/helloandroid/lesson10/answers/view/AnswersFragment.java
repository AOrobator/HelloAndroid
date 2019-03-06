package com.orobator.helloandroid.lesson10.answers.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.orobator.helloandroid.lesson10.answers.viewmodel.AnswerViewModel;
import com.orobator.helloandroid.lesson10.answers.viewmodel.AnswersViewModel;
import com.orobator.helloandroid.lesson10.answers.viewmodel.AnswersViewModelFactory;
import com.orobator.helloandroid.lesson10.databinding.FragmentAnswersBinding;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionViewModel;
import com.orobator.helloandroid.stackoverflow.questions.Question;
import dagger.android.support.AndroidSupportInjection;
import java.util.List;
import javax.inject.Inject;

public class AnswersFragment extends Fragment implements Observer<List<AnswerViewModel>> {
  private static final String KEY_QUESTION = "?";
  private FragmentAnswersBinding binding;
  @Inject AnswersViewModelFactory viewModelFactory;
  private AnswersRecyclerAdapter adapter;

  public static AnswersFragment newInstance(Question question) {
    Bundle args = new Bundle();
    args.putParcelable(KEY_QUESTION, question);
    AnswersFragment fragment = new AnswersFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);

    AndroidSupportInjection.inject(this);
    setHasOptionsMenu(true);
  }

  @Nullable @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_answers, container, false);
    return binding.getRoot();
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    AnswersViewModel viewModel =
        ViewModelProviders.of(this, viewModelFactory).get(AnswersViewModel.class);

    Question question = getArguments().getParcelable(KEY_QUESTION);
    viewModel.setQuestion(question);

    binding.setVm(viewModel);

    viewModel.getAnswerViewModelsLiveData().observe(this, this);

    adapter = new AnswersRecyclerAdapter(new QuestionViewModel(question));
    RecyclerView answersRecyclerView = view.findViewById(R.id.answersRecyclerView);

    answersRecyclerView.setAdapter(adapter);
  }

  @Override public void onChanged(List<AnswerViewModel> answerViewModels) {
    adapter.updateList(answerViewModels);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      getActivity().getSupportFragmentManager().popBackStack();
    }

    return true;
  }
}
