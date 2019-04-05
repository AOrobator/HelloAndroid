package com.orobator.helloandroid.lesson18.followalong.db;

import android.util.Log;
import androidx.lifecycle.LiveData;
import com.orobator.helloandroid.lesson18.followalong.model.Repo;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Class that handles the DAO local data source. This ensures that methods are
 * triggered on the correct executor.
 */
public class GithubLocalCache {
  private final RepoDao repoDao;
  private final Executor ioExecutor;

  public GithubLocalCache(RepoDao repoDao, Executor ioExecutor) {
    this.repoDao = repoDao;
    this.ioExecutor = ioExecutor;
  }

  /**
   * Insert a list of repos in the database, on a background thread
   */
  public void insert(List<Repo> repos, OnInsertFinishedListener listener) {
    ioExecutor.execute(() -> {
      Log.d("GithubLocalCache", "inserting " + repos.size() + " repos");
      repoDao.insert(repos);
      listener.insertFinished();
    });
  }

  /**
   * Request a LiveData<List<Repo>> from the Dao, based on a repo name. If the
   * name contains multiple words separated by spaces, then we're emulating the
   * Github API behavior and allow any characters between the words.
   *
   * @param name repository name
   */
  public LiveData<List<Repo>> reposByName(String name) {
    // appending '%' so we can allow other characters to be before and after
    // the query string
    String query = "%" + name.replace(' ', '%') + "%";
    return repoDao.reposByName(query);
  }

  public interface OnInsertFinishedListener {
    void insertFinished();
  }
}
