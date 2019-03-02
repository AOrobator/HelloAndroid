package com.orobator.helloandroid.numbers.api;

import com.orobator.helloandroid.numbers.model.NumberFact;
import io.reactivex.Single;

public interface NumbersRepository {
  Single<NumberFact> getTriviaFact(int number);
}
