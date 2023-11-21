package com.tzh.tools.model

import androidx.annotation.Keep

@Keep
class LatelyFestivalBean {

    var countDownDay: Int? = null//距离当前还剩几天
    var date: String? = null//日期
    var holidayDays: Int? = null//如果是法定节日，放假几天
    var longTime: Long? = null//毫秒时间戳
    var name: String? = null//节日名
    var time: String? = null//节气会有这个时间
    var type: Int? = null//1是节气，2是法定节日

}