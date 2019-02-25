# Lesson 5: Configuration Changes

Take any of the MV* Activities we created in the previous lesson, calculate the tip, then rotate
the screen. What happens? The device loses all of its state as if the app had just restarted! This
doesn't lead to a very good user experience. We'll definitely fix this, but first we have to
understand what's going on.

A device configuration is a set of system settings that Android uses to render our app. It includes
things such as device orientation, language settings, screen size, and more. Android will use all
the settings in this configuration to make sure our app is laid out appropriately. 

Whenever this configuration changes, (with the most common config change being rotating the device)
Android will destroy the current Activity and recreate it, making sure to use the appropriate
resources for the new configuration.

Now that we have a grasp on what's going on, we can work towards persisting our data across
configuration changes to prevent a UI reset.