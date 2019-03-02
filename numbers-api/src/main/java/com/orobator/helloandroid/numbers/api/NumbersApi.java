package com.orobator.helloandroid.numbers.api;

import com.orobator.helloandroid.numbers.model.NumberFact;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NumbersApi {
  @GET("{number}/trivia?json")
  Single<NumberFact> getTriviaFact(@Path("number") int number);
}
