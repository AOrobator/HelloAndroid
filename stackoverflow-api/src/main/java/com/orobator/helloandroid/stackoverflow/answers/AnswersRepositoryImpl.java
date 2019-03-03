package com.orobator.helloandroid.stackoverflow.answers;

import com.orobator.helloandroid.stackoverflow.ApiConstants;
import io.reactivex.Single;

public class AnswersRepositoryImpl implements AnswersRepository {
  private static final String getAnswerBodyFilter = "!9Z(-wzu0T";
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
        getAnswerBodyFilter
    );
  }
}
