package com.matrangola.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

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
