package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tzh.tools.R
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.constants.Constants
import com.tzh.tools.model.WebViewEnum
import com.tzh.tools.view.FixedWebView
import com.gyf.immersionbar.ImmersionBar

/**
 * @author: Created by zfj
 * created on: 2023/5/16
 * description:气温、空气、台风路径
 */
class WebViewActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var gobackBtn: ImageView? = null
    private var webView: FixedWebView? = null
    private var mTitleView: TextView? = null

    //反作弊
    private var url: String? = null

    private var ivClose: ImageView? = null
    private var mPageLayout: LinearLayout? = null

    companion object {
        private const val INTENT_KEY = "url"
        fun startActivity(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(INTENT_KEY, url)
            context.startActivity(intent)
        }
        fun startActivity(context: Context, webViewEnum: WebViewEnum) {
            val intent = Intent(context, WebViewActivity::class.java)
            val url = when(webViewEnum){
                WebViewEnum.AIR ->{
                    Constants.AIR_URL
                }
                WebViewEnum.TEMPERATURE ->{
                    Constants.TEMPERATURE_URL
                }
                WebViewEnum.TYPHOON ->{
                    Constants.TYPHOON_URL
                }
            }

            intent.putExtra(INTENT_KEY, url)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .titleBar(findViewById(R.id.rl_title))
            .init()
        mPageLayout = findViewById(R.id.ll_adpage)
        webView = FixedWebView(applicationContext)

        try {
            val intent = intent
            url = intent.getStringExtra(INTENT_KEY)
        } catch (e: Exception) {
        }
        webView?.loadUrl(url!!)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView?.getSettings()?.safeBrowsingEnabled = false
        }
        ivClose = findViewById(R.id.iv_web_close)
        ivClose?.setOnClickListener(View.OnClickListener { finish() })
        mPageLayout?.addView(webView, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        gobackBtn = findViewById<View>(R.id.iv_sgad_back) as ImageView
        mTitleView = findViewById<View>(R.id.tv_sgad_title) as TextView
        gobackBtn?.setOnClickListener(View.OnClickListener { finish() })
        webView?.let {
            setWebViewSetting(it)
        }

    }

    private fun setWebViewSetting(webView: WebView) {
        //支持获取手势焦点
        webView.requestFocusFromTouch()
        val settings = webView.settings

        // 缩放
        settings.setSupportZoom(true)
        settings.textZoom = 100
        settings.builtInZoomControls = true
        //隐藏原生的缩放控件
        settings.displayZoomControls = false

        //支持JS
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        // 适应屏幕
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        //支持内容重新布局
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.supportMultipleWindows()
        settings.setSupportMultipleWindows(false)
        //设置可访问文件
        settings.allowFileAccess = true
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true)
        //支持自动加载图片
        settings.loadsImagesAutomatically = true
        // 缓存
        settings.databaseEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.setLayerType(View.LAYER_TYPE_NONE, null)
        settings.setAppCacheEnabled(true)
        // 允许https页面加载http资源
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("weixin://wap/pay?")) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    return true
                }
                return false
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                setTitle(title)
            }

            override fun onProgressChanged(view: WebView, progress: Int) {
                super.onProgressChanged(view, progress)
            }
        }
    }

    private fun setTitle(title: String) {
        if (!TextUtils.isEmpty(title) && !title.startsWith("http") && !title.startsWith("www.")) {
            mTitleView!!.text = title
        } else {
            mTitleView!!.text = ""
        }
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_webview_sg
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}