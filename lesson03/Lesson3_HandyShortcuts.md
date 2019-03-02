# Lesson 3: Handy Shortcuts

Keyboard shortcuts will always be faster than using a mouse. Android Studio has too many shortcuts 
to list, and you'll end up memorizing some with repeated use.

[Here](IntelliJIDEA_KeyboardShortcuts.pdf) you'll find a full list of Android Studio/IntelliJ's shortcuts. 

Here are some of my most used shortcuts:

## <kbd>⌃ Control</kbd> + <kbd>R</kbd> : Run

Installs and runs the app or test. 


## <kbd>⌃ Control</kbd> + <kbd>D</kbd> : Debug

Run with the debugger attached.


## <kbd>⌘ Command</kbd> + <kbd>⇧ Shift</kbd> + <kbd>A</kbd> : Invoke Action

I recommend memorizing this command immediately. It is the shortcut to all shortcuts. If you want
to invoke an action (commit, reformat code, etc.) start typing and a list of options will appear.
Some options have the keyboard shortcut next to them so you can invoke those commands directly
next time.

## <kbd>⇧ Shift</kbd> <kbd>⇧ Shift</kbd>: Search Everywhere

This command is similar to Invoke Action, but the scope is much wider. In addition to searching for
actions, double tapping shift will also search for symbols (class names, variable names, resource
names, files). 


## <kbd>⌘ Command</kbd> + <kbd>O</kbd> : Open Class / <kbd>⌘ Command</kbd> + <kbd>⇧ Shift</kbd> + <kbd>O</kbd> : Open File

Other great shortcuts include ⌘+O, which allows you to open classes, and ⌘+⇧+O, which allows you to
open files (including classes). Both of these searches allow for fuzzy search, which means that if
your classes are CamelCasedLikeSo, you don’t have to type out an entire word before typing the 
next word. This allows you to find things much faster than typing out each word at a time. For
example, if I was searching for the class CoffeeGrinderImpl, I could search for “CoGrIm” and find
exactly what I was looking for.

When searching for classes, you can also append your search with “:line number” to go to a
specific line number of that class. Searching for “CoGrIm:42” would take me to the 42nd line of the
class CoffeeGrinderImpl.
  

## <kbd>⌘ Command</kbd> + <kbd>1...N</kbd> : Toggle Tool Window

There are numbered tool windows throughout Android Studio. Pressing ⌘ + the number next to the
tool window will toggle its visibility.

 - ⌘ + 1 : Toggle Project View
 - ⌘ + 6 : Toggle Logcat
 - ⌘ + 9 : Toggle Version Control
 
 
## <kbd>⌥ Option</kbd> + <kbd>↑ Up</kbd> / <kbd>↓ Down</kbd> : Smart Select

This will increasingly or decreasingly select text by scope. For example, if your cursor is in the
middle of a variable and you continuously press ⌥ + ↑, you well select the variable, then the line
it's on, then the containing for loop, then the method body, then the class. Useful for selecting
large blocks of text.


## <kbd>⌘ Command</kbd> + <kbd>⌥ Option</kbd> + <kbd>← Left</kbd> / <kbd>→ Right</kbd> : Navigate Back / Forward

Places your cursor at the previous/next place it was in history.


## <kbd>⌃ Control</kbd> + <kbd>G</kbd> / <kbd>⌃ Control</kbd> + <kbd>⌘ Command</kbd> + <kbd>G</kbd> : Select Next Occurrence / Select All Occurrences

Progressively places multiple cursors at each occurrence of the current selection. Useful for refactoring.


## <kbd>⌥ Option</kbd> + <kbd>⌘ Command</kbd> + <kbd>V</kbd> : Extract Variable

Extracts the current selection into a new variable. 


## Learn shortcuts faster : KeyPromoterX Plugin

KeyPromoterX is a plugin used to help you memorize shortcuts faster. Each time you click an action
with a shortcut, it will remind you of the shortcut for that action. It will also prompt you to 
create a shortcut if you click on a shortcut-less action multiple times. More info can be found 
[here](https://plugins.jetbrains.com/plugin/9792-key-promoter-x). To install, go to 
Preferences > Plugins > Marketplace > KeyPromoterX


## Perform ADB Actions: ADB Idea Plugin

Adds the following ADB commands to Android Studio and Intellij:

 - ADB Uninstall App
 - ADB Kill App
 - ADB Start App
 - ADB Restart App
 - ADB Clear App Data
 - ADB Clear App Data and Restart
 - ADB Revoke Permissions
 - ADB Start App With Debugger
 - ADB Restart App With Debugger
 
All of these can be invoked by ⌘ + ⇧ + A. To install, go to Preferences > Plugins > Marketplace > ADB Idea


## Find and implement code: Exynap Plugin

Exynap is an Android Studio plugin which helps you find and implement the code you require in an 
instant. You only have to formulate a command and press Enter. Exynap has thousands of smart code 
solutions! Read more [here](http://exynap.com/)

To install, go to Preferences > Plugins > Marketplace > Exynap


## Add your own!

Finally, if there isn't a keyboard shortcut available for an action you'd like to invoke, create
your own by going to Preferences > Keymap and add a shortcut.