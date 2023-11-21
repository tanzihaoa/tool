package com.tzh.tools.model

data class TestSpeedModel(
    val icon: Int,
    val name: String,
    var speed: String = "--ms",
    val ip: String
)
