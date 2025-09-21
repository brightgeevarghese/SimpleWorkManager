package com.example.wmdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        return runCatching {
            for (i in 1..50) {
                if (isStopped) {
                    Log.d("MyWorker", "Worker stopped")
                    return Result.failure()
                }
//                if (i % 10 == 0) {
//                    Log.d("MyWorker", "throw error at $i")
//                    throw Exception("Error at $i")
//                }
                println("Hello from worker $i")
                Thread.sleep(1000)
                Log.d("MyWorker", "Hello from worker $i")
            }
        }.fold(
            onSuccess = {
                Log.d("MyWorker", "Worker finished")
                Result.success()
            },
            onFailure = {error ->
                Log.d("MyWorker", "Worker failed due to ${error.message}")
                Result.failure()
            }
        )
    }

    override fun onStopped() {
        super.onStopped()
        Log.d("MyWorker", "Worker stopped")
    }
}