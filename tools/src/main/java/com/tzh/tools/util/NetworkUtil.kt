package com.tzh.tools.util

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.RequiresPermission
import com.tzh.tools.Tools


/**
 * @author Created by Adam on 2018-11-08
 */
object NetworkUtil {

    private val connectivityManager by lazy {
        com.tzh.tools.Tools.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    @RequiresPermission(ACCESS_NETWORK_STATE)
    private fun getActiveNetworkInfo(): NetworkInfo? {
        return connectivityManager.activeNetworkInfo
    }

    /**
     * Return whether network is connected.
     *
     * Must hold `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return `true`: connected<br></br>`false`: disconnected
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean {
        val info = getActiveNetworkInfo()
        return info != null && info.isConnected
    }

    /**
     * Return whether using mobile data.
     *
     * Must hold `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return `true`: yes<br></br>`false`: no
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isMobileData(): Boolean {
        val info = getActiveNetworkInfo()
        return (null != info && info.isAvailable
                && info.type == ConnectivityManager.TYPE_MOBILE)
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isWifiData(): Boolean {
        val info = getActiveNetworkInfo()
        return (null != info && info.isAvailable
                && info.type == ConnectivityManager.TYPE_WIFI)
    }
}
