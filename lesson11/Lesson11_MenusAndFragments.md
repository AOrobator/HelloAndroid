# Lesson 11: Menus and Fragments

In this lesson, we'll continue working on our StackOverflow client as we cover the topics of menus 
and Fragments. The options menu is the primary collection of menu items for an activity. It's where 
you should place actions that have a global impact on the app, such as "Search," "Compose email," 
and "Settings." Here's an example of Gmail's Options Menu:

![Options Menu][options_menu]

In our StackOverflow Client, we'll create an options menu in `QuestionsActivity` that will take us 
to `FragmentActivity`. In `FragmentActivity` we'll learn about `Fragments`, which are reusable view 
components very similar to Activities.

## Defining a Menu in XML

For all menu types, Android provides a standard XML format to define menu items. Instead of building
a menu in your activity's code, you should define a menu and all its items in an XML menu resource. 
You can then inflate the menu resource (load it as a Menu object) in your Activity or Fragment.

Using a menu resource is a good practice for a few reasons:

 * It's easier to visualize the menu structure in XML.
 
 * It separates the content for the menu from your application's behavioral code.

 * It allows you to create alternative menu configurations for different platform versions, screen 
   sizes, and other configurations by leveraging the app resources framework.

To define the menu, create an XML file inside your project's res/menu/ directory and build the menu 
with the following elements:

 * `<menu>` - Defines a Menu, which is a container for menu items. A `<menu>` element must be the 
   root node for the file and can hold one or more <item> and <group> elements.
   
 * `<item>` - Creates a MenuItem, which represents a single item in a menu. This element may contain
   a nested `<menu>` element in order to create a submenu.

[options_menu]: options_menu.jpg "Options Menu"