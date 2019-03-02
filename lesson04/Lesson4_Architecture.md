# Lesson 4: Architecture (MVC, MVP, MVVM)

There are currently three major approaches to architecting your Android apps:

## Model-View-Controller (a.k.a MVC)

![Model View Controller][model-view-controller]

 * **Model**: the data layer, responsible for managing the business logic and handling network or 
database API.

 * **View**: the UI layer — a visualisation of the data from the Model.
 
 * **Controller**: the logic layer, gets notified of the user’s behavior and updates the Model
 as needed.

This is the "default" approach with layout files, Activities/Fragments acting as the controller
and Models used for data and persistence. With this approach, Activities are in charge of
processing the data, executing business logic, and updating the views. Activities act like a
controller in MVC but with some extra responsibilities that should be part of the view. The
problem with this standard architecture is that Activities and Fragments can become quite large and
very difficult to test.

See [MvcTipCalcActivity] for an example.

## Model-View-Presenter (a.k.a MVP)

![Model View Presenter][model-view-presenter]

 * **Model**: the data layer. Responsible for handling the business logic and communication with 
 the network and database layers.
 
 * **View**: the UI layer. Displays the data and notifies the Presenter about user actions.
 
 * **Presenter**: retrieves the data from the Model, applies the UI logic and manages the state of
 the View, decides what to display and reacts to user input notifications from the View.
 
Since the View and the Presenter work closely together, they need to have a reference to one
another. To make the Presenter unit testable with JUnit, the View is abstracted and an interface
for it used. The relationship between the Presenter and its corresponding View is defined in a 
Contract interface class, making the code more readable and the connection between the two easier
to understand.

The Model-View-Controller pattern has two main disadvantages: firstly, the View has a reference to
both the Controller and the Model; and secondly, it does not limit the handling of UI logic to a
single class, this responsibility being shared between the Controller and the View or the Model.
The Model-View-Presenter pattern solves both of these issues by breaking the connection that the
View has with the Model and creating only one class that handles everything related to the
presentation of the View - the Presenter: a single class that is easy to unit test.

See [MvpTipCalcActivity] for an example.

## Model-View-ViewModel (a.k.a MVVM)

![Model View ViewModel][model-view-viewmodel]

 * **View**: informs the ViewModel about the user’s actions
 * **ViewModel**: exposes streams of data relevant to the View
 * **DataModel**: abstracts the data source. The ViewModel works with the DataModel to get and save the data.
 
At a first glance, MVVM seems very similar to the Model-View-Presenter pattern, because both of
them do a great job in abstracting the view’s state and behavior. The Presentation Model abstracts
a View independent from a specific user-interface platform, whereas the MVVM pattern was created to
simplify the event driven programming of user interfaces.

If the MVP pattern meant that the Presenter was telling the View directly what to display, in MVVM,
ViewModel exposes streams of events to which the Views can bind to. Like this, the ViewModel does
not need to hold a reference to the View anymore, like the Presenter is. This also means that all
the interfaces that the MVP pattern requires, are now dropped.

The Views also notify the ViewModel about different actions. Thus, the MVVM pattern supports
two-way data binding between the View and ViewModel and there is a many-to-one relationship between
View and ViewModel. View has a reference to ViewModel but ViewModel has no information about the
View. The consumer of the data should know about the producer, but the producer - the ViewModel -
doesn’t know, and doesn’t care, who consumes the data.

See [MvvmTipCalcActivity] for an example. 

[model-view-controller]: mvc.png "model-view-controller"
[MvcTipCalcActivity]: src/main/java/com/orobator/helloandroid/lesson4/mvc/controller/MvcTipCalcActivity.java

[model-view-presenter]: mvp.png "model-view-presenter"
[MvpTipCalcActivity]: src/main/java/com/orobator/helloandroid/lesson4/mvp/view/MvpTipCalcActivity.java

[model-view-viewmodel]: mvvm.png "model-view-viewmodel"
[MvvmTipCalcActivity]: src/main/java/com/orobator/helloandroid/lesson4/mvvm/view/MvvmTipCalcActivity.java