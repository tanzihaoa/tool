package com.tzh.tools.model

import androidx.annotation.Keep

@Keep
data class TodayInHistoryData(var dayMonth: String = "", var list: List<TodayInHistoryBean> = mutableListOf())
