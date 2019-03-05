# Job Scheduler

- With the Job Scheduler you can tell the OS to call a method in your app periodically
- This can be used to request updates and sync data between local storage and network storage
- The Job Scheduler replaces Alarm Manager and other cron-like facilities tried on Android over the years.
- Job Scheduler's main advantage over these deprecated scheduling systems is that it schedules activities  in ways that can optimize the battery.

## Using JobScheduler

1. Create a Class that extends JobService

   1. In `onStartJob` perform the operation that makes up the job. Use background threads for long-running activities because `onStartJob` is called in the main thread.
   2. Call `jobFInished` when you are don the work. This helps properly account for battery use.
   3. in `onStartJob` return true if the work will continue in the background or false if it is complete
   4. In `onStopJob` handle canceling any background workers.
   5. In `onStopJob` return true if the job should be rescheduled or false if it should be abandoned 

   ```java
   public class DemoJobService extends JobService {
       private static final String TAG = "DemoJobService";
       private boolean isCanceled;
   
       @Override
       public boolean onStartJob(final JobParameters jobParameters) {
           Log.i(TAG, "onStartJob");
           isCanceled = false;
           Thread worker = new Thread(new Runnable() {
               @Override
               public void run() {
                   for (int i = 0; i < 100; i++) {
                       Log.i(TAG, "background work " + i);
                       pause(1000);
                       if (isCanceled) break;
                   }
                   jobFinished(jobParameters, false);
               }
           });
           worker.start();
           return true;
       }
   
       private void pause(int i) {
           try {
               Thread.sleep(i);
           } catch (InterruptedException e) {
               // ignored
           }
       }
   
       @Override
       public boolean onStopJob(JobParameters jobParameters) {
           Log.i(TAG, "onStopJob");
           isCanceled = true;
           return true;
       }
   }
   ```

   

2. Add a `service` entry to the `application` in the AndroidManifest.xml for the JobService subclass defined in step 1.

   ```xml
   <service
               android:name=".DemoJobService"
               android:label="Demo Job Service"
               android:permission="android.permission.BIND_JOB_SERVICE"
               android:exported="true"/>
   ```

   

3. To Schedule the Job, create a `JobInfo` class and use the system service `JobScheduler` to schedule.

   1. JobInfo builder can have many properties set by the `JobInfo.Builder` Some of the most common are listed in the Table below

      | Setter                      | Description                                                  |
      | --------------------------- | ------------------------------------------------------------ |
      | addTriggerContentUrl        | ContentObserver monitors and causes job to execute if changed |
      | setEstimatedNetworkBytes    | Allows the system to prioritize jobs based on estimated network use. It may delay large network jobs when bandwidth is slow to save battery. |
      | setImportantWhileForeground | Setting this indicates that the job is important while the app. The OS will relax doze restrictions. |
      | setMinimumLatency           | For non-periodic. delay for specified time                   |
      | setOverrideDeadline         | For non-periodic. delay no more than specified time          |
      | setPeriodic                 | Execute once per interval. Minimum interval is 15 minutes.   |
      | setPersisted                | Specify if the Job should persist across reboots             |
      | requireNetwork              | If the Job requires network access                           |
      | setRequiredNetworkType      | Specify the Network type (typically  NETWORK_TYPE_UNMETERED or NETWORK_TYPE_NOT_ROAMING) |
      | setRequiresBatteryNotLow    | Only run when there is plenty of juce                        |
      | setRequiresCharging         | Only run when hooked up to the mains                         |
      | setRequiresDeviceIdle       | Only run when no one is using the device                     |

       Additional settings can be found: <https://developer.android.com/reference/android/app/job/JobInfo.Builder.html

      ```java
      private void scheduleJob() {
              JobScheduler jobScheduler = (JobScheduler)
                  getSystemService(Context.JOB_SCHEDULER_SERVICE);
              JobInfo.Builder builder = new JobInfo.Builder(DEMO_JOB_ID, 
                            new ComponentName(this, DemoJobService.class))
                  .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                  .setPeriodic(15*60*1000);
              int result = jobScheduler.schedule(builder.build());
              if (result == JobScheduler.RESULT_SUCCESS) {
                  Log.i(TAG, "Job Scheduled");
              }
              else {
                  Log.w(TAG, "Job Not Scheduled");
              }
          }
      ```

   ## Running the Demo Code

   To see how the Job scheduler works with network type criteria enable Wifi. If running the Emulator, enable Wifi and select "Android" wifi.

   Launch the app and switch to the Debug Console window or filter on the App in Logcat

   Click: Start Job

   Disable Wifi

   Notice that the background work counter stops.

   Start Wifi

   Notice that the background work counter restarts.