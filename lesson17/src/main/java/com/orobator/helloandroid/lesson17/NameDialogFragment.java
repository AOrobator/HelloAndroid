package com.orobator.helloandroid.lesson17;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class NameDialogFragment extends DialogFragment {

  public interface NameDialogListener {
    void onNameEntered(String name);

    void onNegativeButtonClicked();
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
    // Get the layout inflater
    LayoutInflater inflater = requireActivity().getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because it's going in the dialog layout
    View view = inflater.inflate(R.layout.dialog_name, null);
    final EditText nameEditText = view.findViewById(R.id.nameEditText);
    final NameDialogListener listener = (NameDialogListener) requireActivity();
    builder.setView(view)
        .setTitle("Enter Name")
        // Add action buttons
        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            String name = nameEditText.getText().toString();
            listener.onNameEntered(name);
          }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            listener.onNegativeButtonClicked();
          }
        });
    return builder.create();
  }
}
