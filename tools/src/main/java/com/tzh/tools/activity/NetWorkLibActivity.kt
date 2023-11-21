package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.wifi.WifiManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseActivity
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityNetWorkNewBinding
import com.tzh.tools.model.NetWorkWifiBean
import com.tzh.tools.util.WifiUtils
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 网络诊断
 */
class NetWorkLibActivity : BaseActivity<BaseViewModel<*>, ActivityNetWorkNewBinding>() {

    private var isAdResume = false

    private var type = 0

    private lateinit var wifiManager: WifiManager

    override fun onResume() {
        super.onResume()
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            if (intent.getIntExtra(GOTO_TYPE, 0) == 1) {
                LibAdBridge.instance.startInterstitial(this)
            } else if (intent.getIntExtra(GOTO_TYPE, 0) == 2) {
                LibAdBridge.instance.startRewardVideo(this)
            }
        }
    }

    companion object {

        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            val intent = Intent(context, NetWorkLibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(BaseLibActivity.KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE, adType)
            context.startActivity(intent)
        }
    }


    override fun initView() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()
        type = intent.getIntExtra(GOTO_TYPE, 0)
        mDataBinding.top.findViewById<TextView>(R.id.tv_title).text = "网络诊断"
        mDataBinding.top.findViewById<TextView>(R.id.tv_title).setTextColor(Color.WHITE)
        mDataBinding.top.findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }
        mDataBinding.top.findViewById<ImageView>(R.id.iv_back)
            .setImageResource(R.mipmap.ic_left_in_white)
        rotationView()
        mDataBinding.tvRestart.clickDelay(500) {
            mDataBinding.root.findViewById<FrameLayout>(R.id.fl_outer).visibility = View.VISIBLE
            mDataBinding.tvRestart.visibility = View.GONE
            mDataBinding.ivNetState.visibility = View.GONE
            mDataBinding.lvContent.visibility = View.VISIBLE
            rotationView()
            getData(true)
        }
    }

    private fun rotationView() {

        mDataBinding.tvResult.visibility =
            View.GONE

        mDataBinding.tvResult2.visibility =
            View.GONE

        mDataBinding.tvResult3.visibility =
            View.GONE

        mDataBinding.ivRotation.post {
            val aRotateAnimation = RotateAnimation(
                0f,
                360f,
                Animation.ABSOLUTE,
                (mDataBinding.ivRotation.width / 2).toFloat(),
                Animation.ABSOLUTE,
                (mDataBinding.ivRotation.height / 2).toFloat()
            )

            aRotateAnimation.duration = 500
            aRotateAnimation.repeatCount = -1
            mDataBinding.ivRotation.startAnimation(aRotateAnimation)
        }
        mDataBinding.ivRotation2.post {
            val aRotateAnimation = RotateAnimation(
                0f,
                360f,
                Animation.ABSOLUTE,
                (mDataBinding.ivRotation2.width / 2).toFloat(),
                Animation.ABSOLUTE,
                (mDataBinding.ivRotation2.height / 2).toFloat()
            )

            aRotateAnimation.duration = 500
            aRotateAnimation.repeatCount = -1
            mDataBinding.ivRotation2.startAnimation(aRotateAnimation)
        }
        mDataBinding.ivRotation3.post {
            val aRotateAnimation = RotateAnimation(
                0f,
                360f,
                Animation.ABSOLUTE,
                (mDataBinding.ivRotation3.width / 2).toFloat(),
                Animation.ABSOLUTE,
                (mDataBinding.ivRotation3.height / 2).toFloat()
            )

            aRotateAnimation.duration = 500
            aRotateAnimation.repeatCount = -1
            mDataBinding.ivRotation3.startAnimation(aRotateAnimation)
        }
    }

    override fun initDataObserver() {
    }

    @SuppressLint("MissingPermission")
    private fun getWifiSSid(): String {
        val wifiInfo = wifiManager.connectionInfo
        var SSID = wifiInfo.ssid
        val networkId = wifiInfo.networkId
        val configuredNetworks = wifiManager.configuredNetworks
        for (wifiConfiguration in configuredNetworks) {
            if (wifiConfiguration.networkId == networkId) {
                SSID = wifiConfiguration.SSID
            }
        }
        return SSID.replace("\"", "")
    }

    private fun getData(isRepeat: Boolean) {

        launch {
            repeat(3) {
                delay(2000)
                when (it) {
                    0 -> {
                        mDataBinding.ivRotation.clearAnimation()
                        mDataBinding.ivRotation.visibility = View.GONE
                        mDataBinding.tvResult.visibility = View.VISIBLE
                    }

                    1 -> {
                        mDataBinding.ivRotation2.clearAnimation()
                        mDataBinding.ivRotation2.visibility = View.GONE
                        mDataBinding.tvResult2.visibility = View.VISIBLE
                    }

                    2 -> {
                        mDataBinding.ivRotation3.clearAnimation()
                        mDataBinding.ivRotation3.visibility = View.GONE
                        mDataBinding.tvResult3.visibility = View.VISIBLE

                        mDataBinding.lvContent.visibility = View.GONE
                        mDataBinding.tvRestart.visibility = View.VISIBLE
                        mDataBinding.ivNetState.visibility = View.VISIBLE

                    }
                }
            }

        }

        if (!this::wifiManager.isInitialized) {
            wifiManager =
                applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        }
        mDataBinding.tvContent.text = getWifiSSid()
        val ipAddress = WifiUtils.getIPAddress(this)
        if (ipAddress != null && ipAddress is NetWorkWifiBean) {
            mDataBinding.tvContent2.text = "${ipAddress.intensity}dbm"
        }
        WifiUtils.getDelayedNet("www.baidu.com", 5) {
            mDataBinding.tvContent3.text = "${it}ms"
        }
    }

    override fun loadData() {
        getData(false)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_net_work_new
    }
}