package com.example.jetpackproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackproject.databinding.ActivityMainBinding
import com.example.jetpackproject.utils.MainViewModelFactory
import com.example.jetpackproject.utils.MyObserver
import com.example.jetpackproject.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "ACT"
    var binding: ActivityMainBinding? = null
    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate: start")
        super.onCreate(savedInstanceState)
        /*binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved", 0)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(countReserved)
        ).get(MainViewModel::class.java)
        initView()
        binding!!.isshow = true
        //使用lifecycle感知activity的生命周期
        lifecycle.addObserver(MyObserver(lifecycle))
        Log.e("onCreate: ", lifecycle.currentState.name)
        //使用livedata 在数据发生变化的时候会通知观察者
        viewModel.counter.observe(this) {
            binding!!.infoText.text = it.toString()
            binding!!.content = it.toString()
            binding!!.age = it
        }
        viewModel.user.observe(this) {
            binding!!.infoText.text = it.firstName
        }
        Log.e(TAG, "onCreate: end")
    }

    private fun initView() {
        binding!!.plusOneBtn.setOnClickListener(this)
        binding!!.clearBtn.setOnClickListener(this)
        binding!!.getUserBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.plusOneBtn -> {
                viewModel.plusOne()
            }
            R.id.clearBtn -> {
                viewModel.clear()
            }
            R.id.getUserBtn -> {
                val userId = (0..10000).random().toString()
                viewModel.getUser(userId)
            }
        }
    }

    override fun onStart() {
        Log.e(TAG, "onStart: start")
        super.onStart()
        Log.e(TAG, "onStart: end")
    }
    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter.value ?: 0)
        }
    }

    override fun onDestroy() {
        Log.e("ACT", "onDestroy: start")
        super.onDestroy()
        Log.e("onDestroy: ", lifecycle.currentState.name)
        Log.e("ACT", "onDestroy: end")
    }
}