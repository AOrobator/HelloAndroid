# Android Product Flavors

- Product flavors can be used to build and distribute different versions of your app
- The most common use-case for this is a free vs. paid version. However that is probably better implemented using the in-app purchases.
- Other use-cases are A/B testing, branding, demo versions and admin versions of the app.
- It is also possible to use Product Flavors to support older versions of Android, but that is best handled with the Support library.

## Build Types

- There are two dimensions that will affect the build process. Build Types and Product Flavors.
- Build types are used to control settings and the build process
- Two build types are assigned by default, debug and release.
  - Debug has the debug symbols and is not optimized
  - Release strips symbols and runs Proguard to obfuscate and create translation table and signs the apk with the release
  - Other Build Types might sign the apk but not run Proguard for Beta Testing. Allowing more convenient crash reporting and debugging on trusted devices that (because it's signed) can get the updates through Google Play.
- All build types are automatically assigned to each flavor.
- Use the `buildTypes` block in the `android` section of build.gradle to define the build types.
- IDE Build: Choose in the Build Variants window of the Android Studio
- Command Line Build: Append Product flavor to target in gradlew calls `./gradlew assembleDebug` vs `./gradlew assembleRelease`

## Defining Product Flavors

- Use the `productFlavors` closure in the `android` section to define the flavors in the module's build.gradle.

- Each Flavor must have a dimension which is defined using `productFlavors` and groups them as that dimension.

- Each flavor is defined as a structure inside the `productFlavors` closure

  ```groovy
   flavorDimensions "taste"
      productFlavors {
          chocolate {
              dimension "taste"
          }
          vanilla {
              dimension "taste"
          }
      }
  ```

- Each flavor requires a corresponding source set.

  - By default the source set matches the flavor name and is at the same level as `<ModuleName>/src/main`

  - Each flavor will produce an apk in the corresponding build/output directory.

    ![image-20190303185423322](/Users/geoff/Library/Application Support/typora-user-images/image-20190303185423322.png)

- The `<ModuleName>/src/main` contains the common source and resources

- The Flavor is added to the Build Variant column in the Build Variants window along with the build types.

  ![image-20190303185132000](/Users/geoff/Library/Application Support/typora-user-images/image-20190303185132000.png)

- The flavor and Build type selected in the Build Variant window effects the apk that is built and run when the user hits build.

- The Flavor can be specified as part of the target if building from the command line.

  - `./gradlew :gradleflavors:assembleChocolateDebug` produces
    -  `<ModuleName>/buildoutputs/apk/chocolate/debug/gradleflavors-chocolate-debug.apk`
  - `./gradlew :gradleflavors:assembleDebug` produces 
    - `<ModuleName>/build/outputs/apk/chocolate/debug/gradleflavors-chocolate-debug.apk`
    - `<ModuleName>/build/outputs/apk/vanilla/debug/gradleflavors-vanilla-debug.apk`
  - `./gradlew :gradleflavors:assemble` produces
    - `<ModuleName>/build/outputs/apk/chocolate/debug/gradleflavors-chocolate-debug.apk`
    - `<ModuleName>/build/outputs/apk/vanilla/debug/gradleflavors-vanilla-debug.apk`
    - `<ModuleName>/build/outputs/apk/chocolate/release/gradleflavors-chocolate-release.apk`
    - `<ModuleName>/build/outputs/apk/vanilla/release/gradleflavors-vanilla-release.apk`
  - Building is obviously faster the more specific the target flavor/type combination

  ![image-20190303185038487](/Users/geoff/Library/Application Support/typora-user-images/image-20190303185038487.png) 

  

  