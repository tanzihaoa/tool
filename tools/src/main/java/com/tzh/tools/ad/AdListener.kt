package com.tzh.tools.ad

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity

interface AdListener {
    /**
     * 请求激励视频
     * @param inValid 广告结束后，未达到激励标准时回调
     * @param onReward 广告结束后，达到激励门槛时回调
     */
    fun startRewardVideo(activity: FragmentActivity,
                         onShow: () -> Unit = {},
                         inValid: () -> Unit = {}, onReward: () -> Unit = {})

    /**
     * 请求插屏广告
     * @param onShow 插屏广告展示
     * @param onClose 插屏广告关闭
     */
    fun startInterstitial(
        activity: FragmentActivity,
        onLoaded: () -> Unit = {}, // 本地拿到广告
        onShow: () -> Unit = {}, onClose: () -> Unit = {}
    )

    /**
     * 加载信息流广告
     * @param adContainer 信息流广告组件容器
     * @param listener 更详细的广告回调
     * @param from 自定义广告位置，刷新时间按from记录
     * @param showAfterRender 信息流广告渲染后是否直接展示
     * @param limited 是否限制刷新,距离上次刷新时间超过 {@link AdConfig#FeedAdConfig#feedAdRefreshTimeLimit} 才刷新
     */
    fun startFeed(
        activity: FragmentActivity,
        adContainer: ViewGroup?,
        listener: FeedLibAdListener? = null,
        from: String = "",
        showAfterRender: Boolean = true,
        limited: Boolean = false
    )


    /**
     * 展示全屏广告
     */
    fun startFull(
        activity: FragmentActivity,
        onLoaded: () -> Unit = {}, // 本地拿到广告
        onShow: () -> Unit = {}, onClose: () -> Unit = {}
    )

    /**
     * 提前预加载一个全屏视频，场景为马上使用，过场时间提前加载
     * 需要判断此广告位poolMax是否大于0，如果已经预加载不执行此方法
     */
    fun prepareFull(activity: FragmentActivity)

    /**
     * 提前预加载一个全屏视频，场景为马上使用，过场时间提前加载
     * 需要判断此广告位poolMax是否大于0，如果已经预加载不执行此方法
     */
    fun prepareInsert(activity: FragmentActivity)

    /**
     * 提前预加载一个全屏视频，场景为马上使用，过场时间提前加载
     * 需要判断此广告位poolMax是否大于0，如果已经预加载不执行此方法
     */
    fun prepareVideo(activity: FragmentActivity)

    /**
     * 本次后台回来无需进入fromBackground逻辑
     */
    fun excludeFromBackground()
    fun excludeAdViaMember()

}