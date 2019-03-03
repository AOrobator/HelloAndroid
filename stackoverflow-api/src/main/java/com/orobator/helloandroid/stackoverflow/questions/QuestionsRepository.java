package com.orobator.helloandroid.stackoverflow.questions;

import com.orobator.helloandroid.stackoverflow.ApiConstants;
import io.reactivex.Single;

public interface QuestionsRepository {

  Single<QuestionsResponse> getQuestions(
      int page,
      int pageSize,
      ApiConstants.Order order,
      ApiConstants.Sort sort
  );
}