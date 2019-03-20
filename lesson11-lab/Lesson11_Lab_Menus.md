# Lesson 11 Lab: Menus

In this lab, we'll be creating a menu with different items and responding to click events 
appropriately.

The first thing we'll want to do is create our menu xml resource file. Create a folder with the path
`main/res/menu`. Once this folder is created, select it and press Command + N. You should see the 
following menu appear:

![new_menu]

Select Menu resource file from this menu and choose lab_menu as a name. Typing "menu" will select 
the option faster than using your mouse. A menu xml resource file will be created for you with 
enclosing menu tags. 

Create a menu item with the following attributes:

```xml
<item
  android:id="@+id/option_archive"
  android:icon="@drawable/ic_archive_24dp"
  android:title="Archive"
  app:showAsAction="ifRoom"/>
```

You'll have to create the icon yourself using the Vector Asset Studio. Select the `drawable` folder 
and press Command + N to open up the "New Menu". Type "vector" and select Vector Asset. Once there,
you should see the following window:

![create_vector_asset]

First click on the clip art to change the icon. You'll want to search for "Archive". Next change the 
icon color to white. Then rename the icon to "ic_archive_24dp".

After this we'll create a second menu item that will only be shown in the overflow menu. Note that 
the `app:showAsAction` attribute is set to never. 

```xml
<item
  android:id="@+id/option_snooze"
  android:title="Snooze Email"
  app:showAsAction="never"/>
```

Your menu preview should look like this:

![lab_menu]

It's important to note that the archive option won't always show up as an action. If we had a lot of 
other actions, in combination with a smaller screen, Android would put the Archive option into the 
overflow menu. Whenever there isn't space for an item to go in the Toolbar, it will be placed in the 
overflow menu.

Now that our menu is created, it's time to show it in the Activity. In order to do this, we must 
override `onCreateOptionsMenu(Menu)` and inflate our menu. Use the following implementation to 
inflate the lab_menu resource:

```java
@Override public boolean onCreateOptionsMenu(Menu menu) {
  MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.lab_menu, menu);
  return true;
}
```

Now run the app. You should see that the menu is visible. If you tap on the 3 dots for the overflow 
menu, a.k.a the shiskabob icon, you should see the Snooze Email option appear. Notice that clicking 
on either of the menu options doesn't do anything because we haven't set that up yet. However, if 
you long press on the "Archive" option, you'll see a Toast that pops up indicating the title of the 
menu option.

![menu_toast]

This lets the user get an idea for what the icon will do, so always provide a title for your menu 
items, even if it's just an icon. Also remember, that the icon may be pushed into the overflow menu, 
so it may not always be represented as an icon.
  

[new_menu]: new_menu.png "New Menu"
[create_vector_asset]: vector_asset_studio.png "Create Vector Asset"
[lab_menu]: lab_menu.png "Menu Preview"
[menu_toast]: menu_toast.png "Menu Label in a Toast"
