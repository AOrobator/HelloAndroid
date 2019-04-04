package com.orobator.helloandroid.lesson18.ui;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.orobator.helloandroid.lesson18.model.Repo;

/**
 * Adapter for the list of repositories
 */
class ReposAdapter extends ListAdapter<Repo, RecyclerView.ViewHolder> {
  private static final DiffUtil.ItemCallback<Repo> REPO_COMPARATOR =
      new DiffUtil.ItemCallback<Repo>() {
        @Override public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
          return oldItem.getFullName().equals(newItem.getFullName());
        }

        @Override public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
          return oldItem.equals(newItem);
        }
      };

  ReposAdapter() {
    super(REPO_COMPARATOR);
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return RepoViewHolder.create(parent);
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    Repo repoItem = getItem(position);
    if (repoItem != null) {
      ((RepoViewHolder) holder).bind(repoItem);
    }
  }
}
