package com.tzh.tools.view

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView
import androidx.annotation.RequiresApi

/**
 * 修复创建WebView时，在部分Android5.x系统上会崩溃的问题
 * Created by xwm on 2019-11-15
 */
class FixedWebView : WebView {

    companion object {
        private fun fixedContextFowWebView(context: Context): Context {
            return if (Build.VERSION.SDK_INT in 21..22) {
                context.createConfigurationContext(Configuration())
            } else {
                context
            }
        }
    }

    constructor(context: Context) : super(fixedContextFowWebView(context))

    constructor(context: Context, attrs: AttributeSet) : super(
        fixedContextFowWebView(context),
        attrs
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        fixedContextFowWebView(context),
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        fixedContextFowWebView(context),
        attrs,
        defStyleAttr,
        defStyleRes
    )
}
