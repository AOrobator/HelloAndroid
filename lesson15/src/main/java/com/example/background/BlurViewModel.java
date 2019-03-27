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
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.background.workers.BlurWorker;

import static com.example.background.Constants.KEY_IMAGE_URI;

public class BlurViewModel extends ViewModel {
  private WorkManager workManager;

  private Uri mImageUri;

  public BlurViewModel() {
    workManager = WorkManager.getInstance();
  }

  /**
   * Create the WorkRequest to apply the blur and save the resulting image
   *
   * @param blurLevel The amount to blur the image
   */
  void applyBlur(int blurLevel) {
    OneTimeWorkRequest blurRequest =
        new OneTimeWorkRequest.Builder(BlurWorker.class)
            .setInputData(createInputDataForUri())
            .build();

    workManager.enqueue(blurRequest);
  }

  private Uri uriOrNull(String uriString) {
    if (!TextUtils.isEmpty(uriString)) {
      return Uri.parse(uriString);
    }
    return null;
  }

  /**
   * Getters
   */
  Uri getImageUri() {
    return mImageUri;
  }

  /**
   * Setters
   */
  void setImageUri(String uri) {
    mImageUri = uriOrNull(uri);
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