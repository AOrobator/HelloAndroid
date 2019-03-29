package com.orobator.helloandroid.lesson16.lab;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {
  private static final String TAG = MainActivity.class.getSimpleName();
  private static final int PERMISSIONS_REQUEST_CONTACTS = 100;

  @SuppressLint("InlinedApi")
  private static final String[] PROJECTION = { ContactsContract.Contacts.DISPLAY_NAME_PRIMARY };

  // Defines the text expression
  @SuppressLint("InlinedApi")
  private static final String SELECTION =
      ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";
  // Defines a variable for the search string
  private String searchString = "John";
  // Defines the array to hold values that replace the ?
  private String[] selectionArgs = { searchString };

  private RecyclerView contactsRecyclerView;
  private Cursor contactsCursor = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
  }

  @Override protected void onStart() {
    super.onStart();

    if (ContextCompat.checkSelfPermission(this, READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED) {

      // Permission is not granted
      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
          READ_CONTACTS)) {
        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.
      } else {
        // No explanation needed; request the permission
        ActivityCompat.requestPermissions(this,
            new String[] { READ_CONTACTS },
            PERMISSIONS_REQUEST_CONTACTS);

        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
      }
    } else {
      // Permission has already been granted
      Log.d(TAG, "onCreate, permission granted");
      showContacts();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      String permissions[], int[] grantResults) {
    switch (requestCode) {
      case PERMISSIONS_REQUEST_CONTACTS: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // permission was granted, yay! Do the
          // contacts-related task you need to do.
          showContacts();
        } else {
          // permission denied, boo! Disable the
          // functionality that depends on this permission.
        }
        return;
      }
    }
  }

  @Override protected void onStop() {
    super.onStop();

    if (contactsCursor != null) {
      contactsCursor.close();
    }
  }

  private void showContacts() {
    Log.d(TAG, "showContacts()");
    LoaderManager
        .getInstance(this)
        .initLoader(5, null, this);
  }

  @NonNull @Override public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    Log.d(TAG, "onCreateLoader: creating...");
    /*
     * Makes search string into pattern and
     * stores it in the selection array
     */
    selectionArgs[0] = "%" + searchString + "%";
    // Starts the query
    return new CursorLoader(
        this,
        ContactsContract.Contacts.CONTENT_URI,
        PROJECTION,
        SELECTION,
        selectionArgs,
        null
    );
  }

  @Override public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    Log.d(TAG, "onLoadFinished: result count = " + data.getCount());
    contactsCursor = data;
    ContactsAdapter adapter = new ContactsAdapter(data);
    contactsRecyclerView.setAdapter(adapter);
  }

  @Override public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    Log.d(TAG, "onLoaderReset: ");
    if (contactsCursor != null) {
      contactsCursor.close();
    }

    contactsRecyclerView.setAdapter(null);
  }
}
