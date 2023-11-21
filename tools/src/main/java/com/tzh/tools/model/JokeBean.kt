package com.tzh.tools.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
class JokeBean {
    @SerializedName("content")
    var content: String = ""

    @SerializedName("hashId")
    var hashId: String = ""

    @SerializedName("unixtime")
    var unixtime: Int = 0
}


