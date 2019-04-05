package com.orobator.helloandroid.lesson18.followalong.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.orobator.helloandroid.lesson18.followalong.data.GithubRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

  private final GithubRepository repository;

  public ViewModelFactory(GithubRepository repository) {
    this.repository = repository;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(SearchRepositoriesViewModel.class)) {
      return (T) new SearchRepositoriesViewModel(this.repository);
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
