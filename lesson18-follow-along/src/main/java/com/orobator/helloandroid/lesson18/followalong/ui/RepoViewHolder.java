package com.orobator.helloandroid.lesson18.followalong.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.orobator.helloandroid.lesson18.R;
import com.orobator.helloandroid.lesson18.followalong.model.Repo;

/**
 * View Holder for a {@link Repo} RecyclerView list item
 */
public class RepoViewHolder extends RecyclerView.ViewHolder {
  private TextView name;
  private TextView description;
  private TextView stars;
  private TextView language;
  private TextView forks;

  private Repo repo = null;

  public static RepoViewHolder create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.repo_view_item, parent, false);
    return new RepoViewHolder(view);
  }

  public RepoViewHolder(@NonNull View itemView) {
    super(itemView);

    name = itemView.findViewById(R.id.repo_name);
    description = itemView.findViewById(R.id.repo_description);
    stars = itemView.findViewById(R.id.repo_stars);
    language = itemView.findViewById(R.id.repo_language);
    forks = itemView.findViewById(R.id.repo_forks);

    itemView.setOnClickListener((view) -> {
      if (repo != null && repo.getUrl() != null) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repo.getUrl()));
        view.getContext().startActivity(intent);
      }
    });
  }

  public void bind(Repo repo) {
    if (repo == null) {
      Resources resources = itemView.getResources();
      name.setText(resources.getString(R.string.loading));
      description.setVisibility(View.GONE);
      language.setVisibility(View.GONE);
      stars.setText(resources.getString(R.string.unknown));
      forks.setText(resources.getString(R.string.unknown));
    } else {
      showRepoData(repo);
    }
  }

  @SuppressLint("SetTextI18n") private void showRepoData(Repo repo) {
    this.repo = repo;
    name.setText(repo.getFullName());

    // if the description is missing, hide the TextView
    int descriptionVisibility = View.GONE;
    if (repo.getDescription() != null) {
      description.setText(repo.getDescription());
      descriptionVisibility = View.VISIBLE;
    }
    description.setVisibility(descriptionVisibility);

    stars.setText(repo.getStars() + "");
    forks.setText(repo.getForks() + "");

    // if the language is missing, hide the label and the value
    int languageVisibility = View.GONE;
    if (!TextUtils.isEmpty(repo.getLanguage())) {
      Resources resources = this.itemView.getContext().getResources();
      language.setText(resources.getString(R.string.language, repo.getLanguage()));
      languageVisibility = View.VISIBLE;
    }
    language.setVisibility(languageVisibility);
  }
}
