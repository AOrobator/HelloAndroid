package com.orobator.helloandroid.lesson12;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationActivity extends AppCompatActivity {

  private static final String MAIN_CHANNEL_ID = "MainChannel";
  private static final int BASIC_NOTIFICATION_ID = 100;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification);
    Button notifyButton = findViewById(R.id.basicNotifyButton);
    Button buttonNotification = findViewById(R.id.buttonNotification);

    createNotificationChannel();

    notifyButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showBasicNotification();
      }
    });

    buttonNotification.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showButtonNotification();
      }
    });
  }

  private void showButtonNotification() {
    // Pending intent to give the OS something to do when the user taps the Notification
    Intent intent = new Intent(this, NotificationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    Intent actionIntent = new Intent(this, NotificationReceiver.class);
    actionIntent.setAction("Pressed");
    PendingIntent actionPendingIntent = PendingIntent.getBroadcast(this, 0, actionIntent, 0);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MAIN_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Content Title")
        .setContentText("This is the Content Text")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent) // launch this activity when tapped
        .setAutoCancel(true) // clear notification when tapped
        .addAction(android.R.drawable.ic_dialog_info, "Action", actionPendingIntent);
    // post the notification
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
    notificationManager.notify(BASIC_NOTIFICATION_ID, builder.build());
  }

  private void showBasicNotification() {
    // Pending intent to give the OS something to do when the user taps the Notification
    Intent intent = new Intent(this, NotificationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MAIN_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.star_on)
        .setContentTitle("Content Title")
        .setContentText("This is the Basic Content Text")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent) // launch this activity when tapped
        .setAutoCancel(true); // clear notification when tapped

    // post the notification
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
    notificationManager.notify(BASIC_NOTIFICATION_ID, builder.build());
  }

  private void createNotificationChannel() {
    // that's an Oh as in Oreo no a zero
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.main_channel_name);
      String description = getString(R.string.main_channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(MAIN_CHANNEL_ID, name, importance);
      channel.setDescription(description);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
}
