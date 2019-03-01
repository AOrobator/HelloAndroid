package com.orobator.helloandroid.lesson7.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.orobator.helloandroid.lesson7.BR;
import com.orobator.helloandroid.lesson7.model.AppSchedulers;
import com.orobator.helloandroid.lesson7.model.api.NumberFact;
import com.orobator.helloandroid.lesson7.model.api.NumbersRepository;
import com.orobator.helloandroid.lesson7.model.connectivity.ConnectionChecker;
import com.orobator.helloandroid.lesson7.model.connectivity.NetworkState;
import com.orobator.helloandroid.observableviewmodel.ObservableViewModel;
import io.reactivex.disposables.Disposable;

import static com.orobator.helloandroid.lesson7.model.connectivity.NetworkState.DISCONNECTED;

public class RetrofitNumberFactViewModel extends ObservableViewModel {
  public String inputNumber = "";

  public String outputFact = "";

  private final MutableLiveData<ViewEvent<NetworkState>> networkStateMutableLiveData =
      new MutableLiveData<>();

  private ConnectionChecker connectionChecker;
  private NumbersRepository numbersRepo;
  private AppSchedulers schedulers;
  private Disposable disposable;

  public void init(
      ConnectionChecker checker,
      NumbersRepository repository,
      AppSchedulers schedulers) {
    connectionChecker = checker;
    numbersRepo = repository;
    this.schedulers = schedulers;
  }

  public void getRandomFact() {
    if (connectionChecker.isConnected()) {
      // Do network call
      Integer number = toIntOrNull(inputNumber);

      if (number != null) {
        disposable = numbersRepo.getTriviaFact(number)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe(this::onGetFactSuccess, this::onGetFactError);
      }
    } else {
      networkStateMutableLiveData.setValue(new ViewEvent(DISCONNECTED));
    }
  }

  @Override protected void onCleared() {
    super.onCleared();

    if (disposable != null) {
      disposable.dispose();
      disposable = null;
    }
  }

  private void updateFact(String fact) {
    outputFact = fact;
    notifyPropertyChanged(BR._all);
  }

  private void onGetFactSuccess(NumberFact fact) {
    updateFact(fact.text);
  }

  private void onGetFactError(Throwable throwable) {
    updateFact(throwable.getMessage());
  }

  public LiveData<ViewEvent<NetworkState>> getNetworkStateLiveData() {
    return networkStateMutableLiveData;
  }

  private @Nullable Integer toIntOrNull(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
