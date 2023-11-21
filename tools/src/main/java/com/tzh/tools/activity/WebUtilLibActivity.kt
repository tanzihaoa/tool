package com.tzh.tools.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityWebUtilBinding
import com.tzh.tools.util.BarUtil
import com.gyf.immersionbar.ImmersionBar


/**
 * 气温、空气、历史天气、台风路径、宝宝起名
 */
class WebUtilLibActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isAdResume = false

    companion object {
        //气温排行榜(默认)
        const val temperature = 1

        //台风路径
        const val typhoon = 2

        //历史天气
        const val history_weather = 3

        //空气排行榜
        const val air = 4

        //宝宝起名
        const val name = 5

        private const val URL_TYPE_KEY = "URL_TYPE_KEY"
        fun startActivity(
            context: Context,
            type: Int,
            @LayoutRes layoutResID: Int?,
            darkFront: Boolean = false
        ) {
            var intent = Intent(context, WebUtilLibActivity::class.java)
            intent.putExtra(URL_TYPE_KEY, type)

            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            darkFront?.let {
                intent.putExtra(KEY_DARK, darkFront)
            }
            context.startActivity(intent)
        }

        fun startActivity(context: Context, type: Int) {
            startActivity(context, type, null)
        }

        fun startActivity(
            context: Context,
            type: Int,
            showAd: Boolean,
            darkFront: Boolean,
            @LayoutRes layoutResID: Int?
        ) {
            val intent = Intent(context, WebUtilLibActivity::class.java)
            intent.putExtra(URL_TYPE_KEY, type)
            intent.putExtra("showAd", showAd)
            intent.putExtra(KEY_DARK, darkFront)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    //状态栏高度
    private val statusBarHeight = BarUtil.getStatusBarHeight()
    private var webView: WebView? = null
    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

    private fun getUrlType(): String {
        val type = intent.getIntExtra(URL_TYPE_KEY, 1)
        var title = "气温排行榜"
        var url = "https://waptianqi.2345.com/h5/rank/index.html?source=temperature"
        when (type) {
            air -> {
                url = "https://waptianqi.2345.com/h5/rank/index.html?source=air"
                title = "空气排行榜"
            }
            history_weather -> {
                url = "https://m.tianqi.com/lishi/"
                title = "历史天气"
            }
            typhoon -> {
                url = "http://typhoon.nmc.cn/mobile.html"
                title = "台风路径"
            }
            name -> {
                url = "https://zx.caijiexinxi.cn/baziqiming/index?channel=sw_fxqx_00002"
                title = "宝宝起名"
            }
        }
        findViewById<TextView>(R.id.tv_title).text = title
        return url
    }

    override fun initView() {
        val lp = findViewById<View>(R.id.web_top).layoutParams
        if (lp.height != statusBarHeight) {
            lp.height = statusBarHeight
            findViewById<View>(R.id.web_top).layoutParams = lp
        }

        if (darkFront) {
            ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .autoDarkModeEnable(true, 0.8f)
                .init()
        }

        webView = WebView(applicationContext)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView?.settings?.safeBrowsingEnabled = false
        }
        findViewById<View>(R.id.iv_back).setOnClickListener {
            finish()
        }
        findViewById<LinearLayout>(R.id.ll_adpage).addView(
            webView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        setWebViewSetting()

        webView?.loadUrl(getUrlType())
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
        if (intent.getBooleanExtra("showAd", true)) {
            if (!isAdResume) {
                isAdResume = true
                LibAdBridge.instance.startInterstitial(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.destroy()
        webView = null
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    private fun setWebViewSetting() {
        //支持获取手势焦点
        webView?.requestFocusFromTouch()

        val settings = webView?.settings

        // 缩放
        settings?.setSupportZoom(true)
        settings?.textZoom = 100
        settings?.builtInZoomControls = true
        //隐藏原生的缩放控件
        settings?.displayZoomControls = false

        //支持JS
        settings?.setJavaScriptEnabled(true)
        settings?.javaScriptCanOpenWindowsAutomatically = true
        // 适应屏幕
        settings?.useWideViewPort = true
        settings?.loadWithOverviewMode = true

        //支持内容重新布局
        settings?.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings?.supportMultipleWindows()
        settings?.setSupportMultipleWindows(false)
        //设置可访问文件
        settings?.allowFileAccess = true
        //当webview调用requestFocus时为webview设置节点
        settings?.setNeedInitialFocus(true)
        //支持自动加载图片
        settings?.loadsImagesAutomatically = true
        // 缓存
        settings?.databaseEnabled = true
        settings?.domStorageEnabled = true
        settings?.cacheMode = WebSettings.LOAD_DEFAULT
        webView?.setLayerType(View.LAYER_TYPE_NONE, null)
        // 允许https页面加载http资源
        settings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

    }

    override fun initDataObserver() {}
    override fun getLayoutId(): Int {
        return R.layout.activity_web_util
    }

}