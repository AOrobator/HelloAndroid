package com.orobator.helloandroid.lesson7.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.orobator.helloandroid.lesson7.BR;
import com.orobator.helloandroid.lesson7.model.connectivity.ConnectionChecker;
import com.orobator.helloandroid.lesson7.model.connectivity.NetworkState;
import com.orobator.helloandroid.observableviewmodel.ObservableViewModel;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.orobator.helloandroid.lesson7.model.connectivity.NetworkState.DISCONNECTED;

public class OkHttpNumberFactViewModel extends ObservableViewModel {
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
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
          .url("http://numbersapi.com/" + inputNumber)
          .addHeader("Content-Type", "application/json")
          .build();

      client.newCall(request).enqueue(new Callback() {
        @Override public void onFailure(Call call, IOException e) {
          updateFact(e.getMessage());
        }

        @Override public void onResponse(Call call, Response response) throws IOException {
          if (response.isSuccessful() && response.body() != null) {
            updateFact(response.body().string());
          }
        }
      });

    } else {
      networkStateMutableLiveData.setValue(new ViewEvent(DISCONNECTED));
    }
  }

  private void updateFact(String fact) {
    outputFact = fact;
    notifyPropertyChanged(BR._all);
  }

  public LiveData<ViewEvent<NetworkState>> getNetworkStateLiveData() {
    return networkStateMutableLiveData;
  }
}
