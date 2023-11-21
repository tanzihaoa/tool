package com.tzh.tools.model
import com.google.gson.annotations.SerializedName


data class PhoneNumberModel(
    @SerializedName("city")
    val city: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("province")
    val province: String
)