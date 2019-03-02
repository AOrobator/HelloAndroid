package com.orobator.helloandroid.stackoverflow.questions;

import io.reactivex.Single;

public interface QuestionsRepository {

  Single<QuestionsResponse> getQuestions(
      int page,
      int pageSize,
      QuestionsApi.Order order,
      QuestionsApi.Sort sort
  );
}