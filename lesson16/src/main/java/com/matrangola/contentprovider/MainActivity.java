package com.matrangola.contentprovider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {
  public static final String TAG = "MainActivity";
  public static final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
  private static String[] PROJECTION = new String[] {
      MediaStore.Audio.AudioColumns.ALBUM,
      MediaStore.Audio.AudioColumns.TITLE
  };
  private static final int MY_PERMISSIONS_REQUEST_ACCESS_MEDIA = 222;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override protected void onStart() {
    super.onStart();

    if (ContextCompat.checkSelfPermission(this, PERMISSION)
        != PackageManager.PERMISSION_GRANTED) {

      // Permission is not granted
      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
          PERMISSION)) {
        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.
      } else {
        // No explanation needed; request the permission
        ActivityCompat.requestPermissions(this,
            new String[] { PERMISSION },
            MY_PERMISSIONS_REQUEST_ACCESS_MEDIA);

        // MY_PERMISSIONS_REQUEST_ACCESS_MEDIA is an
        // app-defined int constant. The callback method gets the
        // result of the request.
      }
    } else {
      // Permission has already been granted
      readMedia();
    }
  }

  private void readMedia() {
    LoaderManager
        .getInstance(this)
        .initLoader(5, null, this);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_ACCESS_MEDIA: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // permission was granted, yay! Do the
          // contacts-related task you need to do.
          readMedia();
        } else {
          // permission denied, boo! Disable the
          // functionality that depends on this permission.
        }
        return;
      }
    }
  }

  @NonNull @Override public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    Uri mediaStoreUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    return new CursorLoader(
        this,
        mediaStoreUri,
        PROJECTION,
        null, // MediaStore.Audio.AudioColumns.TITLE + "LIKE ?"
        null, // new String[] { "%Love%" }
        null);
  }

  @Override public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
    int idxAlbum = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM);
    int idxTitle = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE);

    Log.i(TAG, "Media Rows: " + cursor.getCount());
    while (cursor.moveToNext()) {
      String album = cursor.getString(idxAlbum);
      String title = cursor.getString(idxTitle);
      Log.i(TAG, "Album: " + album + " Title: " + title);
    }

    cursor.close();
  }

  @Override public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    // Release reference to cursor data, by closing or nulling out reference.
  }
}
