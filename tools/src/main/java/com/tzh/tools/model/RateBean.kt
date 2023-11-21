package com.tzh.tools.model

import androidx.annotation.Keep

@Keep
data class RateBean(
    val currencyF: String?,
    val currencyF_Name: String?,
    val currencyT: String?,
    val currencyT_Name: String?,
    val exchange: String?
)