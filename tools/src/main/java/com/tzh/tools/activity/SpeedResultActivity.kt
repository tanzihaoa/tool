package com.tzh.tools.activity

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.content.res.ResourcesCompat
import com.tzh.tools.R
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.WifiUtil
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus

/**
 * 网络测速结果
 */
class SpeedResultActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var maxDownloadSpeed: Long = 0L
    private var maxDownloadSpeedStr = "不给力"
    private var maxUploadSpeedStr = "不给力"
    private var networkDelay = "460ms"


    override fun initVar() {
        networkDelay = intent.getStringExtra("networkDelay") ?: ""
        maxDownloadSpeed = intent.getLongExtra("maxDownloadSpeed", maxDownloadSpeed)
        maxDownloadSpeedStr = intent.getStringExtra("maxDownloadSpeedStr") ?: "不给力"
        maxUploadSpeedStr = intent.getStringExtra("maxUploadSpeedStr") ?: "不给力"
        if (maxDownloadSpeedStr.isEmpty()) {
            maxDownloadSpeedStr = "不给力"
        }
        if (maxUploadSpeedStr.isEmpty()) {
            maxUploadSpeedStr = "不给力"
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .titleBar(R.id.title_bar)
            .init()

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = "网络测速"
        findViewById<TextView>(R.id.tv_wifi_name).text = WifiUtil.getCurrentWifiName()
        var bandwidth = (maxDownloadSpeed / 1024f * 8 / 1024f).toInt()
        if (bandwidth <= 0) {
            bandwidth = 1
        }
        findViewById<TextView>(R.id.tv_bandwidth).text = "用户相当于${bandwidth}M带宽"
        findViewById<TextView>(R.id.tv_network_delay).text = networkDelay
        findViewById<TextView>(R.id.tv_download_speed).text = maxDownloadSpeedStr
        findViewById<TextView>(R.id.tv_upload_speed).text = maxUploadSpeedStr

        when {
            bandwidth <= 2 -> {
                findViewById<TextView>(R.id.tv_can_do).text = "当前网络适合聊天"
                findViewById<ProgressBar>(R.id.pb_speed).progress = 2
            }
            bandwidth in 2..4 -> {
                findViewById<TextView>(R.id.tv_can_do).text = "当前网络适合上网"
                findViewById<ProgressBar>(R.id.pb_speed).progress = 4
            }
            bandwidth in 4..6 -> {
                findViewById<TextView>(R.id.tv_can_do).text = "当前网络适合玩游戏"
                findViewById<ProgressBar>(R.id.pb_speed).progress = 6
            }
            else -> {
                findViewById<TextView>(R.id.tv_can_do).text = "当前网络适合看视频"
                findViewById<ProgressBar>(R.id.pb_speed).progress = 8
            }
        }
        findViewById<TextView>(R.id.tv_start).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

    }


    override fun loadData() {

    }

    override fun initDataObserver() {

    }


    override fun getLayoutId(): Int {
        return R.layout.activity_speed_result
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}