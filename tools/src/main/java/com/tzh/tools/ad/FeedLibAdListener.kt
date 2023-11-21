package com.tzh.tools.ad

import android.view.View

/**
 * Created by xwm on 2021/7/20
 */
interface FeedLibAdListener : ICommonLibAdListener {

    fun onAdClick() {}

    fun onSingleLoaded(ad: Any?) {}

    fun onAdShow() {}

    fun onRenderSuccess(adView: View) {}

    fun onRenderFail(adView: View?) {}

    fun onAdLoadedFail() {}

    fun onDislike() {}
}