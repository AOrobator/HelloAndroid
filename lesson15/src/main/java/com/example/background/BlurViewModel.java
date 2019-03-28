/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.background;

import android.net.Uri;
import android.text.TextUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import com.example.background.workers.BlurWorker;
import com.example.background.workers.CleanupWorker;
import com.example.background.workers.SaveImageToFileWorker;
import java.util.List;

import static com.example.background.Constants.IMAGE_MANIPULATION_WORK_NAME;
import static com.example.background.Constants.KEY_IMAGE_URI;
import static com.example.background.Constants.TAG_OUTPUT;

public class BlurViewModel extends ViewModel {
  private WorkManager workManager;
  private LiveData<List<WorkInfo>> savedWorkInfo;
  private Uri mImageUri;
  private Uri mOutputUri;

  public BlurViewModel() {
    workManager = WorkManager.getInstance();
    savedWorkInfo = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT);
  }

  public LiveData<List<WorkInfo>> getSavedWorkInfo() {
    return savedWorkInfo;
  }

  /**
   * Create the WorkRequest to apply the blur and save the resulting image
   *
   * @param blurLevel The amount to blur the image
   */
  void applyBlur(int blurLevel) {

    // Add WorkRequest to Cleanup temporary images
    WorkContinuation continuation =
        workManager
            .beginUniqueWork(IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker.class));

    // Add WorkRequests to blur the image the number of times requested
    for (int i = 0; i < blurLevel; i++) {
      OneTimeWorkRequest.Builder blurBuilder =
          new OneTimeWorkRequest.Builder(BlurWorker.class);

      // Input the Uri if this is the first blur operation
      // After the first blur operation the input will be the output of previous
      // blur operations.
      if (i == 0) {
        blurBuilder.setInputData(createInputDataForUri());
      }

      continuation = continuation.then(blurBuilder.build());
    }

    // Add WorkRequest to save the image to the filesystem
    OneTimeWorkRequest save = new OneTimeWorkRequest.Builder(SaveImageToFileWorker.class)
        .addTag(TAG_OUTPUT) // This adds the tag
        .build();
    continuation = continuation.then(save);

    // Actually start the work
    continuation.enqueue();
  }

  private Uri uriOrNull(String uriString) {
    if (!TextUtils.isEmpty(uriString)) {
      return Uri.parse(uriString);
    }
    return null;
  }

  /**
   * Cancel work using the work's unique name
   */
  void cancelWork() {
    workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME);
  }

  /**
   * Getters
   */
  Uri getImageUri() {
    return mImageUri;
  }

  Uri getOutputUri() {
    return mOutputUri;
  }

  /**
   * Setters
   */
  void setImageUri(String uri) {
    mImageUri = uriOrNull(uri);
  }

  void setOutputUri(String outputImageUri) {
    mOutputUri = uriOrNull(outputImageUri);
  }

  /**
   * Creates the input data bundle which includes the Uri to operate on
   *
   * @return Data which contains the Image Uri as a String
   */
  private Data createInputDataForUri() {
    Data.Builder builder = new Data.Builder();
    if (mImageUri != null) {
      builder.putString(KEY_IMAGE_URI, mImageUri.toString());
    }
    return builder.build();
  }
}