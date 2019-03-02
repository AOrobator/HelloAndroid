package com.orobator.helloandroid.numbers.api;

import com.orobator.helloandroid.numbers.model.NumberFact;
import io.reactivex.Single;

public class NumbersRepositoryImpl implements NumbersRepository {
  private final NumbersApi numbersApi;

  public NumbersRepositoryImpl(NumbersApi numbersApi) {
    this.numbersApi = numbersApi;
  }

  @Override public Single<NumberFact> getTriviaFact(int number) {
    return numbersApi.getTriviaFact(number);
  }
}
