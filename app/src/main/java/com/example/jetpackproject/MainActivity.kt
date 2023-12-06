package com.example.jetpackproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackproject.utils.MainViewModelFactory
import com.example.jetpackproject.utils.MyObserver
import com.example.jetpackproject.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved", 0)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(countReserved)
        ).get(MainViewModel::class.java)
        initView()
        //使用lifecycle感知activity的生命周期
        lifecycle.addObserver(MyObserver(lifecycle))
        //使用livedata 在数据发生变化的时候会通知观察者
        viewModel.counter.observe(this) {
            infoText.text = it.toString()
        }
        viewModel.user.observe(this) {
            infoText.text = it.firstName
        }
    }

    private fun initView() {
        plusOneBtn.setOnClickListener(this)
        clearBtn.setOnClickListener(this)
        getUserBtn.setOnClickListener(this)
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

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter.value ?: 0)
        }
    }
}