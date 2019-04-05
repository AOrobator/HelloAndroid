package com.orobator.helloandroid.lesson18.model;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

public class RepoSearchResult {
  private final LiveData<PagedList<Repo>> data;
  private final LiveData<String> networkErrors;

  public RepoSearchResult(
      LiveData<PagedList<Repo>> data, LiveData<String> networkErrors) {
    this.data = data;
    this.networkErrors = networkErrors;
  }

  public LiveData<PagedList<Repo>> getData() {
    return data;
  }

  public LiveData<String> getNetworkErrors() {
    return networkErrors;
  }
}
