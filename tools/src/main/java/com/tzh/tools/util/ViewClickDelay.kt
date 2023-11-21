package com.tzh.tools.util

import android.view.View

/**
 * Created by xwm on 2021/7/19
 */
object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
    const val SPACE_TIME: Long = 2000
}

fun View.clickDelay(
    delayTime: Long = ViewClickDelay.SPACE_TIME,
    clickAction: (view: View) -> Unit
) {
    this.setOnClickListener {
        if (this.hashCode() != ViewClickDelay.hash) {
            ViewClickDelay.hash = this.hashCode()
            ViewClickDelay.lastClickTime = System.currentTimeMillis()
            clickAction(it)
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - ViewClickDelay.lastClickTime > delayTime) {
                ViewClickDelay.lastClickTime = System.currentTimeMillis()
                clickAction(it)
            }
        }
    }
}