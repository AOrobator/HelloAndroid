package com.orobator.helloandroid.numbers.api;

import com.orobator.helloandroid.numbers.model.NumberFact;
import io.reactivex.Single;
import java.util.HashMap;
import java.util.Map;

/** An implementation of NumbersRepository that uses an in-memory cache */
public class CachingNumbersRepository implements NumbersRepository {
  private Map<Integer, NumberFact> cache = new HashMap<>();
  private final NumbersApi numbersApi;

  public CachingNumbersRepository(NumbersApi numbersApi) {
    this.numbersApi = numbersApi;
  }

  @Override public Single<NumberFact> getTriviaFact(int number) {
    // Before we do the network call, first check if the result is cached.
    NumberFact cachedValue = cache.get(number);

    // If the value is in the cache, we can just return it.
    if (cachedValue != null) {
      // We can't return just the NumberFact, so we'll wrap it in a Single
      return Single.fromCallable(() -> cachedValue);
    }

    // The value isn't in the cache, so we'll have to do the network call
    return numbersApi.getTriviaFact(number)
        // When we get the result from the network, put it in the cache
        .doOnSuccess(numberFact -> cache.put(number, numberFact));
  }
}
