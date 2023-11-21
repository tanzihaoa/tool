package com.tzh.tools.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.TextView
import com.tzh.tools.R

object AppUtil {

    val getSettingsTipView = {activity: Activity ->
        try {
            val curActivity = Utils.topActivityOrApp as Activity
            val decorView = curActivity.window.decorView
            val view = activity.layoutInflater.inflate(R.layout.layout_settings_tip, null)
            val tvTip = view.findViewById<TextView>(R.id.tv_tip)
            val reportIpBusinessId = MMKVUtil.get("id", "") as String
            tvTip.text = String.format("奖励申请中(%s)...请勿退出",reportIpBusinessId)
            if(decorView is ViewGroup){
                decorView.addView(view)
            }
        }catch (_:Exception){}
    }

    /**
     * 跳转到APP的设置页面
     * @param context
     */
    fun toSelfSettingActivity(context: Context) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }

    /**
     * Return whether the app is installed.
     *
     * @param context
     * @param packageName
     * @return
     */
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        return !TextUtils.isEmpty(packageName) && packageManager.getLaunchIntentForPackage(
            packageName
        ) != null
    }

}
