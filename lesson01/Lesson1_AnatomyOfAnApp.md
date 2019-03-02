# Lesson 1: Anatomy of an app

### .gradle/

  * Gradle is the official build system for Android development
  * Contains Gradle caches
  * You'll hardly interact with this, unless you have a weird bug and need to clear the cache.
    * One way to delete this cache is to delete this folder. It is auto-generated.
  * .gitignore'd
  
### .idea/

  * Contains IDE specific configurations
    * Code Styles
    * Run Configurations
    * etc.
  * .gitignore'd
  
### gradle/

  * Contains info used for running Gradle
    * gradle-wrapper.jar: Script that invokes a declared version of Gradle, downloading if necessary.
    * gradle-wrapper.properties: Contains info about what version of Gradle to use.
  * Good place to put global gradle scripts (declaring dependencies, common build configs, etc.)
  
### gradle.properties

  * Project wide gradle settings. These get overridden by the IDE.
  
### gradlew & gradlew.bat
  
  * These files are used to invoke Gradle on the command line.
  * Use gradlew on Unix systems, gradlew.bat on Windows.
  * CI systems tend to use this to execute builds.
  * For example:
  
  ```
  ./gradlew testDebug
  ```

### settings.gradle

  * List all modules to be compiled here.
  * An Android app is composed of 1 or more modules.
  * A single `app` module is created by default.
  * Modules are logically related groups of code that have the ability to compile in parallel.
  * Larger apps will want to structure their app using a multi-module architecture to improve build times.
  
### <module_name>.iml

  * Module specific info such as dependencies or language info.
  * Will hardly interact with this file.
  * .gitignore'd
  
### local.properties

  * File used to store Android SDK & NDK location
  * .gitignore'd
  
### build.gradle

  * Top level build.gradle
  * Contains top level build configurations, settings that apply to all modules
    * Gradle plugins
    * Repositories to fetch dependencies from
    
### <module_name>/build.gradle

  * Contains build configurations and dependency info per module.
  
### <module_name>/src/AndroidManifest.xml

  * Where all major components of your app are declared to the Android system
    * Permissions
    * Activities (including launcher Activities)
    * Services
    * BroadcastReceiver
    * NOT Fragments
    
### <module_name>/src/res

  * Non-code resources for your app. Includes layouts, icons, UI strings etc.
  
### <module_name>/src/res/drawable

  * Most icons you see in an app will be drawables.
  * Can take many forms
  * png/mipmaps
    * Image files. Usually a density bucket (mdpi, xhdpi) is required, but not always.
  * Vector Drawables
    * Series of drawing commands, scales to any screen size
    * Android Asset Studio provides a bunch
    * Supported natively on API 21+, use support drawables otherwise.
    * AS can convert SVG into vector drawables
    
### <module_name>/src/res/<res_type>-<resource_qualifier>

  * Resource qualifiers are used to tell the system when/how the resource should be used
  * API
    * -v24, -v21
  * Screen
    * -xhdpi, -mdpi
  * Screen size
    * -sw800
  * Language
    * -en, -es
    
### <module_name>/src/res/values/colors.xml

  * Store color constants for your app here  
  
### <module_name>/src/res/values/strings.xml
 
  * Store UI related strings here
  * This file tends to get very long. It can be broken down into <feature>_strings.xml
  
### <module_name>/src/values/styles.xml

  * App wide themes/styles
  * Can also declare styles to be used
  
### <module_name>/src/values/layout/  
    
  * This is where all the layouts for your app live.
  * Use different layouts depending on your need.
  * Most common will be ConstraintLayout, LinearLayout, FrameLayout
  
### <module_name>/src/main/java/<package_name>

  * This is where the code lives.
  * Main production code goes here.
  
### <module_name>/src/test/java/<package_name>

  * This is where all unit tests should live.
  * These tests are run on the JVM, so shouldn't have any Android dependencies.
  * Most tests should be unit tests.
  * Tend to run very fast.
  
### <module_name>/src/androidTest/java/<package_name>

  * This is where instrumentation tests live.
  * These tests are run on a physical device and tend to be slower.
    