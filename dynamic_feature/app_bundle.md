# App Bundle

- The Google Play store app bundle feature allows developers to deploy their applications to 
  customers in small increments.
- This requires a change in the deployment procedure vs. the traditional .apk build and sign.
- To create an App Bundle using Android Studio 3.2
  - File -> New -> New Module
  - Select Dynamic Feature Module - Don't worry, we aren't going to create a whole new program. This
    just wraps an existing module.
  - Select the **Base application Module** - The module that you want to be bundled.
  - Specify a Module name, etc.
- Now, when the new App Bundle Module is deployed to Google Play it sends up the code and resources 
  for each Dynamic Feature and lets the Google Play store sign the release.

## App Bundle Format

![App Bundle](app_bundle.png)

The Module is assembled into .aab format and sent to the Google Play store. Additional modules can 
be configured in the dynamic features module. They have the same format as the Base module and are 
included in the .aab file.

When a device downloads the Bundled App from the Play Store, it is provided with only the resources 
and native executable libraries it needs. For example, an hdpi French device will only download 
values-fr and drawable-hdpi resources. By specifying additional dynamic features, entire sets of 
assets and source code can be downloaded independently. This reduces the initial bandwidth burden of
an installation of a game or similar.

