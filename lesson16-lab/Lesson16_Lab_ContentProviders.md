# Lesson 16 Lab: ContentProviders

In this lab, you'll be searching the Contacts ContentProvider for contacts named John, then 
displaying the results (contact names) in a RecyclerView.

First make sure to add the contacts permission to your manifest.

```xml
<uses-permission android:name="android.permission.READ_CONTACTS"/>
```

After declaring the permission in your manifest, you'll want to ask for this permission in `onStart`
of your Activity.

You'll want to make use of the following constants:

```
ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
ContactsContract.Contacts.CONTENT_URI
```

Make sure to close/clean up your cursor in onStop of your Activity. The results should be displayed 
in a RecyclerView. Do not hardcode the searchString "John" directly into the selection. Instead use 
selectionArgs.