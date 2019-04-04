package com.orobator.helloandroid.lesson18.data;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.orobator.helloandroid.lesson18.api.GithubService;
import com.orobator.helloandroid.lesson18.db.GithubLocalCache;
import com.orobator.helloandroid.lesson18.model.Repo;
import com.orobator.helloandroid.lesson18.model.RepoSearchResult;
import java.util.List;

/**
 * Repository class that works with local and remote data sources.
 */
public class GithubRepository {
  private static final int NETWORK_PAGE_SIZE = 50;

  private final GithubService service;
  private final GithubLocalCache cache;

  // Keep the last requested page. When the request is successful, increment
  // the page number.
  private int lastRequestedPage = 1;

  // LiveData of network errors.
  private MutableLiveData<String> networkErrors = new MutableLiveData<>();

  // Avoid triggering multiple requests in the same time
  private boolean isRequestInProgress = false;

  public GithubRepository(GithubService service, GithubLocalCache cache) {
    this.service = service;
    this.cache = cache;
  }

  /**
   * Search repositories whose names match the query.
   */
  public RepoSearchResult search(String query) {
    Log.d("GithubRepository", "New query: " + query);
    lastRequestedPage = 1;
    requestAndSaveData(query);

    // Get data from the local cache
    LiveData<List<Repo>> data = cache.reposByName(query);

    return new RepoSearchResult(data, networkErrors);
  }

  public void requestMore(String query) {
    requestAndSaveData(query);
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
