package com.example.jetpackproject.workManager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.work.*
import com.example.jetpackproject.R
import com.example.jetpackproject.databinding.ActivityWorkManegerBinding
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class WorkManegerActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "WorkManegerActivity"
    lateinit var binding: ActivityWorkManegerBinding
    private val mWorkA = OneTimeWorkRequest.Builder(UploadWorker::class.java)
        .addTag("workA")
        .build()

    val mWList = ArrayList<UUID>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_maneger)
        initView()
    }

    private fun initView() {
        onclickView(
            arrayOf(
                binding.workA,
                binding.workC,
                binding.constrains,
                binding.delay,
                binding.query,
                binding.regularTask,
                binding.singleTask,
                binding.viewTask,
                binding.cancelAllTask,
                binding.cancelSingleTask,
                binding.clean,
                binding.cleanTask
            )
        )
    }

    private fun onclickView(views: Array<View>) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.workA -> {
                //mWorkA 只执行一次
                mWList.add(mWorkA.id)
                val op = WorkManager.getInstance(applicationContext).enqueue(mWorkA)
                binding.logContent.postDelayed({
                    binding.logContent.text =
                        "${binding.logContent.text}\n${UploadWorker.doWork}\n" +
                                "${op.result}--${op.state.value}"
                }, 400)

            }
            R.id.workC -> {
                //workC 每次都是新建对象，可执行多次
                val workC = OneTimeWorkRequest.Builder(UploadWorker::class.java)
                    .addTag("workC")
                    .build()
                mWList.add(workC.id)
                WorkManager.getInstance(applicationContext).enqueue(workC)
                binding.logContent.postDelayed({
                    binding.logContent.text = "${binding.logContent.text}\n${UploadWorker.doWork}"
                }, 400)
            }
            R.id.constrains -> {
                /**达到约束条件才会执行
                NetworkType 约束运行工作所需的网络类型。例如 Wi-Fi (UNMETERED)。
                BatteryNotLow 若为 true，那么当设备处于“电量不足模式”时，工作不会运行。
                RequiresCharging 若为 true，那么工作只能在设备充电时运行。
                DeviceIdle 若为 true，则设备必须处于空闲状态才能运行工作。如果考虑到其他应用的性能，建议用这个约束。
                StorageNotLow 若为 true，那么当设备上的存储空间不足时，工作不会运行。
                 */
                val constrain = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED) //需要Wifi
                    .build()
                val d1 = OneTimeWorkRequest.Builder(UploadWorker2::class.java)
                    .setConstraints(constrain)
                    .addTag("约束")
                    .build()
                mWList.add(d1.id)
                WorkManager.getInstance(applicationContext).enqueue(d1)
                binding.logContent.postDelayed({
                    binding.logContent.text = "${binding.logContent.text}\n${UploadWorker2.dowork2}"
                }, 200)
            }
            R.id.delay -> {
                val delayWork = OneTimeWorkRequest.Builder(UploadWorker2::class.java)
                    .setInitialDelay(3, TimeUnit.SECONDS)
                    .addTag("延迟3s")
                    .build()
                mWList.add(delayWork.id)
                WorkManager.getInstance(applicationContext).enqueue(delayWork)
                binding.logContent.postDelayed({
                    binding.logContent.text = "${binding.logContent.text}\n${UploadWorker2.dowork2}"
                }, 200)
            }
            R.id.query -> {
                val wmg = WorkManager.getInstance(applicationContext)
                //使用UUID查询
                for (i in mWList) {
                    val cur = wmg.getWorkInfoById(i)
                    binding.logContent.text = "${binding.logContent.text}\n查询任务：${cur.get()}\n"
                }
                //使用tag查询
                for (t in wmg.getWorkInfosByTag("约束").get()) {
                    binding.logContent.text = "${binding.logContent.text}\n使用tag查询：$t"
                }
            }
            R.id.regularTask -> {
                //定时任务，enqueue后立即执行一次任务；定时一定时间后会再次创建任务执行
                val regular1 = PeriodicWorkRequestBuilder<UploadWorker2>(15, TimeUnit.MINUTES)
                    .addTag("regular1")
                    .build()
                mWList.add(regular1.id)
                WorkManager.getInstance(applicationContext).enqueue(regular1)

                binding.logContent.postDelayed({
                    binding.logContent.text = "${binding.logContent.text}\n${UploadWorker2.dowork2}"
                }, 200)
            }
            R.id.singleTask -> {
                val regular2 = PeriodicWorkRequestBuilder<UploadWorker2>(15, TimeUnit.MINUTES)
                    .addTag("regular2")
                    .build()
                mWList.add(regular2.id)
                WorkManager.getInstance(applicationContext)
                    .enqueueUniquePeriodicWork(
                        "单独的定时任务regular2",
                        //发现重复任务时，保留原任务，不新增任务；REPLACE停止删除旧任务，插入新任务
                        ExistingPeriodicWorkPolicy.KEEP,
                        regular2
                    )
                binding.logContent.postDelayed({
                    binding.logContent.text = "${binding.logContent.text}\n${UploadWorker2.dowork2}"
                }, 200)
            }
            R.id.viewTask -> {
                val states =
                    WorkManager.getInstance(applicationContext).getWorkInfosByTag("regular1")
                val workInfoList = states.get()
                for (w in workInfoList) {
                    binding.logContent.text = "${binding.logContent.text}\n查看regular1任务：$w"
                }
            }
            R.id.cancelAllTask -> {
                val allWork = WorkManager.getInstance(applicationContext)
                allWork.cancelAllWork()
                for (i in mWList) {
                    val workInfo = allWork.getWorkInfoById(i)
                    binding.logContent.text =
                        "${binding.logContent.text}\n查看所有任务的状态：${workInfo.get()}\n"
                }
            }
            R.id.cancelSingleTask -> {
                val allWork = WorkManager.getInstance(applicationContext)
                val works = allWork.getWorkInfosByTag("regular1").get()
                for (w in works) {
                    allWork.cancelWorkById(w.id)
                }
                allWork.cancelAllWorkByTag("延迟3s")
                allWork.cancelUniqueWork("regular2")
                for (i in mWList) {
                    val workInfo = allWork.getWorkInfoById(i)
                    binding.logContent.text =
                        "${binding.logContent.text}\n查看所有任务的状态：${workInfo.get()}\n"
                }
            }
            R.id.clean -> binding.logContent.text = ""
            R.id.cleanTask -> mWList.clear()
        }
    }
}