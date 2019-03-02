package com.orobator.helloandroid.stackoverflow.questions;

import io.reactivex.Single;

public class QuestionsDownloader implements QuestionsRepository {
  private QuestionsApi questionsApi;

  public QuestionsDownloader(QuestionsApi questionsApi) {
    this.questionsApi = questionsApi;
  }

  @Override
  public Single<QuestionsResponse> getQuestions(int page, int pageSize, QuestionsApi.Order order,
      QuestionsApi.Sort sort) {
    return questionsApi.getQuestions(page, pageSize, order.val, sort.val,
        QuestionsApi.STACK_OVERFLOW_SITE);
  }
}
