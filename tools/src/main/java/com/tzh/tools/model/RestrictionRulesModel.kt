package com.tzh.tools.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RestrictionRulesModel(val day: String, val todayDate: String, val status1: String, val date1: String, val status2: String, val date2: String, val status3: String, val date3: String, val content: String) :
    Parcelable