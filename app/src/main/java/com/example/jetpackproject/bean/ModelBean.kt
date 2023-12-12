package com.example.jetpackproject.bean

import android.os.Build
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.ObservableField

/**
 *Create by GWJ 2023/9/3 17:25
 **/
data class User(var firstName: String, var lastName: String, var age: Int)


class SysInfoObs {
    val info1 = ObservableField(Build.MANUFACTURER)
    val timeStr = ObservableField<String>()
    val time = ObservableField<Long>()
}

//点击事件
class ClickFunctionVM {
    fun onClickBack(view: View) {
        Toast.makeText(view.context, "后退", Toast.LENGTH_SHORT).show()
    }

    fun onClickAdd(view: View) {
        Toast.makeText(view.context, "前进", Toast.LENGTH_SHORT).show()
    }
}

//双向数据绑定
class TwoWay {
    val rememberMe = ObservableField(false)
    fun rememberMeChanged(buttonView: CompoundButton, isChecked: Boolean) {
        rememberMe.set(isChecked)
    }
}