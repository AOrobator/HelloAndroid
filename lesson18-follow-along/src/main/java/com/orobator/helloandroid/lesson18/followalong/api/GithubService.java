package com.orobator.helloandroid.lesson18.followalong.api;

import android.util.Log;
import com.orobator.helloandroid.lesson18.followalong.model.Repo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Github API communication setup via Retrofit
 */
public interface GithubService {
  String BASE_URL = "https://api.github.com";

  String TAG = "GithubService";
  String IN_QUALIFIER = "in:name,description";

  /**
   * Gets repos ordered by stars.
   */
  @GET("search/repositories?sort=stars")
  Call<RepoSearchResponse> searchRepos(
      @Query("q") String query,
      @Query("page") int page,
      @Query("per_page") int itemsPerPage
  );

  static GithubService create() {
    HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
    logger.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(logger)
        .build();

    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubService.class);
  }

  /**
   * Search the repos based on query.
   * Trigger a request to the Github searchRepo API with the following params:
   *
   * @param query searchRepo keyword
   * @param page request page index
   * @param itemsPerPage number of repositories to be returned by the Github
   * API per page
   *
   * The result of the request is handled by the implementation of the
   * listeners passed as params.
   * @param successListener listener that defines how to handle the list of repos received
   * @param errorListener listener that defines how to handle request failure
   */
  static void searchRepos(
      GithubService service,
      String query,
      int page,
      int itemsPerPage,
      OnSuccessListener successListener,
      OnErrorListener errorListener
  ) {
    Log.d(TAG, "query: " + query + ", page: " + page + ", itemsPerPage: " + itemsPerPage);

    String apiQuery = query + IN_QUALIFIER;

    service.searchRepos(apiQuery, page, itemsPerPage).enqueue(
        new Callback<RepoSearchResponse>() {
          @Override public void onResponse(Call<RepoSearchResponse> call,
              Response<RepoSearchResponse> response) {
            Log.d(TAG, "Received response " + response);
            if (response.isSuccessful()) {
              RepoSearchResponse searchResponse = response.body();
              List<Repo> repos =
                  searchResponse == null ? new ArrayList<>() : searchResponse.getItems();
              successListener.onSuccess(repos);
            } else {
              try {
                ResponseBody errorBody = response.errorBody();
                if (errorBody != null) {
                  String errorMsg = errorBody.string();
                  errorListener.onError(errorMsg == null ? "Unknown error" : errorMsg);
                }
              } catch (IOException e) {
                e.printStackTrace();
                errorListener.onError(e.getMessage() == null ? "Unknown error" : e.getMessage());
              }
            }
          }

          @Override public void onFailure(Call<RepoSearchResponse> call, Throwable t) {
            Log.e(TAG, "Failed to get data");
            String message = t.getMessage();
            errorListener.onError(message == null ? "Unknown error" : message);
          }
        }
    );
  }

  interface OnSuccessListener {
    void onSuccess(List<Repo> repos);
  }

  interface OnErrorListener {
    void onError(String error);
  }
}
