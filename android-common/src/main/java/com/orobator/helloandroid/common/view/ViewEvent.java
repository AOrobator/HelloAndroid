package com.orobator.helloandroid.common.view;

import androidx.annotation.Nullable;
import java.util.Objects;

public class ViewEvent<T> {
  private T data;

  public ViewEvent(T data) {
    this.data = data;
  }

  public @Nullable T consume() {
    if (data == null) {
      return null;
    } else {
      T tmp = data;
      data = null;
      return tmp;
    }
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ViewEvent<?> viewEvent = (ViewEvent<?>) o;
    return Objects.equals(data, viewEvent.data);
  }

  @Override public int hashCode() {
    return Objects.hash(data);
  }
}
