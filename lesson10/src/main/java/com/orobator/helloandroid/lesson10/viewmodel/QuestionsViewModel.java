package com.orobator.helloandroid.lesson10.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.orobator.helloandroid.common.AppSchedulers;
import com.orobator.helloandroid.lesson10.BR;
import com.orobator.helloandroid.observableviewmodel.ObservableViewModel;
import com.orobator.helloandroid.stackoverflow.questions.Question;
import com.orobator.helloandroid.stackoverflow.questions.QuestionsApi.Order;
import com.orobator.helloandroid.stackoverflow.questions.QuestionsApi.Sort;
import com.orobator.helloandroid.stackoverflow.questions.QuestionsRepository;
import com.orobator.helloandroid.stackoverflow.questions.QuestionsResponse;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public class QuestionsViewModel extends ObservableViewModel {
  private final QuestionsRepository questionsRepository;
  private final AppSchedulers schedulers;

  private Disposable disposable;
  private boolean isLoading = false;
  private boolean isEmpty = false;
  private boolean hasError = false;
  private String errorText = "";
  private final MutableLiveData<List<QuestionViewModel>> questionsLiveData =
      new MutableLiveData<>();

  public QuestionsViewModel(
      QuestionsRepository questionsRepository,
      AppSchedulers schedulers) {
    this.questionsRepository = questionsRepository;
    this.schedulers = schedulers;
  }

  public boolean isEmpty() {
    return isEmpty;
  }

  public boolean hasError() {
    return hasError;
  }

  public boolean isLoading() {
    return isLoading;
  }

  public String getErrorText() {
    return errorText;
  }

  public LiveData<List<QuestionViewModel>> questionViewModelLiveData() {
    return questionsLiveData;
  }

  private void updateViewState(boolean isLoading, boolean isEmpty, boolean hasError) {
    this.isLoading = isLoading;
    this.isEmpty = isEmpty;
    this.hasError = hasError;

    notifyPropertyChanged(BR._all);
  }

  public void loadQuestions() {
    disposable = questionsRepository
        .getQuestions(1, 20, Order.DESC, Sort.HOT)
        .subscribeOn(schedulers.io)
        .observeOn(schedulers.main)
        .subscribe(this::onGetQuestionsSuccess, this::onGetQuestionsError);

    updateViewState(true, false, false);
  }

  private void onGetQuestionsSuccess(QuestionsResponse response) {
    List<QuestionViewModel> list = new ArrayList<>();

    for (Question question : response.getItems()) {
      list.add(new QuestionViewModel(question));
    }

    questionsLiveData.setValue(list);

    updateViewState(false, response.getItems().isEmpty(), false);
  }

  private void onGetQuestionsError(Throwable throwable) {
    errorText = throwable.getMessage();
    updateViewState(false, false, true);
  }

  @Override protected void onCleared() {
    super.onCleared();

    if (disposable != null) {
      disposable.dispose();
      disposable = null;
    }
  }
}
