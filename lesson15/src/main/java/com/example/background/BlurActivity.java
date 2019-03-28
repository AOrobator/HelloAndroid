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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Data;
import androidx.work.WorkInfo;
import com.bumptech.glide.Glide;

import static com.example.background.Constants.KEY_IMAGE_URI;

public class BlurActivity extends AppCompatActivity {
  private static final String TAG = BlurActivity.class.getSimpleName();

  private BlurViewModel mViewModel;
  private ImageView mImageView;
  private ProgressBar mProgressBar;
  private Button mGoButton, mOutputButton, mCancelButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_blur);

    // Get the ViewModel
    mViewModel = ViewModelProviders.of(this).get(BlurViewModel.class);

    // Get all of the Views
    mImageView = findViewById(R.id.image_view);
    mProgressBar = findViewById(R.id.progress_bar);
    mGoButton = findViewById(R.id.go_button);
    mOutputButton = findViewById(R.id.see_file_button);
    mCancelButton = findViewById(R.id.cancel_button);

    // Image uri should be stored in the ViewModel; put it there then display
    Intent intent = getIntent();
    String imageUriExtra = intent.getStringExtra(KEY_IMAGE_URI);
    mViewModel.setImageUri(imageUriExtra);
    if (mViewModel.getImageUri() != null) {
      Glide.with(this).load(mViewModel.getImageUri()).into(mImageView);
    }

    // Setup blur image file button
    mGoButton.setOnClickListener(view -> mViewModel.applyBlur(getBlurLevel()));

    mViewModel.getSavedWorkInfo().observe(this, listOfWorkInfos -> {
      // If there are no matching work info, do nothing
      if (listOfWorkInfos == null || listOfWorkInfos.isEmpty()) {
        return;
      }

      // We only care about the first output status.
      // Every continuation has only one worker tagged TAG_OUTPUT
      WorkInfo workInfo = listOfWorkInfos.get(0);

      boolean finished = workInfo.getState().isFinished();
      if (!finished) {
        showWorkInProgress();
      } else {
        showWorkFinished();
        Data outputData = workInfo.getOutputData();

        String outputImageUri = outputData.getString(KEY_IMAGE_URI);
        Log.d(TAG, "outputImageUri = " + outputImageUri);

        // If there is an output file show "See File" button
        if (!TextUtils.isEmpty(outputImageUri)) {
          Log.d(TAG, "Setting uri on viewmodel");
          mViewModel.setOutputUri(outputImageUri);
          mOutputButton.setVisibility(View.VISIBLE);
        } else {
          Log.d(TAG, "Got empty uri");
          Toast.makeText(this, "Got empty Uri", Toast.LENGTH_SHORT).show();
        }
      }
    });

    mOutputButton.setOnClickListener(view -> {
      Uri currentUri = mViewModel.getOutputUri();
      if (currentUri != null) {
        Intent actionView = new Intent(Intent.ACTION_VIEW, currentUri);
        if (actionView.resolveActivity(getPackageManager()) != null) {
          startActivity(actionView);
        } else {
          Toast.makeText(this, "Failed to resolve Activity", Toast.LENGTH_SHORT).show();
        }
      } else {
        Toast.makeText(this, "Current Uri is null", Toast.LENGTH_SHORT).show();
      }
    });

    // Hookup the Cancel button
    mCancelButton.setOnClickListener(view -> mViewModel.cancelWork());
  }

  /**
   * Shows and hides views for when the Activity is processing an image
   */
  private void showWorkInProgress() {
    mProgressBar.setVisibility(View.VISIBLE);
    mCancelButton.setVisibility(View.VISIBLE);
    mGoButton.setVisibility(View.GONE);
    mOutputButton.setVisibility(View.GONE);
  }

  /**
   * Shows and hides views for when the Activity is done processing an image
   */
  private void showWorkFinished() {
    mProgressBar.setVisibility(View.GONE);
    mCancelButton.setVisibility(View.GONE);
    mGoButton.setVisibility(View.VISIBLE);
  }

  /**
   * Get the blur level from the radio button as an integer
   *
   * @return Integer representing the amount of times to blur the image
   */
  private int getBlurLevel() {
    RadioGroup radioGroup = findViewById(R.id.radio_blur_group);

    switch (radioGroup.getCheckedRadioButtonId()) {
      case R.id.radio_blur_lv_1:
        return 1;
      case R.id.radio_blur_lv_2:
        return 2;
      case R.id.radio_blur_lv_3:
        return 3;
    }

    return 1;
  }
}