package com.orobator.helloandroid.stackoverflow.answers;

import io.reactivex.Single;

public interface AnswersRepository {
  Single<AnswersResponse> getAnswersForQuestion(long questionId);
}
