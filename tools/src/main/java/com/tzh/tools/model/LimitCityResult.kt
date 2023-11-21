package com.tzh.tools.model

import com.google.gson.annotations.SerializedName

class LimitCityResult  {
    @SerializedName("list")
    var list: List<LimitCity> = mutableListOf()
}