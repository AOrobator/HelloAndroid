package com.orobator.helloandroid.lesson10.answers.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.orobator.helloandroid.lesson10.R;
import com.orobator.helloandroid.lesson10.answers.view.AnswersRecyclerAdapter.QuestionDetailViewHolder;
import com.orobator.helloandroid.lesson10.answers.viewmodel.AnswerViewModel;
import com.orobator.helloandroid.lesson10.databinding.ListItemAnswerBinding;
import com.orobator.helloandroid.lesson10.databinding.ListItemQuestionDetailBinding;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionViewModel;
import java.util.Collections;
import java.util.List;

public class AnswersRecyclerAdapter extends RecyclerView.Adapter<QuestionDetailViewHolder> {
  private static final int VIEW_TYPE_QUESTION = 0;
  private static final int VIEW_TYPE_ANSWER = 1;

  private List<AnswerViewModel> answerViewModels = Collections.emptyList();
  private final QuestionViewModel questionViewModel;

  public AnswersRecyclerAdapter(QuestionViewModel questionViewModel) {
    this.questionViewModel = questionViewModel;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return VIEW_TYPE_QUESTION;
    } else {
      return VIEW_TYPE_ANSWER;
    }
  }

  @Override public int getItemCount() {
    return 1 + answerViewModels.size();
  }

  @NonNull @Override
  public QuestionDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_QUESTION) {
      return onCreateQuestionViewHolder(parent);
    } else {
      return onCreateAnswerViewHolder(parent);
    }
  }

  private QuestionViewHolder onCreateQuestionViewHolder(ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ListItemQuestionDetailBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.list_item_question_detail, parent, false);
    return new QuestionViewHolder(binding);
  }

  private QuestionDetailViewHolder onCreateAnswerViewHolder(ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ListItemAnswerBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.list_item_answer, parent, false);
    return new AnswerViewHolder(binding);
  }

  @Override public void onBindViewHolder(@NonNull QuestionDetailViewHolder holder, int position) {
    if (getItemViewType(position) == VIEW_TYPE_QUESTION) {
      ((QuestionViewHolder) holder).bind(questionViewModel);
    } else {
      ((AnswerViewHolder) holder).bind(answerViewModels.get(position - 1));
    }
  }

  public void updateList(List<AnswerViewModel> answerViewModels) {
    this.answerViewModels = answerViewModels;
    notifyDataSetChanged();
  }

  static abstract class QuestionDetailViewHolder extends RecyclerView.ViewHolder {
    public QuestionDetailViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }

  static class QuestionViewHolder extends QuestionDetailViewHolder {
    private final ListItemQuestionDetailBinding binding;

    public QuestionViewHolder(ListItemQuestionDetailBinding binding) {
      super(binding.getRoot());

      this.binding = binding;
    }

    public void bind(QuestionViewModel questionViewModel) {
      binding.setVm(questionViewModel);

      Glide
          .with(itemView)
          .load(questionViewModel.profilePicUrl)
          .into(binding.profileImageView);
    }
  }

  static class AnswerViewHolder extends QuestionDetailViewHolder {
    private final ListItemAnswerBinding binding;

    public AnswerViewHolder(ListItemAnswerBinding binding) {
      super(binding.getRoot());

      this.binding = binding;
    }

    public void bind(AnswerViewModel answerViewModel) {
      binding.setVm(answerViewModel);
    }
  }
}
