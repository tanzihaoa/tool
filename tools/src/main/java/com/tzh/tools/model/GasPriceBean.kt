package com.tzh.tools.model

data class GasPriceBean(
    val list: List<GasBean>
)

data class GasBean(
    val `0h`: String,
    val `92h`: String,
    val `95h`: String,
    val `98h`: String,
    val city: String
)
