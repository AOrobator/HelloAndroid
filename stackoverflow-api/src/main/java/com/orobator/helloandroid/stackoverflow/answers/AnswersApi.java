package com.orobator.helloandroid.stackoverflow.answers;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnswersApi {

  @GET("2.2/questions/{questionId}/answers")
  Single<AnswersResponse> getAnswersForQuestion(
      @Path("questionId") long questionId,
      @Query("order") String order,
      @Query("sort") String sort,
      @Query("site") String site,
      @Query(value = "filter", encoded = true) String filter
  );
}
