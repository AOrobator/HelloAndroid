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

[model-view-controller]: mvc.png "model-view-controller"
[MvcTipCalcActivity]: src/main/java/com/orobator/helloandroid/lesson4/mvc/controller/MvcTipCalcActivity.java