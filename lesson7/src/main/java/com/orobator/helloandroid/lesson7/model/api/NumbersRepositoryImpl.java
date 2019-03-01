package com.orobator.helloandroid.lesson7.model.api;

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
