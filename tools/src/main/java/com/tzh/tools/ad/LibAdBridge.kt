package com.tzh.tools.ad

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity


class LibAdBridge {
    companion object {
        val instance: LibAdBridge by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LibAdBridge()
        }
    }
    private var mAdListener:AdListener? = null

    fun initAd(ad:AdListener){
        this.mAdListener = ad
    }



    fun startRewardVideo(activity: FragmentActivity,
                         onShow: () -> Unit = {},
                         inValid: () -> Unit = {}, onReward: () -> Unit = {}){
        mAdListener?.startRewardVideo(activity,onShow,inValid,onReward)
    }

    fun startInterstitial(
        activity: FragmentActivity,
        onLoaded: () -> Unit = {}, // 本地拿到广告
        onShow: () -> Unit = {}, onClose: () -> Unit = {}
    ){
        mAdListener?.startInterstitial(activity,onLoaded,onShow,onClose)
    }

    fun startFeed(
        activity: FragmentActivity,
        adContainer: ViewGroup?,
        listener: FeedLibAdListener? = null,
        from: String = "",
        showAfterRender: Boolean = true,
        limited: Boolean = false
    ){
        mAdListener?.startFeed(activity,adContainer,listener,from,showAfterRender,limited)
    }

    fun startFull(
        activity: FragmentActivity,
        onLoaded: () -> Unit = {}, // 本地拿到广告
        onShow: () -> Unit = {}, onClose: () -> Unit = {}
    ){
        mAdListener?.startFull(activity,onLoaded,onShow,onClose)
    }

    fun prepareFull(activity: FragmentActivity){
        mAdListener?.prepareFull(activity)
    }

    fun prepareInsert(activity: FragmentActivity){
        mAdListener?.prepareInsert(activity)
    }

    fun prepareVideo(activity: FragmentActivity){
        mAdListener?.prepareVideo(activity)
    }
    fun excludeFromBackground(){
        mAdListener?.excludeFromBackground()
    }
    fun excludeAdViaMember(){
        mAdListener?.excludeAdViaMember()
    }
}