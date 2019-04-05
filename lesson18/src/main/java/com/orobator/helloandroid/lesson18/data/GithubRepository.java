package com.orobator.helloandroid.lesson18.data;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.orobator.helloandroid.lesson18.api.GithubService;
import com.orobator.helloandroid.lesson18.db.GithubLocalCache;
import com.orobator.helloandroid.lesson18.model.Repo;
import com.orobator.helloandroid.lesson18.model.RepoSearchResult;

/**
 * Repository class that works with local and remote data sources.
 */
public class GithubRepository {
  private static final int DATABASE_PAGE_SIZE = 20;

  private final GithubService service;
  private final GithubLocalCache cache;

  public GithubRepository(GithubService service, GithubLocalCache cache) {
    this.service = service;
    this.cache = cache;
  }

  /**
   * Search repositories whose names match the query.
   */
  public RepoSearchResult search(String query) {
    Log.d("GithubRepository", "New query: " + query);

    // Get data source factory from the local cache
    DataSource.Factory<Integer, Repo> dataSourceFactory = cache.reposByName(query);

    // Construct the boundary callback
    RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(query, service, cache);
    LiveData<String> networkErrors = boundaryCallback.getNetworkErrors();

    // Get the paged list
    LiveData<PagedList<Repo>> data =
        new LivePagedListBuilder<>(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build();

    // Get the network errors exposed by the boundary callback
    return new RepoSearchResult(data, networkErrors);
  }

}
