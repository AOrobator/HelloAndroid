package com.orobator.helloandroid.lesson5.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;
import com.orobator.helloandroid.lesson5.BR;

/**
 * An ObservableViewModel which implements the Observable interface rather
 * than extending BaseObservable.
 */
public class ObservableViewModel extends AndroidViewModel implements Observable {
  private transient PropertyChangeRegistry callbacks;

  public ObservableViewModel(@NonNull Application application) {
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
  public void notifyChanged() {
    callbacks.notifyChange(this, BR._all);
  }
}
