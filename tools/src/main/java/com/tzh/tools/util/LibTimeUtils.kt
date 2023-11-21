package com.tzh.tools.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object LibTimeUtils {
    private val dateFormat = SimpleDateFormat("MM/dd")
    private val weekStrings = arrayOf("日", "一", "二", "三", "四", "五", "六")

    /**
     * 改变日期格式
     * @param date  2017年02月09日
     * @return 2017-02-09
     */
    fun changeFormatDate(date: String): String? {
        val dFormat = SimpleDateFormat("yyyy-MM-dd")
        var curDate: String? = null
        try {
            val dt = dateFormat.parse(date)
            curDate = dFormat.format(dt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return curDate
    }

    /**
     * 返回当前的时间 yyyy-MM-dd HH:mm:ss
     * @return  yyyy-MM-dd HH:mm:ss
     */
    fun getCurrentTimeStamp(): String {
        val dFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dFormat.format(System.currentTimeMillis())
    }

    fun dateStringToDate(date: String): Date {
        val dFormat = SimpleDateFormat("yyyy-MM-dd")
        var curDate: Date = Date()
        try {
            curDate = dFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return curDate
    }

    /**
     * 判断日期是否与当前日期差7天
     * @param date  2020年04月22日
     * @return true
     */
    fun isDateOutDate(date: String): Boolean {
        try {
            if ((Date().time - dateFormat.parse(date).time) > 6 * 24 * 60 * 60 * 1000) return true

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }

    fun getCurrentMin(): Int {
        val date = Date()
        return date.minutes
    }

    /**
     * 返回当前的时间
     * @return  今天 09:48
     */
    private fun getCurTime(): String {
        val dFormat = SimpleDateFormat("HH:mm")
        return "今天 " + dFormat.format(System.currentTimeMillis())
    }


    /**
     * 返回当前的时间
     * @return  今天 09:48
     */
    fun getCurrentTime(): String {
        val dFormat = SimpleDateFormat("HH:mm")
        return dFormat.format(System.currentTimeMillis())
    }

    fun getCurrDayString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(Date())
    }

    fun getBeforeDayString(): String {
        val preCalendar = Calendar.getInstance()
        preCalendar.add(Calendar.DATE, -1)
        var dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(preCalendar.time)
    }

    fun getAfterDayString(): String {
        val preCalendar = Calendar.getInstance()
        preCalendar.add(Calendar.DATE, 1)
        var dateFormat = SimpleDateFormat("MM月dd日/明天")
        return dateFormat.format(preCalendar.time)
    }

    fun getBeforeDay(): Int {
        val preCalendar = Calendar.getInstance()
        preCalendar.add(Calendar.DATE, -1)
        return preCalendar.time.day
    }

    fun getAfterDay(): Int {
        val preCalendar = Calendar.getInstance()
        preCalendar.add(Calendar.DATE, 1)
        return preCalendar.time.day
    }

    fun getQianDay(): Int {
        val preCalendar = Calendar.getInstance()
        preCalendar.add(Calendar.DATE, -2)
        return preCalendar.time.day
    }

    /**
     * 获取是几号
     *
     * @return dd
     */
    fun getCurrentDay(): Int {
        return Calendar.getInstance().get(Calendar.DATE)
    }

    /**
     * 获取当前的日期
     *
     * @return yyyy年MM月dd日
     */
    fun getCurrentDate(): String {
        return dateFormat.format(
            Calendar.getInstance().time
        )
    }

    fun getCurrentYM(): String {
        val dFormat = SimpleDateFormat("yyyy/MM")
        return dFormat.format(System.currentTimeMillis())
    }

    fun getBeforeDate(): String {
        val preCalendar = Calendar.getInstance()
        preCalendar.add(Calendar.DATE, -1)
        return dateFormat.format(preCalendar.time)
    }


    /**
     * 根据date列表获取day列表
     *
     * @param dateList
     * @return
     */
    fun dateListToDayList(dateList: List<String>): List<Int> {
        val calendar = Calendar.getInstance()
        val dayList: MutableList<Int> = ArrayList()
        for (date in dateList) {
            try {
                calendar.time = dateFormat.parse(date)
                val day = calendar.get(Calendar.DATE)
                dayList.add(day)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        return dayList
    }


    /**
     * 根据当前日期获取以含当天的前一周日期
     * @return [2017年02月21日, 2017年02月22日, 2017年02月23日, 2017年02月24日, 2017年02月25日, 2017年02月26日, 2017年02月27日]
     */
    fun getBeforeDateListByNow(): List<String> {
        val weekList: MutableList<String> = ArrayList()

        for (i in -5..0) {
            //以周日为一周的第一天
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, i)
            val date = dateFormat.format(calendar.time)
            weekList.add(date)
        }
        return weekList
    }

    /**
     * 判断当前日期是周几
     * @param curDate
     * @return
     */
    fun getCurWeekDay(curDate: String): String {
        var w = 0
        try {
            val date = dateFormat.parse(curDate)
            val calendar = Calendar.getInstance()
            calendar.time = date
            w = calendar.get(Calendar.DAY_OF_WEEK) - 1
            if (w < 0) {
                w = 0
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return weekStrings[w]
    }

    fun getWeekDayByCalendar(calendar: Calendar): String {
        var w = 0
        try {
//                val date = dateFormat.parse(curDate)
//                val calendar = Calendar.getInstance()
//                calendar.time = date
            w = calendar.get(Calendar.DAY_OF_WEEK) - 1
            if (w < 0) {
                w = 0
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return weekStrings[w]
    }

    /**
     *获取当时天过去的秒数
     *
     */
    fun getCurrentMill(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val zero = calendar.time
        return ((System.currentTimeMillis() - zero.time) / 1000).toInt()
    }

    /**
     *是否是上午
     *
     */
    fun isMorning(): Boolean {
        val date = Date()
        return date.hours <= 12
    }

    fun getDelayWeek(delay: Int): String? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_WEEK, delay)
        val week = cal.get(Calendar.DAY_OF_WEEK)
        var desc = "今天"
        when (delay) {
            -1 -> {
                desc = "昨天"
            }
            0 -> {
                desc = "今天"
            }
            1 -> {
                desc = "明天"
            }
            else -> {
                when (week) {
                    Calendar.SUNDAY -> {
                        desc = "周日"
                    }
                    Calendar.MONDAY -> {
                        desc = "周一"
                    }
                    Calendar.TUESDAY -> {
                        desc = "周二"
                    }
                    Calendar.WEDNESDAY -> {
                        desc = "周三"
                    }
                    Calendar.THURSDAY -> {
                        desc = "周四"
                    }
                    Calendar.FRIDAY -> {
                        desc = "周五"
                    }
                    Calendar.SATURDAY -> {
                        desc = "周六"
                    }
                }
            }
        }

        return desc
    }

    fun getDelayDay(delay: Int): String? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, delay)
        val dft = SimpleDateFormat("MM/dd")
        return dft.format(cal.time)
    }

    fun getDelayHour(delay: Int): Int {
        val date = Date()
        var hour = date.hours + delay
        hour %= 24
//            val cal = Calendar.getInstance()
//            cal.add(Calendar.DAY_OF_MONTH, delay)
//            val week = cal.get(Calendar.HOUR_OF_DAY)
//            val dft = SimpleDateFormat("MM/dd")
//            return if (hour > 9) hour.toString() else "0${hour.toString()}"
        return hour
    }

    fun stringFormatDetailToDate(time: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Date {
        try {
            val format = SimpleDateFormat(pattern)
            return format.parse(time) ?: Date()
        } catch (e: Exception) { //time 日期有时候无法解析，抛出 java.text.ParseException
            e.printStackTrace()
        }
        return Date()
    }

    /**
     * 返回当前MM/dd
     * @return  03/13
     */
    fun getCurDate(): String {
        return dateFormat.format(System.currentTimeMillis())
    }

    fun timeToStringDate(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd").format(time)
    }

    fun changeFormatDateCN(): String? {
        val dFormat = SimpleDateFormat("yyyy-MM-dd")
        dFormat.timeZone = TimeZone.getTimeZone("GMT+08")
        val curDate = Date(System.currentTimeMillis()) //获取当前时间
        return dFormat.format(curDate)
    }
}