package com.orobator.helloandroid.stackoverflow.answers;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Objects;

public class AnswersResponse {
  @SerializedName("items")
  public final List<Answer> answers;

  @SerializedName("has_more")
  public final boolean hasMore;

  @SerializedName("quota_max")
  public final int quotaMax;

  @SerializedName("quota_remaining")
  public final int quotaRemaining;

  public AnswersResponse(
      List<Answer> answers, boolean hasMore, int quotaMax, int quotaRemaining) {
    this.answers = answers;
    this.hasMore = hasMore;
    this.quotaMax = quotaMax;
    this.quotaRemaining = quotaRemaining;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AnswersResponse that = (AnswersResponse) o;
    return hasMore == that.hasMore &&
        quotaMax == that.quotaMax &&
        quotaRemaining == that.quotaRemaining &&
        Objects.equals(answers, that.answers);
  }

  @Override public int hashCode() {
    return Objects.hash(answers, hasMore, quotaMax, quotaRemaining);
  }
}
