package com.orobator.helloandroid.lesson15.lab

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.SUCCEEDED
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FibonacciWorkerTest {
  @Before
  fun setup() {
    // First configure WorkManager to work for tests
    val context =
      InstrumentationRegistry
          .getInstrumentation()
          .targetContext

    val config = Configuration.Builder()
        // Set log level to Log.DEBUG to make it easier to debug
        .setMinimumLoggingLevel(Log.DEBUG)
        // Use a SynchronousExecutor here to make it easier to write tests
        .setExecutor(SynchronousExecutor())
        .build()

    // Initialize WorkManager for instrumentation tests.
    WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
  }

  @Test
  fun whenPositiveNumberGiven_WorkerComputesResult() {
    // Define input data
    val inputData = FibonacciWorker.makeInputData(3)

    // Define constraints
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiresDeviceIdle(true)
        .build()

    // Create request
    val request = OneTimeWorkRequest.Builder(FibonacciWorker::class.java)
        .setInputData(inputData)
        .setConstraints(constraints)
        .build()

    val workManager = WorkManager.getInstance()
    val testDriver = WorkManagerTestInitHelper.getTestDriver()

    // Enqueue and wait for result.
    workManager.enqueue(request)
        .result.get()

    // Use TestDriver to fake constraints being met
    testDriver.setAllConstraintsMet(request.id)

    // Get WorkInfo and outputData
    val workInfo = workManager.getWorkInfoById(request.id)
        .get()
    val outputData = workInfo.outputData

    workInfo.state shouldEqual SUCCEEDED

    val nthFib = outputData.getInt(FibonacciWorker.KEY_NTH_FIB, -1)
    nthFib shouldEqual 2
  }

  @Test
  fun whenNegativeNumberGiven_WorkerReturnsFailure() {
    // Define input data
    val inputData = FibonacciWorker.makeInputData(-1)

    // Define constraints
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiresDeviceIdle(true)
        .build()

    // Create request
    val request = OneTimeWorkRequest.Builder(FibonacciWorker::class.java)
        .setInputData(inputData)
        .setConstraints(constraints)
        .build()

    val workManager = WorkManager.getInstance()
    val testDriver = WorkManagerTestInitHelper.getTestDriver()

    // Enqueue and wait for result.
    workManager.enqueue(request)
        .result.get()

    // Use TestDriver to fake constraints being met
    testDriver.setAllConstraintsMet(request.id)

    // Get WorkInfo
    val workInfo = workManager.getWorkInfoById(request.id)
        .get()

    workInfo.state shouldEqual FAILED
  }
}
