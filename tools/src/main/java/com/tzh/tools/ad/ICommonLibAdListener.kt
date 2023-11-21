package com.tzh.tools.ad

interface ICommonLibAdListener {

    /**
     * 表示广告示例是从本地缓存池中获取的
     */
    fun onAdPeekFromPool() {}

    /**
     * 广告实际请求数超过
     */
    fun onRequestExceedLimit(limit: Int) {}

    /**
     * 实际发起广告请求动作前回调
     */
    fun onBeforeAdRequest(reqNo: Int) {}


}