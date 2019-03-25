package com.orobator.helloandroid.lesson13;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import java.io.IOException;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {
  private static final String TAG = "MusicPlayerService";
  private static final String ACTION_PLAY = "play";
  private static final String ACTION_PAUSE = "pause";
  private static final String ACTION_STOP = "stop";

  private @Nullable MediaPlayer mediaPlayer = null;

  public static Intent getPlayIntent(Context context) {
    Intent intent = new Intent(context, MusicPlayerService.class);
    intent.setAction(ACTION_PLAY);
    return intent;
  }

  public static Intent getPauseIntent(Context context) {
    Intent intent = new Intent(context, MusicPlayerService.class);
    intent.setAction(ACTION_PAUSE);
    return intent;
  }

  public static Intent getStopIntent(Context context) {
    Intent intent = new Intent(context, MusicPlayerService.class);
    intent.setAction(ACTION_STOP);
    return intent;
  }

  private static PendingIntent getPlayPendingIntent(Context context) {
    Intent intent = getPlayIntent(context);
    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    return pendingIntent;
  }

  private static PendingIntent getPausePendingIntent(Context context) {
    Intent intent = getPauseIntent(context);
    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    return pendingIntent;
  }

  private static PendingIntent getStopPendingIntent(Context context) {
    Intent intent = getStopIntent(context);
    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    return pendingIntent;
  }

  @Override public void onCreate() {
    super.onCreate();

    Log.d(TAG, "onCreate()");
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    String action = intent.getAction();

    Log.d(TAG, "onStartCommand(" + action + ")");
    NotificationManager nm =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    int id = NotificationFactory.NOTIFICATION_ID_PLAYBACK;

    switch (action) {
      case ACTION_PLAY:
        playMusic();
        Notification notification =
            NotificationFactory.getPlaybackNotification(
                this,
                getPlayPendingIntent(this),
                getPausePendingIntent(this),
                getStopPendingIntent(this));
        nm.notify(id, notification);
        startForeground(id, notification);
        break;
      case ACTION_PAUSE:
        pauseMusic();
        notification =
            NotificationFactory.getPlaybackNotification(
                this,
                getPlayPendingIntent(this),
                getPausePendingIntent(this),
                getStopPendingIntent(this));
        startForeground(id, notification);
        break;
      case ACTION_STOP:
        stopMusic();
        stopForeground(true);
        stopSelf();
        break;
      default:
        throw new IllegalArgumentException("Received unknown action: " + action);
    }

    return START_NOT_STICKY;
  }

  private void stopMusic() {
    if (mediaPlayer != null) {
      try {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
      } catch (IllegalStateException e) {
        Log.e(TAG, "Stop failed", e);
      }
    }
  }

  private void pauseMusic() {
    if (mediaPlayer != null) {
      try {
        mediaPlayer.pause();
      } catch (IllegalStateException e) {
        Log.e(TAG, "Pausing failed", e);
      }
    }
  }

  private void playMusic() {
    if (mediaPlayer == null) {
      try {
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.bensound_thejazzpiano);
        if (afd != null) {

          MediaPlayer mp = new MediaPlayer();

          AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
          int audioSessionId = am.generateAudioSessionId();
          mp.setAudioSessionId(audioSessionId);

          mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
          afd.close();
          mp.setOnPreparedListener(this);
          mp.prepareAsync();
          mediaPlayer = mp;
        }
      } catch (IOException ex) {
        Log.d(TAG, "create failed:", ex);
        // fall through
      } catch (IllegalArgumentException ex) {
        Log.d(TAG, "create failed:", ex);
        // fall through
      } catch (SecurityException ex) {
        Log.d(TAG, "create failed:", ex);
        // fall through
      }
    } else {
      try {
        mediaPlayer.start();
      } catch (IllegalStateException e) {
        Log.d(TAG, "play failed", e);
      }
    }
  }

  @Override public void onPrepared(MediaPlayer mp) {
    mp.start();
  }

  @Override public void onDestroy() {
    super.onDestroy();

    Log.d(TAG, "onDestroy()");
  }
}
