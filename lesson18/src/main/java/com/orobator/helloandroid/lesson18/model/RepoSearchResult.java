package com.orobator.helloandroid.lesson18.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

public class RepoSearchResult {
  private final LiveData<PagedList<Repo>> data;
  private final MutableLiveData<String> networkErrors;

  public RepoSearchResult(
      LiveData<PagedList<Repo>> data, MutableLiveData<String> networkErrors) {
    this.data = data;
    this.networkErrors = networkErrors;
  }

  public LiveData<PagedList<Repo>> getData() {
    return data;
  }

  public MutableLiveData<String> getNetworkErrors() {
    return networkErrors;
  }
}
