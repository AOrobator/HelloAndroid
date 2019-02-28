package com.orobator.helloandroid.observableviewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;

/**
 * An ObservableViewModel which implements the Observable interface rather
 * than extending BaseObservable.
 */
public class AndroidObservableViewModel extends AndroidViewModel implements Observable {
  private transient PropertyChangeRegistry callbacks;

  public AndroidObservableViewModel(@NonNull Application application) {
    super(application);
  }

  @Override public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    synchronized (this) {
      if (callbacks == null) {
        callbacks = new PropertyChangeRegistry();
      }
    }
    callbacks.add(callback);
  }

  @Override public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    synchronized (this) {
      if (callbacks == null) {
        return;
      }
    }
    callbacks.remove(callback);
  }

  /** Tell View that it needs to update. */
  protected void notifyPropertyChanged(int property) {
    callbacks.notifyChange(this, property);
  }
}
