package com.example.background.workers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.background.Constants;
import java.io.File;

public class CleanupWorker extends Worker {
  public CleanupWorker(
      @NonNull Context appContext,
      @NonNull WorkerParameters workerParams) {
    super(appContext, workerParams);
  }

  private static final String TAG = CleanupWorker.class.getSimpleName();

  @NonNull
  @Override
  public Worker.Result doWork() {
    Context applicationContext = getApplicationContext();

    try {
      File outputDirectory = new File(applicationContext.getFilesDir(),
          Constants.OUTPUT_PATH);
      if (outputDirectory.exists()) {
        File[] entries = outputDirectory.listFiles();
        if (entries != null && entries.length > 0) {
          for (File entry : entries) {
            String name = entry.getName();
            if (!TextUtils.isEmpty(name) && name.endsWith(".png")) {
              boolean deleted = entry.delete();
              Log.i(TAG, String.format("Deleted %s - %s",
                  name, deleted));
            }
          }
        }
      }

      return Worker.Result.success();
    } catch (Exception exception) {
      Log.e(TAG, "Error cleaning up", exception);
      return Worker.Result.failure();
    }
  }
}
