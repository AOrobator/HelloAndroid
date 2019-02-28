package com.orobator.helloandroid.lesson7.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.orobator.helloandroid.lesson7.model.ConnectionChecker;
import com.orobator.helloandroid.observableviewmodel.ObservableViewModel;

import static com.orobator.helloandroid.lesson7.viewmodel.NumberFactViewModel.NetworkState.DISCONNECTED;

public class NumberFactViewModel extends ObservableViewModel {
  public String inputNumber = "";

  public String outputFact = "";

  private final MutableLiveData<ViewEvent<NetworkState>> networkStateMutableLiveData =
      new MutableLiveData<>();

  private ConnectionChecker connectionChecker = null;

  public void init(ConnectionChecker checker) {
    connectionChecker = checker;
  }

  public void getRandomFact() {
    if (connectionChecker.isConnected()) {
      // Do network call
    } else {
      networkStateMutableLiveData.setValue(new ViewEvent(DISCONNECTED));
    }
  }

  public LiveData<ViewEvent<NetworkState>> getNetworkStateLiveData() {
    return networkStateMutableLiveData;
  }

  public enum NetworkState {
    DISCONNECTED
  }
}
