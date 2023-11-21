package com.tzh.tools.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable
@Keep
data class TrafficRestriction(var date: String = "", var plates: List<String> = mutableListOf()) : Serializable
class TrafficRestrictionResult  :Serializable{
    @SerializedName("limits")
    var limits: List<TrafficRestriction> = mutableListOf()

    @SerializedName("city")
    var city :String =""

    @SerializedName("penalty")
    var penalty :String =""
    @SerializedName("region")
    var region :String =""
    @SerializedName("remarks")
    var remarks :String =""


}