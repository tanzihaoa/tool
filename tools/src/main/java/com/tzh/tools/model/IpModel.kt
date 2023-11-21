package com.tzh.tools.model
import com.google.gson.annotations.SerializedName


data class IpModel(
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("isp")
    val isp: String,
    @SerializedName("province")
    val province: String
)