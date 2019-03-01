package com.orobator.helloandroid.lesson7.model.api;

import io.reactivex.Single;

public interface NumbersRepository {
  Single<NumberFact> getTriviaFact(int number);
}
