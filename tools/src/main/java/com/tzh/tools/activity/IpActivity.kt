package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.model.CheckEnum
import com.tzh.tools.model.NetWorkWifiBean
import com.tzh.tools.net.apiLib
import com.tzh.tools.util.WifiUtils
import com.tzh.tools.util.clickDelay
import com.didichuxing.doraemonkit.util.RegexUtils
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * IP与手机归属地查询
 */
class IpActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isAdResume = false
    private var phoneImage = 0
    override fun onResume() {
        super.onResume()
        if (intent.getBooleanExtra("showAd", true)) {
            if (!isAdResume) {
                isAdResume = true
                LibAdBridge.instance.startInterstitial(this)
            }
        }

    }

    companion object {

        const val PHONE_KEY = "PHONE_KEY"
        fun startActivity(context: Context, type: CheckEnum, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, IpActivity::class.java)
            intent.putExtra("type", type)
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT and Intent.FLAG_ACTIVITY_NEW_TASK)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }

        fun startActivity(
            context: Context,
            type: CheckEnum,
            showAd: Boolean,
            @LayoutRes layoutResID: Int?,
        ) {
            val intent = Intent(context, IpActivity::class.java)
            intent.putExtra("type", type)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, it)
            }
            intent.putExtra("showAd", showAd)
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT and Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun startActivity(
            context: Context,
            type: CheckEnum,
            @LayoutRes layoutResID: Int?,
            phoneImage: Int?
        ) {
            val intent = Intent(context, IpActivity::class.java)
            intent.putExtra("type", type)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, it)
            }
            intent.putExtra(PHONE_KEY, phoneImage)
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT and Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .statusBarDarkFont(true)
            .init()

        phoneImage = intent.getIntExtra(PHONE_KEY, 0)

        val etOther = findViewById<TextView>(R.id.et_other)
        val tvIp = findViewById<TextView>(R.id.tv_ip)
        val tvCity = findViewById<TextView>(R.id.tv_city)
        val tvType = findViewById<TextView>(R.id.tv_type)
        val tvOther = findViewById<TextView>(R.id.tv_other)
        val ivMain = findViewById<ImageView>(R.id.iv_main)
        when (intent.getSerializableExtra("type") as CheckEnum) {
            CheckEnum.IP -> {
                findViewById<TextView>(R.id.tv_title).text =
                    "IP地址查询"
                findViewById<View>(R.id.tv_click).clickDelay(5000) {

                    if (!RegexUtils.isIP(etOther.text)) {
                        showToast("请确认是否输入正确IP地址")
                        return@clickDelay
                    }

                    launch(Dispatchers.IO) {
                        runCatching {
                            apiLib.ipGetCity(hashMapOf("ip" to etOther.text.toString()))
                        }.onSuccess {
                            if (it.code == 200) {
                                withContext(Dispatchers.Main) {
                                    tvIp.text = etOther.text.toString()
                                    tvCity.text =
                                        "${it.data.country}-${it.data.province}-${it.data.city} : ${it.data.isp}"
                                }
                            }
                        }.onFailure {
                            showToast("Ip地址查询失败")
                        }
                    }
                }

                val ipAddress = WifiUtils.getIPAddress(this)
                if (ipAddress != null && ipAddress is NetWorkWifiBean) {
                    tvIp.text = ipAddress.ip
                    launch(Dispatchers.IO) {
                        runCatching {
                            apiLib.ipGetCity(hashMapOf("ip" to ipAddress.ip))
                        }.onSuccess {
                            if (it.code == 200) {
                                withContext(Dispatchers.Main) {
                                    tvIp.text = etOther.text.toString()
                                    tvCity.text =
                                        "${it.data.country}-${it.data.province}-${it.data.city} : ${it.data.isp}"
                                }
                            }
                        }
                    }

                }

            }

            CheckEnum.PHONE -> {
                findViewById<TextView>(R.id.tv_title).text =
                    "手机归属地查询"
                if (phoneImage != 0) {
                    ivMain.setImageResource(phoneImage)
                } else {
                    ivMain.setImageResource(R.drawable.bg_phone)
                }

                tvType.text = "当前归属地"
                tvOther.text = "其他手机号"
                etOther.hint = "请输入手机号"
                tvIp.text = "__"
                tvCity.text = "----"
                findViewById<View>(R.id.tv_click).clickDelay(5000) {

                    if (!RegexUtils.isMobileExact(etOther.text)) {
                        showToast("请确认是否输入正确手机号")
                        return@clickDelay
                    }

                    launch(Dispatchers.IO) {
                        runCatching {
                            apiLib.getMobileInfo(hashMapOf("mobile" to etOther.text.toString()))
                        }.onSuccess {
                            if (it.code == 200) {
                                withContext(Dispatchers.Main) {
                                    tvIp.text = etOther.text.toString()
                                    tvCity.text =
                                        "${it.data.province}-${it.data.city} : ${it.data.company}"
                                }
                            }
                        }.onFailure {
                            showToast("手机号归属地查询失败")
                        }
                    }

                }
            }
        }
        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }
    }


    override fun initDataObserver() {
    }

    override fun loadData() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lib_ip
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}