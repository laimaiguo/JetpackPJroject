package com.example.jetpackproject.workManager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 *Create by GWJ 2023/12/19 9:31
 * 定义一个任务
 **/
class UploadWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        var doWork = ""
    }

    override fun doWork(): Result {
        doWork = ""
        for (i in 1..3) {
            doWork = "${doWork}\n模拟执行任务 ${tags.first()} ${Thread.currentThread()}"
            Thread.sleep(100) //模拟耗时
        }
        return Result.success()
    }
}