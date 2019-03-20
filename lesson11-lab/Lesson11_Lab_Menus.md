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


[new_menu]: new_menu.png "New Menu"
[create_vector_asset]: vector_asset_studio.png "Create Vector Asset"
[lab_menu]: lab_menu.png "Menu Preview"