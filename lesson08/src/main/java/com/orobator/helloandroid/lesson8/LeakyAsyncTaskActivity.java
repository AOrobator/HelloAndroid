package com.orobator.helloandroid.lesson8;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LeakyAsyncTaskActivity extends AppCompatActivity {

  private AsyncTask<Void, Integer, Void> progressAsyncTask;
  private int progressCount = 0;
  private TextView counter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leaky_async_task);

    counter = findViewById(R.id.progressTextView);
    updateProgressAsyncTask();
  }

  private void updateCounter(int count) {
    counter.setText(Integer.toString(count));
  }

  private void updateProgressAsyncTask() {
    // Has implicit reference to Activity
    progressAsyncTask = new AsyncTask<Void, Integer, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        for (int i = 0; i < 100; i++) {
          try {
            Thread.sleep(1000);
            progressCount++;
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          publishProgress(progressCount);
        }
        return null;
      }

      @Override
      protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        updateCounter(values[0]);
      }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }
}
