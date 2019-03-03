package com.orobator.helloandroid.stackoverflow.answers;

import com.google.gson.annotations.SerializedName;
import com.orobator.helloandroid.stackoverflow.user.User;
import java.util.Objects;

public class Answer {
  @SerializedName("owner")
  private final User owner;

  @SerializedName("is_accepted")
  private final boolean isAccepted;

  @SerializedName("score")
  private final int score;

  @SerializedName("last_activity_date")
  private final long lastActivityDate;

  @SerializedName("creation_date")
  private final long creationDate;

  @SerializedName("answer_id")
  private final long answerId;

  @SerializedName("question_id")
  private final long questionId;

  @SerializedName("body")
  private final String body;

  public Answer(User owner, boolean isAccepted, int score, long lastActivityDate, long creationDate,
      long answerId, long questionId, String body) {
    this.owner = owner;
    this.isAccepted = isAccepted;
    this.score = score;
    this.lastActivityDate = lastActivityDate;
    this.creationDate = creationDate;
    this.answerId = answerId;
    this.questionId = questionId;
    this.body = body;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Answer answer = (Answer) o;
    return isAccepted == answer.isAccepted &&
        score == answer.score &&
        lastActivityDate == answer.lastActivityDate &&
        creationDate == answer.creationDate &&
        answerId == answer.answerId &&
        questionId == answer.questionId &&
        Objects.equals(owner, answer.owner) &&
        Objects.equals(body, answer.body);
  }

  @Override public int hashCode() {
    return Objects.hash(owner, isAccepted, score, lastActivityDate, creationDate, answerId,
        questionId, body);
  }
}
