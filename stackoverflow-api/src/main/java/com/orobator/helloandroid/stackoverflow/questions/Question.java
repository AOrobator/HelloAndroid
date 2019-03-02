package com.orobator.helloandroid.stackoverflow.questions;

import com.google.gson.annotations.SerializedName;
import com.orobator.helloandroid.stackoverflow.user.User;
import io.reactivex.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class Question {
  @SerializedName("tags")
  private List<String> tags;

  @SerializedName("owner")
  private User owner;

  @SerializedName("is_answered")
  private boolean isAnswered;

  @SerializedName("view_count")
  private int viewCount;

  @SerializedName("answer_count")
  private int answerCount;

  @SerializedName("accepted_answer_id")
  @Nullable
  private Long acceptedAnswerId;

  @SerializedName("score")
  private int score;

  @SerializedName("last_activity_date")
  private long lastActivityDate;

  @SerializedName("creation_date")
  private long creationDate;

  @SerializedName("last_edit_date")
  @Nullable
  private Long lastEditDate;

  @SerializedName("question_id")
  private long questionId;

  @SerializedName("link")
  private String link;

  @SerializedName("title")
  private String title;

  public Question(List<String> tags, User owner, boolean isAnswered, int viewCount,
      int answerCount,
      Long acceptedAnswerId, int score, long lastActivityDate, long creationDate,
      Long lastEditDate, long questionId, String link, String title) {
    this.tags = tags;
    this.owner = owner;
    this.isAnswered = isAnswered;
    this.viewCount = viewCount;
    this.answerCount = answerCount;
    this.acceptedAnswerId = acceptedAnswerId;
    this.score = score;
    this.lastActivityDate = lastActivityDate;
    this.creationDate = creationDate;
    this.lastEditDate = lastEditDate;
    this.questionId = questionId;
    this.link = link;
    this.title = title;
  }

  public List<String> getTags() {
    return tags;
  }

  public User getOwner() {
    return owner;
  }

  public boolean isAnswered() {
    return isAnswered;
  }

  public int getViewCount() {
    return viewCount;
  }

  public int getAnswerCount() {
    return answerCount;
  }

  public Long getAcceptedAnswerId() {
    return acceptedAnswerId;
  }

  public int getScore() {
    return score;
  }

  public long getLastActivityDate() {
    return lastActivityDate;
  }

  public long getCreationDate() {
    return creationDate;
  }

  public long getLastEditDate() {
    return lastEditDate;
  }

  public long getQuestionId() {
    return questionId;
  }

  public String getLink() {
    return link;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Question question = (Question) o;
    return isAnswered == question.isAnswered &&
        viewCount == question.viewCount &&
        answerCount == question.answerCount &&
        score == question.score &&
        lastActivityDate == question.lastActivityDate &&
        creationDate == question.creationDate &&
        questionId == question.questionId &&
        Objects.equals(tags, question.tags) &&
        Objects.equals(owner, question.owner) &&
        Objects.equals(acceptedAnswerId, question.acceptedAnswerId) &&
        Objects.equals(lastEditDate, question.lastEditDate) &&
        Objects.equals(link, question.link) &&
        Objects.equals(title, question.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tags, owner, isAnswered, viewCount, answerCount, acceptedAnswerId,
        score,
        lastActivityDate, creationDate, lastEditDate, questionId, link, title);
  }
}
