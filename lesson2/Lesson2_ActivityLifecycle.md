# Lesson 2: The Activity Lifecycle

### Managing dependencies across modules

Before we dive into the Activity lifecycle, we want to fix a potential problem. Currently we have 2
apps that declare their own dependencies with separate constants. Since each module declares its
own constants, we could potentially have 2 modules that use different versions of the same
dependency. In order to prevent this from happening, we'll refactor the build constants into a
central location under `gradle/`.

gradle/build-config.gradle contains SDK versions and the app version name and code.

gradle/dependencies.gradle contains app level external dependencies.

We'll use `apply from: rootProject.file("gradle/<file_name>.gradle")` to bring in these constants
to our app modules.

By declaring our external dependencies centrally and bringing them into each module, we guarantee
that each module is using the same version of a dependency as all other modules.
  