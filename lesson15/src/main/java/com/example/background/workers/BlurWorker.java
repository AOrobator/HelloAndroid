package com.example.background.workers;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.background.Constants;

public class BlurWorker extends Worker {
  private static final String TAG = BlurWorker.class.getSimpleName();

  public BlurWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Worker.Result doWork() {

    Context applicationContext = getApplicationContext();

    String resourceUri = getInputData().getString(Constants.KEY_IMAGE_URI);

    try {

      if (TextUtils.isEmpty(resourceUri)) {
        Log.e(TAG, "Invalid input uri");
        throw new IllegalArgumentException("Invalid input uri");
      }

      ContentResolver resolver = applicationContext.getContentResolver();
      // Create a bitmap
      Bitmap picture = BitmapFactory.decodeStream(
          resolver.openInputStream(Uri.parse(resourceUri)));

      // Blur the bitmap
      Bitmap output = WorkerUtils.blurBitmap(picture, applicationContext);

      // Write bitmap to a temp file
      Uri outputUri = WorkerUtils.writeBitmapToFile(applicationContext, output);

      WorkerUtils.makeStatusNotification("Output is "
          + outputUri.toString(), applicationContext);

      // If there were no errors, return SUCCESS
      Data outputData = new Data.Builder()
          .putString(Constants.KEY_IMAGE_URI, outputUri.toString())
          .build();
      return Result.success(outputData);
    } catch (Throwable throwable) {

      // Technically WorkManager will return Result.failure()
      // but it's best to be explicit about it.
      // Thus if there were errors, we return FAILURE
      Log.e(TAG, "Error applying blur", throwable);
      return Result.failure();
    }
  }
}
