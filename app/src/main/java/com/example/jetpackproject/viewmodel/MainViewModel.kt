package com.example.jetpackproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.jetpackproject.bean.User
import com.example.jetpackproject.utils.Repository

/**
 *Create by GWJ 2023/9/3 16:12
 * ViewModel：存放activity需要的变量，减少activity的逻辑
 * LiveData：可以包含任何类型的数据，并在数据发生变化时通知给观察者
 **/
class MainViewModel(countReserved: Int) : ViewModel() {
    val counter: LiveData<Int>
        get() = _counter
    private val _counter = MutableLiveData<Int>()

    private val userLiveData = MutableLiveData<User>()
    private val userStrLiveData = MutableLiveData<String>()

    /**map函数：将实际包含数据的livedata进行转换*/
    val userName: LiveData<String> = Transformations.map(userLiveData) {
        "${it.firstName}${it.lastName}"
    }

    /**当使用的livedata不是在ViewModel中创建的，而是通过调用另外的方法获取,可以使用switchMap方法将这个livedata转换为可观察的LiveData对象*/
    val user: LiveData<User> = Transformations.switchMap(userStrLiveData) {
        Repository.getUser(it)
    }

    init {
        _counter.value = countReserved
    }

    fun plusOne() {
        val count = _counter.value ?: 0
        _counter.value = count + 1
    }

    fun clear() {
        _counter.value = 0
    }

    fun getUser(userId: String) {
        userStrLiveData.value = userId
    }
}