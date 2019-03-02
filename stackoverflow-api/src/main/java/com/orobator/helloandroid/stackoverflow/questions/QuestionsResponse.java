package com.orobator.helloandroid.stackoverflow.questions;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Objects;

public class QuestionsResponse {
  @SerializedName("items")
  private List<Question> items;

  @SerializedName("has_more")
  private boolean hasMore;

  @SerializedName("quota_max")
  private int quotaMax;

  @SerializedName("quota_remaining")
  private int quotaRemaining;

  public QuestionsResponse(List<Question> items, boolean hasMore, int quotaMax,
      int quotaRemaining) {
    this.items = items;
    this.hasMore = hasMore;
    this.quotaMax = quotaMax;
    this.quotaRemaining = quotaRemaining;
  }

  public boolean hasMore() {
    return hasMore;
  }

  public int getQuotaMax() {
    return quotaMax;
  }

  public int getQuotaRemaining() {
    return quotaRemaining;
  }

  public List<Question> getItems() {
    return items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    QuestionsResponse that = (QuestionsResponse) o;
    return hasMore == that.hasMore &&
        quotaMax == that.quotaMax &&
        quotaRemaining == that.quotaRemaining &&
        items.equals(that.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, hasMore, quotaMax, quotaRemaining);
  }
}
