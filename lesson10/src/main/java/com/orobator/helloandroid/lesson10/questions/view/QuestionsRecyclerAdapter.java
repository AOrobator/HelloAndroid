package com.orobator.helloandroid.lesson10.questions.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.orobator.helloandroid.lesson10.R;
import com.orobator.helloandroid.lesson10.databinding.ListItemQuestionBinding;
import com.orobator.helloandroid.lesson10.questions.view.QuestionsRecyclerAdapter.QuestionViewHolder;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionViewModel;
import com.orobator.helloandroid.lesson10.questions.viewmodel.QuestionsViewModel;
import java.util.Collections;
import java.util.List;

public class QuestionsRecyclerAdapter extends RecyclerView.Adapter<QuestionViewHolder> {
  private List<QuestionViewModel> questionViewModels = Collections.emptyList();
  private QuestionsViewModel viewModel;

  public QuestionsRecyclerAdapter(QuestionsViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override public int getItemCount() {
    return questionViewModels.size();
  }

  @NonNull @Override
  public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ListItemQuestionBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.list_item_question, parent, false);
    return new QuestionViewHolder(binding, viewModel);
  }

  @Override public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
    holder.bind(questionViewModels.get(position));
  }

  public void updateList(List<QuestionViewModel> questions) {
    questionViewModels = questions;
    notifyDataSetChanged();
  }

  static public class QuestionViewHolder extends RecyclerView.ViewHolder {
    private final ListItemQuestionBinding binding;
    private final QuestionsViewModel viewModel;

    public QuestionViewHolder(
        @NonNull ListItemQuestionBinding binding,
        QuestionsViewModel viewModel) {
      super(binding.getRoot());

      this.binding = binding;
      this.viewModel = viewModel;
    }

    public void bind(QuestionViewModel viewModel) {
      binding.setVm(viewModel);

      binding.chipGroup.removeAllViews();
      for (String tag : viewModel.tags) {
        Chip chip = new Chip(itemView.getContext());
        chip.setText(tag);
        chip.setChipBackgroundColorResource(R.color.orange_100);
        binding.chipGroup.addView(chip);
      }

      binding
          .clickTarget
          .setOnClickListener(v ->
              this.viewModel.onQuestionClicked(getAdapterPosition()));
    }
  }
}
