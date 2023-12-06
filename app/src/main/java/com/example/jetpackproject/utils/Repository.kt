package com.example.jetpackproject.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jetpackproject.bean.User

/**
 *Create by GWJ 2023/9/3 17:31
 **/
object Repository {
    fun getUser(userId: String): LiveData<User> {
        val liveData = MutableLiveData<User>()
        liveData.value = User(userId, userId, 18)
        return liveData
    }
}