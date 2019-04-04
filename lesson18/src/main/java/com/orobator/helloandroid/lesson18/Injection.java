package com.orobator.helloandroid.lesson18;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.orobator.helloandroid.lesson18.api.GithubService;
import com.orobator.helloandroid.lesson18.data.GithubRepository;
import com.orobator.helloandroid.lesson18.db.GithubLocalCache;
import com.orobator.helloandroid.lesson18.db.RepoDatabase;
import com.orobator.helloandroid.lesson18.ui.ViewModelFactory;
import java.util.concurrent.Executors;

/**
 * Class that handles object creation. Like this, objects can be passes as
 * parameters in the constructors and then replaced for testing, where needed.
 */
public class Injection {
  private static Injection injection = null;

  private Injection() {
    // Singleton instance
  }

  private static void lazyInit() {
    synchronized (Injection.class) {
      if (injection == null) {
        injection = new Injection();
      }
    }
  }

  /**
   * Creates an instance of {@link GithubLocalCache} based on the database DAO
   */
  private GithubLocalCache provideCache(Context context) {
    RepoDatabase database = RepoDatabase.getInstance(context);
    return new GithubLocalCache(database.reposDao(), Executors.newSingleThreadExecutor());
  }

  /**
   * Creates an instance of {@link GithubRepository} based on the
   * {@link GithubService} and a {@link GithubLocalCache}
   */
  private GithubRepository provideGithubRepository(Context context) {
    return new GithubRepository(GithubService.create(), provideCache(context));
  }

  /**
   * Provides the {@link ViewModelProvider.Factory} that is then used to get a
   * reference to {@link ViewModel} objects.
   */
  private ViewModelProvider.Factory sProvideViewModelFactory(Context context) {
    return new ViewModelFactory(provideGithubRepository(context));
  }

  public static ViewModelProvider.Factory provideViewModelFactory(Context context) {
    lazyInit();
    return injection.sProvideViewModelFactory(context);
  }
}
