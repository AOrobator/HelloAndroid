# Lesson 17: Dialogs

A dialog is a small window that prompts the user to make a decision or enter additional information.
A dialog does not fill the screen and is normally used for modal events that require users to take 
an action before they can proceed.

Dialogs disable all app functionality when they appear, and remain on screen until confirmed, 
dismissed, or a required action has been taken.

Dialogs are purposefully interruptive, so they should be used sparingly.

![dialog_example]

## Dialog Types

### Alert Dialog

Alert dialogs interrupt users with urgent information, details, or actions.

![alert_dialog]

### Simple Dialog

Simple dialogs display a list of items that take immediate effect when selected.

![simple_dialog]

### Confirmation Dialog

Confirmation dialogs require users to confirm a choice before the dialog is dismissed.

![confirmation_dialog]

### Full-screen Dialog

Full-screen dialogs fill the entire screen, containing actions that require a series of tasks to 
complete.

![full_screen_dialog]

## Alert Dialog Anatomy

![dialogs_regions]

### 1. Title

This is optional and should be used only when the content area is occupied by a detailed message, a 
list, or custom layout. If you need to state a simple message or question (such as the Alert Dialog 
example), you don't need a title.

### 2. Content area

This can display a message, a list, or other custom layout.

### 3. Action buttons

There should be no more than three action buttons in a dialog. The three action types are:

**Positive** - You should use this to accept and continue with the action (the "OK" action).
  
**Negative** - You should use this to cancel the action.
  
**Neutral** - You should use this when the user may not want to proceed with the action, but doesn't
necessarily want to cancel. It appears between the positive and negative buttons. For example, the 
action might be "Remind me later."

## Implementing Dialogs

The [AlertDialog.Builder] class provides APIs that allow you to create an `AlertDialog` with these 
kinds of content, including a custom layout. Extend [DialogFragment] and create an [AlertDialog] in 
the `onCreateDialog()` callback method.

```java
public class FireMissilesDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_fire_missiles)
               .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
```

[dialog_example]: img/dialog_example.png "Example Dialog"
[alert_dialog]: img/alert_dialog.png "Alert Dialog"
[simple_dialog]: img/simple_dialog.png "Simple Dialog"
[confirmation_dialog]: img/confirmation_dialog.png "Confirmation Dialog"
[full_screen_dialog]: img/full_screen_dialog.png "Full-Screen Dialog"
[DialogFragment]: https://developer.android.com/reference/androidx/fragment/app/DialogFragment?hl=en
[AlertDialog]: https://developer.android.com/reference/android/app/AlertDialog.html
[dialogs_regions]: img/dialogs_regions.png "Anatomy of a Dialog"
[AlertDialog.Builder]: https://developer.android.com/reference/android/app/AlertDialog.Builder.html