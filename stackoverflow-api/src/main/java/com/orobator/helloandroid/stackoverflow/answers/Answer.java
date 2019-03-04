package com.orobator.helloandroid.stackoverflow.answers;

import com.google.gson.annotations.SerializedName;
import com.orobator.helloandroid.stackoverflow.user.User;
import java.util.Objects;

public class Answer {
  @SerializedName("owner")
  public final User owner;

  @SerializedName("is_accepted")
  public final boolean isAccepted;

  @SerializedName("score")
  public final int score;

  @SerializedName("last_activity_date")
  public final long lastActivityDate;

  @SerializedName("creation_date")
  public final long creationDate;

  @SerializedName("answer_id")
  public final long answerId;

  @SerializedName("question_id")
  public final long questionId;

  @SerializedName("body")
  public final String body;

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
