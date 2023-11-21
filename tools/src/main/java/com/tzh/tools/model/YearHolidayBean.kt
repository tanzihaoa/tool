package com.tzh.tools.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
class YearHolidayBean  {
    @SerializedName("dayCount")
    var dayCount :String =""
    @SerializedName("endDay")
    var endDay :String =""
    @SerializedName("holiday")
    var holiday :String =""
    @SerializedName("name")
    var name :String =""
    @SerializedName("startDay")
    var startDay :String =""
    @SerializedName("startDayWeek")
    var startDayWeek :String =""
    @SerializedName("tip")
    var tip :String =""
}


