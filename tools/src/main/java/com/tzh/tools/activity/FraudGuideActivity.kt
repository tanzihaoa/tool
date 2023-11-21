package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar

class FraudGuideActivity : BaseLibActivity<BaseViewModel<*>>() {

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            val intent = Intent(context, FraudGuideActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init()
        findViewById<TextView>(R.id.tv_title).text = "互联网防诈骗指南"
        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_fraud_guide
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}