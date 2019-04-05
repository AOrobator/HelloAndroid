package com.orobator.helloandroid.lesson18.ui;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import com.orobator.helloandroid.lesson18.data.GithubRepository;
import com.orobator.helloandroid.lesson18.model.Repo;
import com.orobator.helloandroid.lesson18.model.RepoSearchResult;

/**
 * ViewModel for the {@link SearchRepositoriesActivity} screen.
 * The ViewModel works with the {@link GithubRepository} to get the data.
 */
class SearchRepositoriesViewModel extends ViewModel {
  private static final int VISIBLE_THRESHOLD = 5;
  private GithubRepository repository;
  private MutableLiveData<String> queryLiveData = new MutableLiveData<>();
  private LiveData<RepoSearchResult> repoResult = Transformations.map(queryLiveData,
      (String input) -> repository.search(input));

  public LiveData<PagedList<Repo>> repos =
      Transformations.switchMap(repoResult, RepoSearchResult::getData);

  public LiveData<String> networkErrors =
      Transformations.switchMap(repoResult, RepoSearchResult::getNetworkErrors);

  public SearchRepositoriesViewModel(GithubRepository repository) {
    this.repository = repository;
  }

  /**
   * Search a repository based on a query string.
   */
  public void searchRepo(String queryString) {
    queryLiveData.postValue(queryString);
  }

  public void listScrolled(int visibleItemCount, int lastVisiblePosition, int totalItemCount) {
    if (visibleItemCount + lastVisiblePosition + VISIBLE_THRESHOLD >= totalItemCount) {
      String immutableQuery = lastQueryValue();
      if (immutableQuery != null) {
        repository.requestMore(immutableQuery);
      }
    }
  }

  @Nullable
  String lastQueryValue() {
    return queryLiveData.getValue();
  }
}
