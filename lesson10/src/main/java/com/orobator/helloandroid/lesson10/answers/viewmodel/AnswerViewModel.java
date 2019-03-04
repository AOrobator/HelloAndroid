package com.orobator.helloandroid.lesson10.answers.viewmodel;

import android.text.Spanned;
import androidx.core.text.HtmlCompat;
import com.orobator.helloandroid.stackoverflow.answers.Answer;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class AnswerViewModel {
  public final Spanned body;
  public final String voteCount;
  public final String userName;
  public final String profilePicUrl;

  public AnswerViewModel(Answer answer) {
    body = HtmlCompat.fromHtml(answer.body, FROM_HTML_MODE_LEGACY);
    voteCount = Integer.toString(answer.score);
    userName = answer.owner.getDisplayName();
    profilePicUrl = answer.owner.getProfileImageUrl();
  }
}
