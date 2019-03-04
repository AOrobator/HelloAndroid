package com.orobator.helloandroid.stackoverflow.questions;

import com.orobator.helloandroid.stackoverflow.ApiConstants;
import io.reactivex.Single;

import static com.orobator.helloandroid.stackoverflow.ApiConstants.BODY_FILTER;
import static com.orobator.helloandroid.stackoverflow.ApiConstants.STACK_OVERFLOW_SITE;

public class QuestionsDownloader implements QuestionsRepository {
  private QuestionsApi questionsApi;

  public QuestionsDownloader(QuestionsApi questionsApi) {
    this.questionsApi = questionsApi;
  }

  @Override
  public Single<QuestionsResponse> getQuestions(int page, int pageSize, ApiConstants.Order order,
      ApiConstants.Sort sort) {
    return questionsApi.getQuestions(page, pageSize, order.val, sort.val,
        STACK_OVERFLOW_SITE, BODY_FILTER);
  }
}
