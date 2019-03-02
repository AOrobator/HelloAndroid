package com.orobator.helloandroid.lesson10.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.orobator.helloandroid.lesson10.R;
import com.orobator.helloandroid.lesson10.databinding.ListItemQuestionBinding;
import com.orobator.helloandroid.lesson10.view.QuestionsRecyclerAdapter.QuestionViewHolder;
import com.orobator.helloandroid.lesson10.viewmodel.QuestionViewModel;
import java.util.Collections;
import java.util.List;

public class QuestionsRecyclerAdapter extends RecyclerView.Adapter<QuestionViewHolder> {
  private List<QuestionViewModel> questionViewModels = Collections.emptyList();

  @NonNull @Override
  public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ListItemQuestionBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.list_item_question, parent, false);
    return new QuestionViewHolder(binding);
  }

  @Override public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
    holder.bind(questionViewModels.get(position));
  }

  @Override public int getItemCount() {
    return questionViewModels.size();
  }

  public void updateList(List<QuestionViewModel> questions) {
    questionViewModels = questions;
    notifyDataSetChanged();
  }

  static public class QuestionViewHolder extends RecyclerView.ViewHolder {
    private final ListItemQuestionBinding binding;

    public QuestionViewHolder(@NonNull ListItemQuestionBinding binding) {
      super(binding.getRoot());

      this.binding = binding;
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
    }
  }
}
