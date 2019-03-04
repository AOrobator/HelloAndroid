package com.orobator.helloandroid.lesson10.answers.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.observableviewmodel.ObservableViewModel;
import com.orobator.helloandroid.stackoverflow.answers.Answer;
import com.orobator.helloandroid.stackoverflow.answers.AnswersRepository;
import com.orobator.helloandroid.stackoverflow.answers.AnswersResponse;
import com.orobator.helloandroid.stackoverflow.questions.Question;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public class AnswersViewModel extends ObservableViewModel {
  private final AnswersRepository answersRepository;
  private final AppSchedulers schedulers;
  private Disposable disposable;
  private MutableLiveData<List<AnswerViewModel>> answerViewModelsLiveData = new MutableLiveData<>();

  public AnswersViewModel(
      AnswersRepository answersRepository,
      AppSchedulers schedulers) {
    this.answersRepository = answersRepository;
    this.schedulers = schedulers;
  }

  public void setQuestion(Question question) {
    disposable = answersRepository
        .getAnswersForQuestion(question.getQuestionId())
        .subscribeOn(schedulers.io)
        .observeOn(schedulers.main)
        .subscribe(this::onGetAnswersSuccess, this::onGetAnswersError);
  }

  public LiveData<List<AnswerViewModel>> getAnswerViewModelsLiveData() {
    return answerViewModelsLiveData;
  }

  private void onGetAnswersSuccess(AnswersResponse response) {
    List<AnswerViewModel> answerViewModels = new ArrayList<>(response.answers.size());

    for (Answer answer : response.answers) {
      answerViewModels.add(new AnswerViewModel(answer));
    }

    answerViewModelsLiveData.setValue(answerViewModels);
  }

  private void onGetAnswersError(Throwable throwable) {
    // Ignored for simplicity. See Lesson 9 for error handling
  }

  @Override protected void onCleared() {
    super.onCleared();

    if (disposable != null) {
      disposable.dispose();
      disposable = null;
    }
  }
}
