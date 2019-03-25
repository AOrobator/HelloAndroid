package com.orobator.helloandroid.lesson13;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationFactory {
  public static final int NOTIFICATION_ID_PLAYBACK = 10;
  public static final String CHANNEL_ID_PLAYBACK = "playback";

  public static void createNotificationChannel(Context context) {
    // that's an Oh as in Oreo not a zero
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      String channelName = "Music Playback";
      String description = "Control playback";
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel =
          new NotificationChannel(CHANNEL_ID_PLAYBACK, channelName, importance);
      channel.setDescription(description);
      NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  public static Notification getPlaybackNotification(
      Context context,
      PendingIntent playPendingIntent,
      PendingIntent pausePendingIntent,
      PendingIntent stopPendingIntent
  ) {
    return new NotificationCompat.Builder(context, CHANNEL_ID_PLAYBACK)
        .setSmallIcon(android.R.drawable.star_on)
        .setOngoing(true)
        .addAction(android.R.drawable.star_on, "Play", playPendingIntent)
        .addAction(android.R.drawable.star_on, "Pause", pausePendingIntent)
        .addAction(android.R.drawable.star_on, "Stop", stopPendingIntent)
        .setContentTitle("Music Playback")
        .build();
  }

  private NotificationFactory() {
    // No Instances
  }
}
