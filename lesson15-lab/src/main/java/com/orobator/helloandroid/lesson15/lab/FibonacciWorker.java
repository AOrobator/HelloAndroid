package com.orobator.helloandroid.lesson15.lab;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * An implementation of {@code androidx.work.Worker} that computes the nth
 * Fibonacci number.
 *
 * See https://en.wikipedia.org/wiki/Fibonacci_number
 */
public class FibonacciWorker extends Worker {
  static final String KEY_NTH_FIB = "nth_fib";

  public static Data makeInputData(int n) {
    return new Data.Builder()
        .putInt(KEY_NTH_FIB, n)
        .build();
  }

  public FibonacciWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull @Override public Result doWork() {
    int n = getInputData().getInt(KEY_NTH_FIB, -1);

    if (n < 0) {
      // No negative numbers allowed
      return Result.failure();
    }

    int result = nthFib(n);

    Data outputData = new Data.Builder()
        .putInt(KEY_NTH_FIB, result)
        .build();

    return Result.success(outputData);
  }

  private int nthFib(int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;

    return nthFib(n - 1) + nthFib(n - 2);
  }
}
