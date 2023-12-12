package com.example.jetpackproject.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 *Create by GWJ 2023/12/12 14:06
 **/
object DateUtils {
    fun getDate(currentTime: Long): String {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        calendar.timeInMillis = currentTime
        return simpleDateFormat.format(calendar.time)
    }
}