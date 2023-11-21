package com.tzh.tools.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: Created by zfj
 * created on: 2023/5/16
 * description:
 */
fun Date.toFormatStringYear(): String {
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(this)
}

fun Date.toFormatStringMonth(): String {
    return SimpleDateFormat("MM", Locale.getDefault()).format(this)
}

fun Date.toFormatStringYearMonth(): String {
    return SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(this)
}

fun Date.toFormatStringYearMonth2(): String {
    return SimpleDateFormat("yyyy年MM月", Locale.getDefault()).format(this)
}

fun Date.toFormatStringMonthDay(): String {
    return SimpleDateFormat("MM-dd", Locale.getDefault()).format(this)
}


fun Date.toFormatStringDay(): String {
    return SimpleDateFormat("dd", Locale.getDefault()).format(this)
}

fun Date.toFormatStringMonthDay2(): String {
    return SimpleDateFormat("MM月dd日", Locale.getDefault()).format(this)
}

fun Date.toFormatStringMonthDay3(): String {
    return SimpleDateFormat("MM.dd", Locale.getDefault()).format(this)
}

fun Date.toFormatStringYearMonthDay(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this)
}

fun Date.toFormatStringYearMonthDay2(): String {
    return SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(this)
}

fun Date.toFormatStringYearMonthDay3(): String {
    return SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(this)
}

/**
 * 获取当前日期中某月的第一天
 * @receiver Date
 * @return String
 */
fun Date.getMonthFirstDay(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DATE, 1)
    return calendar.time.toFormatStringYearMonthDay()
}

fun Date.getMonthFirstDay3(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DATE, 1)
    return calendar.time.toFormatStringMonthDay3()
}

/**
 * 获取当前日期中某月的最后一天
 * @receiver Date
 * @return String
 */
fun Date.getMonthLastDay(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DATE, 1)
    calendar.add(Calendar.MONTH, 1)
    calendar.set(Calendar.DATE, 0)
    return calendar.time.toFormatStringYearMonthDay()
}

fun Date.getMonthLastDa3(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DATE, 1)
    calendar.add(Calendar.MONTH, 1)
    calendar.set(Calendar.DATE, 0)
    return calendar.time.toFormatStringMonthDay3()
}

fun String.toDate(): Date {
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        Date()
    }

}
fun String.toDate2(): Date {
    return try {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        Date()
    }

}

fun Long.toDate(): Date {
    var calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.time
}

fun Int.toWeekChinese(): String {
    return when (this) {
        0 -> "星期天"
        1 -> "星期一"
        2 -> "星期二"
        3 -> "星期三"
        4 -> "星期四"
        5 -> "星期五"
        6 -> "星期六"
        else -> ""
    }
}

fun Int.toWeekChinese2(): String {
    return when (this) {
        0 -> "周日"
        1 -> "周一"
        2 -> "周二"
        3 -> "周三"
        4 -> "周四"
        5 -> "周五"
        6 -> "周六"
        else -> ""
    }
}