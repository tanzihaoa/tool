package com.tzh.tools.model

import androidx.annotation.Keep

@Keep
class UpcomingFestivals(
    var dayOfMonth: String = "", var month: String = "", var festival: String = "",
    var data: String = "", var differDay: String = ""
)