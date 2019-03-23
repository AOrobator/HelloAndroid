package com.orobator.helloandroid.lesson13;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class MusicPlayerService extends Service {

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }
}
