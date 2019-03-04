package com.orobator.helloandroid.stackoverflow.answers;

import com.orobator.helloandroid.stackoverflow.ApiConstants;
import io.reactivex.Single;

import static com.orobator.helloandroid.stackoverflow.ApiConstants.BODY_FILTER;

public class AnswersRepositoryImpl implements AnswersRepository {
  private final AnswersApi answersApi;

  public AnswersRepositoryImpl(AnswersApi answersApi) {
    this.answersApi = answersApi;
  }

  @Override public Single<AnswersResponse> getAnswersForQuestion(long questionId) {
    return answersApi.getAnswersForQuestion(
        questionId,
        ApiConstants.Order.DESC.val,
        ApiConstants.Sort.ACTIVITY.val,
        ApiConstants.STACK_OVERFLOW_SITE,
        BODY_FILTER
    );
  }
}
