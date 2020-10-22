package com.example.expenseappmvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import com.example.expenseappmvvm.R
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    @SuppressLint("SimpleDateFormat")
    fun gmtToLocalTime(timestamp: Long): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd' at 'HH:mm")
        return formatter.format(Date(timestamp))
    }

    @SuppressLint("SimpleDateFormat")
    fun transactionTimeFormat(context: Context, timestamp: Long): String {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val timeMs = calendar.timeInMillis

        return if (timeMs - timestamp < Constants.oneDayMs) {
            context.getString(R.string.today)
        } else if (timeMs - timestamp > Constants.oneDayMs && timeMs - timestamp < Constants.twoDayMs) {
            context.getString(R.string.yesterday)
        } else {
            val formatter = SimpleDateFormat("EEE dd MMM")
            formatter.format(Date(timestamp))
        }
    }

    fun currencyDateToGMT(date: String): Long {
        val dateArray = date.split("-")
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.DAY_OF_MONTH, dateArray[2].toInt())
            set(Calendar.MONTH, dateArray[1].toInt() - 1)
            set(Calendar.YEAR, dateArray[0].toInt())
        }
        return calendar.timeInMillis
    }

    //Check if the last saved currency are from yesterday
    fun lastCurrencyCheck(dbDate: Long): Boolean {
        if (!DateUtils.isToday(dbDate + DateUtils.DAY_IN_MILLIS)){
            return true
        }
        return false
    }

    @SuppressLint("SimpleDateFormat")
    fun currencyDateFormat(timestamp: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(Date(timestamp))
    }
}