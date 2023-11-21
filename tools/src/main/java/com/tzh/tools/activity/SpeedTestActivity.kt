package com.tzh.tools.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.arialyy.annotations.Download
import com.arialyy.annotations.Upload
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.arialyy.aria.core.task.UploadTask
import com.tzh.tools.R
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.customview.dashboard.ArcProgressView
import com.tzh.tools.util.*

import com.didichuxing.doraemonkit.util.ConvertUtils
import com.didichuxing.doraemonkit.util.PermissionUtils
import com.gyf.immersionbar.ImmersionBar
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * 网络测速
 */
class SpeedTestActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isTested = false

    private var pingProcess: Process? = null

    private var maxDownloadSpeed: Long = 0L
    private var maxDownloadSpeedStr: String = ""
    private var maxUploadSpeedStr: String = ""
    private var maxUploadSpeed: Long = 0L
    private var networkDelay = "460ms"

    private var showRewardVideo = false

    override fun initVar() {
        showRewardVideo = intent.getBooleanExtra("showRewardVideo", false)
    }

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, SpeedTestActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .titleBarMarginTop(R.id.title_bar)
            .init()

        val ivBack = findViewById<ImageView>(R.id.iv_back)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = "网络测速"

        findViewById<TextView>(R.id.tv_wifi_name).text = WifiUtil.getCurrentWifiName()
        findViewById<TextView>(R.id.tv_current_wifi_name).text = String.format(findViewById<TextView>(R.id.tv_wifi_name).text.toString(),WifiUtil.getCurrentWifiName())

        startTestSpeed()

        findViewById<TextView>(R.id.tv_start).clickDelay {
            startTestSpeed()
        }

        findViewById<TextView>(R.id.tv_test).clickDelay {
            finish()
        }
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }



    override fun loadData() {
    }

    private fun handlerTestResult() {
        val intent = Intent(this, SpeedResultActivity::class.java)
        intent.putExtra("networkDelay", networkDelay)
        intent.putExtra("maxDownloadSpeed", maxDownloadSpeed)
        intent.putExtra("maxDownloadSpeedStr", maxDownloadSpeedStr)
        intent.putExtra("maxUploadSpeedStr", maxUploadSpeedStr)
        startActivity(intent)
        finish()
    }

    private fun startTestSpeed() {
        PermissionX.init(this).permissions(
            Manifest.permission.READ_EXTERNAL_STORAGE, //网络测速
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).request { allGranted, _, _ ->
            if (allGranted) {
                ViewUtil.show(findViewById(R.id.ll_main))
                ViewUtil.show(findViewById(R.id.tv_test_name))
                ViewUtil.show(findViewById(R.id.tv_test))
                maxDownloadSpeed = 0L
                maxUploadSpeed = 0L
                findViewById<TextView>(R.id.tv_network_delay).text = "--"
                findViewById<TextView>(R.id.tv_download_speed).text = "--/s"
                findViewById<TextView>(R.id.tv_upload_speed).text = "--/s"
                findViewById<TextView>(R.id.tv_test_name).text = "检测网络延时"

                TestSpeed.instance.startTestSpeed { bean, type ->
                    if (isDestroyed){
                        return@startTestSpeed
                    }
                    when(type){
                        1->{
                            Log.e("TAG", "startTestSpeed: $bean", )
                            //下载速度
                            val toLong = (bean.listDownloadingSpeed[bean.listDownloadingSpeed.size-1])
                            maxDownloadSpeed = toLong
                            findViewById<TextView>(R.id.tv_current_speed).text = ConvertUtils.byte2FitMemorySize(toLong,2)
                            maxDownloadSpeedStr = ConvertUtils.byte2FitMemorySize(toLong,2)
                            findViewById<TextView>(R.id.tv_download_speed).text = ConvertUtils.byte2FitMemorySize(toLong,2)
                            findViewById<ArcProgressView>(R.id.dash_board_view).setAngle(calculateDashboardAngle(toLong.toFloat()))
                        }
                        2->{
                            //下载速度完成

                        }
                        3->{
                            LogUtil.e("测试下载速度: 失败")
                        }
                        4->{
                            //上传速度
                            val toLong = (bean.listUploadSpeed[bean.listUploadSpeed.size-1])
                            maxUploadSpeed = toLong
                            findViewById<TextView>(R.id.tv_upload_speed).text =  ConvertUtils.byte2FitMemorySize(toLong,2)
                            maxUploadSpeedStr = ConvertUtils.byte2FitMemorySize(toLong,2)
                            findViewById<TextView>(R.id.tv_current_speed).text = ConvertUtils.byte2FitMemorySize(toLong,2)
                            findViewById<ArcProgressView>(R.id.dash_board_view).setAngle(calculateDashboardAngle(toLong.toFloat()))
                        }
                        5->{
                            //上传完成

                        }
                        6->{
                            LogUtil.e("测试上传速度: 失败")
                        }
                        8->{
                            networkDelay = "${bean.netDelay}ms"
                            findViewById<TextView>(R.id.tv_network_delay).text = networkDelay
                        }
                        9->{
                            LogUtil.e("网络测速完成: $bean")
                            handleTestResult()
                            handlerTestResult()
                        }
                    }
                }

            }
        }
    }

    private fun handleTestResult() {
        isTested = true
        findViewById<TextView>(R.id.tv_test).text = "发现精彩生活"
        var bandwidth = (maxDownloadSpeed / 1024f * 8 / 1024f).toInt()
        if (bandwidth <= 0) {
            bandwidth = 1
        }
        findViewById<TextView>(R.id.tv_bandwidth).text = "用户相当于${bandwidth}M带宽"
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
    }

    override fun initDataObserver() {
    }

    private fun calculateDashboardAngle(speed: Float): Float {
        val angle = when (speed) {
            in 0.0..2.0 -> {
                60 + speed * 40
            }
            in 2.0..5.0 -> {
                140 + (speed - 2) / 0.6f * 8
            }
            in 5.0..10.0 -> {
                180 + (speed - 5) / 1 * 8
            }
            in 10.0..30.0 -> {
                220 + (speed - 10) / 2 * 8
            }
            else -> {
                300f
            }
        }
        return angle
    }




    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        TestSpeed.instance.stopTestSpeed()
        pingProcess?.destroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_speed_test
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}