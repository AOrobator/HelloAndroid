package com.orobator.helloandroid.stackoverflow.questions;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.orobator.helloandroid.stackoverflow.user.User;
import io.reactivex.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class Question implements Parcelable {
  @SerializedName("tags")
  private final List<String> tags;

  @SerializedName("owner")
  private final User owner;

  @SerializedName("is_answered")
  private final boolean isAnswered;

  @SerializedName("view_count")
  private final int viewCount;

  @SerializedName("answer_count")
  private final int answerCount;

  @SerializedName("accepted_answer_id")
  @Nullable
  private final Long acceptedAnswerId;

  @SerializedName("score")
  private final int score;

  @SerializedName("last_activity_date")
  private final long lastActivityDate;

  @SerializedName("creation_date")
  private final long creationDate;

  @SerializedName("last_edit_date")
  @Nullable
  private final Long lastEditDate;

  @SerializedName("question_id")
  private final long questionId;

  @SerializedName("link")
  private final String link;

  @SerializedName("title")
  private final String title;

  @SerializedName("body")
  private final String body;

  public Question(List<String> tags, User owner, boolean isAnswered, int viewCount, int answerCount,
      Long acceptedAnswerId, int score, long lastActivityDate, long creationDate,
      Long lastEditDate, long questionId, String link, String title, String body) {
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
    this.body = body;
  }

  protected Question(Parcel in) {
    tags = in.createStringArrayList();
    owner = in.readParcelable(User.class.getClassLoader());
    isAnswered = in.readByte() != 0;
    viewCount = in.readInt();
    answerCount = in.readInt();
    if (in.readByte() == 0) {
      acceptedAnswerId = null;
    } else {
      acceptedAnswerId = in.readLong();
    }
    score = in.readInt();
    lastActivityDate = in.readLong();
    creationDate = in.readLong();
    if (in.readByte() == 0) {
      lastEditDate = null;
    } else {
      lastEditDate = in.readLong();
    }
    questionId = in.readLong();
    link = in.readString();
    title = in.readString();
    body = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeStringList(tags);
    dest.writeParcelable(owner, flags);
    dest.writeByte((byte) (isAnswered ? 1 : 0));
    dest.writeInt(viewCount);
    dest.writeInt(answerCount);
    if (acceptedAnswerId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(acceptedAnswerId);
    }
    dest.writeInt(score);
    dest.writeLong(lastActivityDate);
    dest.writeLong(creationDate);
    if (lastEditDate == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(lastEditDate);
    }
    dest.writeLong(questionId);
    dest.writeString(link);
    dest.writeString(title);
    dest.writeString(body);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Question> CREATOR = new Creator<Question>() {
    @Override
    public Question createFromParcel(Parcel in) {
      return new Question(in);
    }

    @Override
    public Question[] newArray(int size) {
      return new Question[size];
    }
  };

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

  @Override public boolean equals(Object o) {
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
        Objects.equals(title, question.title) &&
        Objects.equals(body, question.body);
  }

  @Override public int hashCode() {
    return Objects.hash(tags, owner, isAnswered, viewCount, answerCount, acceptedAnswerId, score,
        lastActivityDate, creationDate, lastEditDate, questionId, link, title, body);
  }
}
