package com.tzh.tools.model

import com.google.gson.annotations.SerializedName

/**
 * Description:邮编实体类
 * @Author: LYL
 * @CreateDate: 2023/5/30 13:59
 */
class ZipCodeModel {

    @SerializedName("list")
    var list: List<ZipCodeBean> = mutableListOf()

    class ZipCodeBean(
        @SerializedName("PostNumber")
        val PostNumber: String,
        @SerializedName("Province")
        val Province: String,
        @SerializedName("City")
        val City: String,
        @SerializedName("District")
        val District: String,
        @SerializedName("Address")
        val Address: String
    )
}