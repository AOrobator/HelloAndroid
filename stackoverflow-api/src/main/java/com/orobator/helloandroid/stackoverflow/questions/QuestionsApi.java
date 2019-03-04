package com.orobator.helloandroid.stackoverflow.questions;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuestionsApi {

  @GET("2.2/questions")
  Single<QuestionsResponse> getQuestions(
      @Query("page") int page,
      @Query("pagesize") int pageSize,
      @Query("order") String order,
      @Query("sort") String sort,
      @Query("site") String site,
      @Query("filter") String filter
  );
}
