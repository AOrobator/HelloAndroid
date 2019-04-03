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
The FireMissilesDialogFragment produces the following dialog:

![fire_missiles_dialog]

### List Dialog
To create a list dialog, first create an array in `res/values/strings.xml`

```xml
<resources>
  <array name="colors_array">
    <item>Red</item>
    <item>Green</item>
    <item>Blue</item>
  </array>
</resources>
```

Then call `setItems()` on the `AlertDialog.Builder` with the array. Alternatively, you can call 
`setAdapter` and provide a [ListAdapter] to have dynamic items. 

```java
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.pick_color)
           .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
               // The 'which' argument contains the index position
               // of the selected item
           }
    });
    return builder.create();
}
```
The above code would create the following Dialog:

![dialog_list]

### Custom View Dialog

![dialog_custom]

If you want a custom layout in a dialog, create a layout and add it to an `AlertDialog` by calling 
`setView()` on your `AlertDialog.Builder` object.

By default, the custom layout fills the dialog window, but you can still use `AlertDialog.Builder` 
methods to add buttons and a title.

To inflate the layout in your `DialogFragment`, get a `LayoutInflater` with `getLayoutInflater()` 
and call `inflate()`, where the first parameter is the layout resource ID and the second parameter 
is a parent view for the layout. You can then call `setView()` to place the layout in the dialog.

```java
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Get the layout inflater
    LayoutInflater inflater = requireActivity().getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because it's going in the dialog layout
    builder.setView(inflater.inflate(R.layout.dialog_signin, null))
    // Add action buttons
           .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                   // sign in the user ...
               }
           })
           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   LoginDialogFragment.this.getDialog().cancel();
               }
           });
    return builder.create();
}
```

## Showing a Dialog

When you want to show your dialog, create an instance of your `DialogFragment` and call `show()`, 
passing the [FragmentManager] and a tag name for the dialog fragment.

You can get the `FragmentManager` by calling `getSupportFragmentManager()` from the 
`FragmentActivity`. For example:

```java
public void confirmFireMissiles() {
    DialogFragment newFragment = new FireMissilesDialogFragment();
    newFragment.show(getSupportFragmentManager(), "missiles");
}
```

The second argument, "missiles", is a unique tag name that the system uses to save and restore the 
fragment state when necessary. The tag also allows you to get a handle to the fragment by calling 
`findFragmentByTag()`.

## Passing Events Back to the Dialog's Host

When the user touches one of the dialog's action buttons or selects an item from its list, your 
`DialogFragment` might perform the necessary action itself, but often you'll want to deliver the 
event to the activity or fragment that opened the dialog. To do this, define an interface with a 
method for each type of click event. Then implement that interface in the host component that will 
receive the action events from the dialog.

For example, here's a `DialogFragment` that defines an interface through which it delivers the 
events back to the host activity:

```java
public class NoticeDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
```

It's also possible to send events back to the host activity through the `ViewModel`. In that case, you
would get the appropriate `ViewModel` and call `viewModel.onNegativeButtonClicked()` and 
`viewModel.onPositiveButtonClicked()`.

## Dialog Lab

Create an Activity with a single button with the text "Show Dialog". When this button is clicked, 
show a `DialogFragment` named `NameDialogFragment`. Create a dialog with a custom view. This view 
should have an `EditText` with a hint of "Name". The `Dialog` should have a title of "Enter Name". 
Use an interface to forward events back to the host Activity.

When the positive button is clicked, show a `Toast` displaying the input name. When the negative 
button is clicked, show a Toast saying the negative button was clicked. 

[dialog_example]: img/dialog_example.png "Example Dialog"
[alert_dialog]: img/alert_dialog.png "Alert Dialog"
[simple_dialog]: img/simple_dialog.png "Simple Dialog"
[confirmation_dialog]: img/confirmation_dialog.png "Confirmation Dialog"
[full_screen_dialog]: img/full_screen_dialog.png "Full-Screen Dialog"
[DialogFragment]: https://developer.android.com/reference/androidx/fragment/app/DialogFragment?hl=en
[AlertDialog]: https://developer.android.com/reference/android/app/AlertDialog.html
[dialogs_regions]: img/dialogs_regions.png "Anatomy of a Dialog"
[AlertDialog.Builder]: https://developer.android.com/reference/android/app/AlertDialog.Builder.html
[fire_missiles_dialog]: img/fire_missiles_dialog.png "FireMissilesDialog"
[dialog_list]: img/dialog_list.png
[ListAdapter]: https://developer.android.com/reference/android/widget/ListAdapter.html
[dialog_custom]: img/dialog_custom.png "Dialog with custom layout"
[FragmentManager]: https://developer.android.com/reference/androidx/fragment/app/FragmentManager?hl=en