package com.orobator.helloandroid.lesson18.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import com.orobator.helloandroid.lesson18.api.GithubService;
import com.orobator.helloandroid.lesson18.db.GithubLocalCache;

public class RepoBoundaryCallback extends PagedList.BoundaryCallback {
  private static final int NETWORK_PAGE_SIZE = 50;

  private final String query;
  private final GithubService service;
  private final GithubLocalCache cache;

  // Keep the last requested page. When the request is successful, increment
  // the page number.
  private int lastRequestedPage = 1;

  // Avoid triggering multiple requests in the same time
  private boolean isRequestInProgress = false;

  // LiveData of network errors.
  private MutableLiveData<String> networkErrors = new MutableLiveData<>();

  public RepoBoundaryCallback(String query, GithubService service, GithubLocalCache cache) {
    this.query = query;
    this.service = service;
    this.cache = cache;
  }

  @Override
  public void onZeroItemsLoaded() {
    requestAndSaveData(query);
  }

  @Override
  public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
    requestAndSaveData(query);
  }

  public LiveData<String> getNetworkErrors() {
    return networkErrors;
  }

  private void requestAndSaveData(String query) {
    if (isRequestInProgress) return;

    isRequestInProgress = true;
    GithubService.searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE,
        repos -> cache.insert(repos, () -> {
          lastRequestedPage++;
          isRequestInProgress = false;
        }),
        error -> {
          networkErrors.postValue(error);
          isRequestInProgress = false;
        });
  }
}
