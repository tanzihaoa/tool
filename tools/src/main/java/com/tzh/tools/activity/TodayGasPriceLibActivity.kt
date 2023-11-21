package com.tzh.tools.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.adapter.GasPriceAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityTodayGasPriceBinding
import com.tzh.tools.model.GasBean
import com.tzh.tools.net.apiLib
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar

import kotlinx.coroutines.launch


/**
 * 今日油价
 */
class TodayGasPriceLibActivity : BaseLibActivity<BaseViewModel<*>>() {
//    private val adBridge by lazy { SQAdBridge(this) }
    private var isAdResume = false
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
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, TodayGasPriceLibActivity::class.java)

            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }

        fun startActivity(
            context: Context,
            showAd: Boolean,
            darkFront: Boolean,
            @LayoutRes layoutResID: Int?
        ) {
            val intent = Intent(context, TodayGasPriceLibActivity::class.java)
            intent.putExtra("showAd", showAd)
            intent.putExtra(KEY_DARK, darkFront)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }
    override fun getViewModel(): Class<BaseViewModel<*>> {
        return  BaseViewModel::class.java
    }
    private var priceAdapter: GasPriceAdapter? = null
    private val priceList = mutableListOf<GasBean>()

    override fun initView() {
        if (darkFront) {
            ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init()
        }

        findViewById<View>(R.id.top).findViewById<TextView>(R.id.tv_title).text = "今日油价"
        launch {
            kotlin.runCatching {
                val params = hashMapOf<String, String>()
                apiLib.todayOilPrice(params)
            }.onSuccess {
                if (it.code == 200) {
                    it.data.list.forEach { bean ->
                        priceList.add(bean)
                    }
                    priceAdapter = GasPriceAdapter(priceList)
                    findViewById<RecyclerView>(R.id.rcv).adapter = priceAdapter
                }
            }.onFailure {
                Log.d("----==",it.message.toString())
            }
        }
        findViewById<View>(R.id.top).findViewById<ImageView>(R.id.iv_back).clickDelay { finish() }
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_today_gas_price
    }
}