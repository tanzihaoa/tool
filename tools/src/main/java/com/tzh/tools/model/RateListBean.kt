package com.tzh.tools.model

import androidx.annotation.Keep

@Keep
data class RateListBean(
    val records: List<RateListBeanData>?=null
)

data class RateListBeanData(
    val code: String?=null,
    val name: String?=null
)