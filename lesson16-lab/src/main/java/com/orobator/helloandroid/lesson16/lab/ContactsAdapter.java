package com.orobator.helloandroid.lesson16.lab;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

  private final Cursor cursor;

  public ContactsAdapter(@Nullable Cursor cursor) {
    this.cursor = cursor;
  }

  @NonNull @Override
  public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.list_item_contact, parent, false);
    return new ContactsViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
    if (cursor != null) {
      cursor.moveToPosition(position);
      int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
      String name = cursor.getString(nameIndex);
      holder.bind(name);
    }
  }

  @Override public int getItemCount() {
    if (cursor == null) {
      return 0;
    } else {
      return cursor.getCount();
    }
  }

  public static class ContactsViewHolder extends RecyclerView.ViewHolder {
    private final TextView contactsNameTextView;

    public ContactsViewHolder(@NonNull View itemView) {
      super(itemView);

      this.contactsNameTextView = itemView.findViewById(R.id.contactNameTextView);
    }

    public void bind(String name) {
      contactsNameTextView.setText(name);
    }
  }
}
