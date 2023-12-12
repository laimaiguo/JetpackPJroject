package com.example.jetpackproject

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableArrayMap
import com.example.jetpackproject.bean.ClickFunctionVM
import com.example.jetpackproject.bean.SysInfoObs
import com.example.jetpackproject.databinding.DatabindingLayoutBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 *Create by GWJ 2023/12/12 9:56
 **/
class DataBindingAct : AppCompatActivity() {
    lateinit var binding: DatabindingLayoutBinding
    private val mSysInfo = SysInfoObs()
    private var mTimer: Timer? = null
    private val mClickFunctionVM = ClickFunctionVM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.databinding_layout)
        binding.info = mSysInfo
        mTimer = Timer()
        mTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mSysInfo.timeStr.set("Time:${System.currentTimeMillis()}")
                mSysInfo.time.set(System.currentTimeMillis())
            }
        }, 0, 100)
        val user = ObservableArrayMap<String, Any>()
        user["firstName"] = "Rust"
        user["lastName"] = "Fisher"
        user["age"] = 20
        binding.user = user

        val obList = ObservableArrayList<Any>()
        obList.add("Rust")
        obList.add("Fisher")
        obList.add("Android")
        obList.add(2023)
        obList.add("Kotlin")
        binding.obList = obList

        //设置点击事件
        binding.vm = mClickFunctionVM
        searchListener()
    }

    //搜索监听事件
    private fun searchListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Snackbar.make(binding.searchView, "搜索内容===$p0", Snackbar.LENGTH_SHORT).show()
                binding.searchView.clearFocus() //清除焦点，收起软键盘
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (!TextUtils.isEmpty(p0)) {
                    Log.e("onQueryTextChange", p0!!)
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer!!.cancel()
    }
}