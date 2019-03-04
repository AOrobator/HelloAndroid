package com.orobator.helloandroid.lesson10.questions.viewmodel;

import com.orobator.helloandroid.stackoverflow.questions.Question;
import java.util.List;
import java.util.Objects;

public class QuestionViewModel {
  public final String title;
  public final String voteCount;
  public final List<String> tags;

  public QuestionViewModel(String title, int voteCount, List<String> tags) {
    this.title = title;
    this.voteCount = Integer.toString(voteCount);
    this.tags = tags;
  }

  public QuestionViewModel(Question question) {
    this.title = question.getTitle();
    this.voteCount = "" + question.getScore();
    this.tags = question.getTags();
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    QuestionViewModel that = (QuestionViewModel) o;
    return Objects.equals(title, that.title) &&
        Objects.equals(voteCount, that.voteCount) &&
        Objects.equals(tags, that.tags);
  }

  @Override public int hashCode() {
    return Objects.hash(title, voteCount, tags);
  }
}
