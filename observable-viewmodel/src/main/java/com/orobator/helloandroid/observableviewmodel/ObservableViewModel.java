package com.orobator.helloandroid.observableviewmodel;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;

/**
 * An ObservableViewModel which implements the Observable interface rather
 * than extending BaseObservable.
 */
public class ObservableViewModel extends ViewModel implements Observable {
  private transient PropertyChangeRegistry callbacks;


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
    synchronized (this) {
      if (callbacks == null) {
        return;
      }
    }

    callbacks.notifyChange(this, property);
  }
}
