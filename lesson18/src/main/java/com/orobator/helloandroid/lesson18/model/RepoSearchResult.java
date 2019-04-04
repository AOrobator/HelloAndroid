package com.orobator.helloandroid.lesson18.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class RepoSearchResult {
  private final LiveData<List<Repo>> data;
  private final MutableLiveData<String> networkErrors;

  public RepoSearchResult(
      LiveData<List<Repo>> data, MutableLiveData<String> networkErrors) {
    this.data = data;
    this.networkErrors = networkErrors;
  }

  public LiveData<List<Repo>> getData() {
    return data;
  }

  public MutableLiveData<String> getNetworkErrors() {
    return networkErrors;
  }
}
