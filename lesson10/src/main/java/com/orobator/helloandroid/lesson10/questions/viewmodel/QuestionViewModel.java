package com.orobator.helloandroid.lesson10.questions.viewmodel;

import android.text.Spanned;
import androidx.core.text.HtmlCompat;
import com.orobator.helloandroid.stackoverflow.questions.Question;
import java.util.List;
import java.util.Objects;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class QuestionViewModel {
  public final String title;
  public final String voteCount;
  public final List<String> tags;
  public final Spanned body;
  public final String userName;

  public QuestionViewModel(Question question) {
    this.title = question.getTitle();
    this.voteCount = "" + question.getScore();
    this.tags = question.getTags();
    this.body = HtmlCompat.fromHtml(question.getBody(), FROM_HTML_MODE_LEGACY);
    this.userName = question.getOwner().getDisplayName();
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
