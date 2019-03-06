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

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
  <item
      android:id="@+id/launchFragmentActivity"
      android:orderInCategory="20"
      android:title="Fragment Activity"
      app:showAsAction="never"/>
</menu>
```

 * `android:id` - specifies id of the menu item
 * `android:orderInCategory` - specifies order of menu item
 * `app:showAsAction` - Indicates when to put this item in the main toolbar with an icon, or show in
   the overflow menu.
 * `android:title` - Title for Menu Item. If showing as an action, this will appear when long 
   pressed.
 * `android:icon` - Icon for menu item. Only appears if showing as an action.

To associate a menu with a given Activity, you'll have to override `onCreateOptionsMenu`:

```java
@Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.questions_menu, menu);
    return true; // return true so menu will show
  }
```

To respond to menu item clicks, override `onOptionsItemSelected`:

```java
@Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.launchFragmentActivity) {
      // handle menu click
    }
    
    return true; // return true to indicate click was consumed
  }
```

For this menu item click, launch an Intent that starts a new Activity, FragmentActivity.

## Fragments

A Fragment represents a behavior or a portion of user interface in an Activity. You can combine 
multiple fragments in a single activity to build a multi-pane UI and reuse a fragment in multiple 
activities. You can think of a fragment as a modular section of an activity, which has its own 
lifecycle, receives its own input events, and which you can add or remove while the activity is 
running (sort of like a "sub activity" that you can reuse in different activities).

A fragment must always be hosted in an activity and the fragment's lifecycle is directly affected by 
the host activity's lifecycle. For example, when the activity is paused, so are all fragments in it, 
and when the activity is destroyed, so are all fragments. However, while an activity is running (it 
is in the resumed lifecycle state), you can manipulate each fragment independently, such as add or 
remove them. When you perform such a fragment transaction, you can also add it to a back stack 
that's managed by the activity—each back stack entry in the activity is a record of the fragment 
transaction that occurred. The back stack allows the user to reverse a fragment transaction 
(navigate backwards), by pressing the Back button.

![Activity Fragment Lifecycle][activity_fragment_lifecycle]

We don't want FragmentActivity to have a Toolbar, as we'll provide it ourselves via the Fragment. To 
prevent `FragmentActivity` from creating a Toolbar, we'll set the theme in the AndroidManifest to 
AppTheme.NoActionBar.

__AndroidManifest.xml__

```xml
<activity
    android:name=".FragmentActivity"
    android:theme="@style/AppTheme.NoActionBar"/>
```

This theme is defined in styles.xml:

```xml
<style name="AppTheme.NoActionBar">
    <item name="windowActionBar">false</item>
    <item name="windowNoTitle">true</item>
</style>
```

Our layout for FragmentActivity will consist of only a FrameLayout. FrameLayout is designed to block
out an area on the screen to display a single item. In this case, the single item will be a 
Fragment.

__activity_fragment.xml__

```xml
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/fragment_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".FragmentActivity"/>
```

Now that we've made our layout for FragmentActivity, let's implement the code for showing a 
fragment.

__FragmentActivity.java__

```java
public class FragmentActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);

    QuestionsFragment fragment = new QuestionsFragment();
    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commit();
  }
}
```
First we create a new instance of QuestionsFragment, which we'll define later. Then we get a 
reference to the FragmentManager. This class allows us to add Fragments to our UI while also 
managing the back stack.

Every time we manipulate how Fragments are displayed, we must do so using a FragmentTransaction. We 
get an instance of FragmentTransaction by calling FragmentManager#beginTransaction. Afterwards, we 
replace whatever is in our fragment container (the FrameLayout) with our QuestionsFragment. When 
we're done, we call commit to execute this FragmentTransaction.

Now we're ready to create our Fragment. Create a new class called QuestionsFragment and have it 
extend androidx.fragment.app.Fragment. This fragment will essentially be a re-implementation of 
QuestionsActivity, so we can reuse the QuestionsViewModel. This Fragment will need to be injected 
with an instance of QuestionsViewModelFactory, so we'll set up dependency injection for our Fragment 
with Dagger.

Let StackOverflowApplication implement the `HasSupportFragmentInjector` interface. The fragment 
injector will be injected by Dagger in onCreate of our Application. Then we'll implement the 
supportFragmentInjector() method from the `HasSupportFragmentInjector` interface, and return the 
fragment injector.

__StackOverflowApplication.java__

```java
public class StackOverflowApplication extends Application implements
    HasSupportFragmentInjector {

  @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;

  @Override public void onCreate() {
    super.onCreate();

    DaggerAppComponent
        .builder()
        .application(this)
        .build()
        .inject(this);
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return fragmentInjector;
  }
}
```

After providing the fragment injector, we'll update our ActivityBindingModule to let Dagger know 
which module provides the dependencies for QuestionsFragment.

```
@ContributesAndroidInjector(modules = QuestionsActivityModule.class)
abstract QuestionsFragment bindQuestionsFragment();
```

Finally, we're ready to implement QuestionsFragment. The first lifecycle method that will get called
in this case is `onAttach`. This is the callback that lets us know that our Fragment is attached to 
our Activity. In this method, we'll inject our fragment to get our dependency on 
QuestionsViewModelFactory. 

```java
@Override public void onAttach(Context context) {
  super.onAttach(context);
  AndroidSupportInjection.inject(this);
}
```

Notice how we're using `AndroidSupportInjection` instead of `AndroidInjection`. This is because the 
fragment we're injecting is not the Framework version (which is deprecated), but the AndroidX 
version.

Next in onCreateView, we use the DatabindingUtil to inflate our view and get our binding. Then we 
return the root of our binding.

```java
private FragmentQuestionsBinding binding;

@Nullable @Override
public View onCreateView(
    @NonNull LayoutInflater inflater, 
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {

  binding = DataBindingUtil.inflate(
      inflater, 
      R.layout.fragment_questions, 
      container, 
      false);
  
  return binding.getRoot();
}
```

After that we'll implement onViewCreated, and we'll use it to initialize our ViewModel and adapters.
This method will be very similar to QuestionActivity's `onCreate`. 

[options_menu]: options_menu.jpg "Options Menu"
[activity_fragment_lifecycle]: activity_fragment_lifecycle.png "Activity Fragment Lifecycle"
