package com.tzh.tools.model

data class ScanningInternetModel(
    val icon: Int,
    val name: String,
    var ip: String,
    val devicesType: String,
    var mode: String = "default"
    )
