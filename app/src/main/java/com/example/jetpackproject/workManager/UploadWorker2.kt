package com.example.jetpackproject.workManager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 *Create by GWJ 2023/12/19 10:35
 **/
class UploadWorker2(
    context: Context, params: WorkerParameters
) : Worker(context, params) {

    companion object {
        var dowork2 = ""
    }

    override fun doWork(): Result {
        dowork2 = "模拟执行任务2 ${Thread.currentThread()}"
        Log.e("UploadWorker2", "模拟执行任务2 ${Thread.currentThread()}")
        return Result.success()
    }
}