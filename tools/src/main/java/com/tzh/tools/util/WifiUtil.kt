package com.tzh.tools.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresPermission
import com.tzh.tools.Tools
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStreamReader
import java.util.*


/**
 * Created by xwm on 2021/8/25
 */
object WifiUtil {

    private val wifiManager by lazy {
        com.tzh.tools.Tools.app.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    /**
     * 是否开启WiFi
     */
    fun isEnabled(): Boolean {
        return wifiManager.isWifiEnabled
    }

    /**
     * 获取当前连接的WiFi信息
     */
    fun getConnectionInfo(): WifiInfo {
        return wifiManager.connectionInfo
    }

    fun getCurrentWifiName(): String {
        if (!NetworkUtil.isWifiData())
            return ""
        val info = wifiManager.connectionInfo
        val wifiName = info.ssid.replace("\"", "")
        if (wifiName == "<unknown ssid>") {
            return "未知网络"
        }
        return wifiName
    }

    @SuppressLint("MissingPermission")
    fun getConnectedList(): MutableList<WifiConfiguration> {
        var list = mutableListOf<WifiConfiguration>()
        runCatching {
            val tempList: MutableList<WifiConfiguration>? = wifiManager.configuredNetworks
            tempList
        }.onSuccess {
            if (it != null) {
                list = it
            }
        }
        return list
    }

    fun switchWifi(state: Boolean): Boolean {
        return wifiManager.setWifiEnabled(state)
    }


    /**
     * 移除输入错误密码的WiFi配置
     */
    fun removeErrorNetwork(netId: Int) {
        wifiManager.removeNetwork(netId)
    }

    fun startScan(): Boolean {
        return if (!isEnabled())
            false
        else
            wifiManager.startScan()
    }



    fun getIPAddress(): String {
        val info = getConnectionInfo()
        val sb = StringBuilder()
        sb.append(info.ipAddress and 0xFF).append(".")
        sb.append(info.ipAddress shr 8 and 0xFF).append(".")
        sb.append(info.ipAddress shr 16 and 0xFF).append(".")
        sb.append(info.ipAddress shr 24 and 0xFF)
        return sb.toString()
    }

    /**
     * 获取 WiFi 安全性
     */
    fun getWifiSecurityType(capabilities: String): String {
        // 如果包含WAP-PSK的话，则为WAP加密方式
        if (capabilities.contains("WPA-PSK") || capabilities.contains("WPA2-PSK")) {
            return "WPA/WPA2 PSK"
        } else if (capabilities.contains("WPA2-EAP")) {
            return "WPA2-EAP"
        } else if (capabilities.contains("WEP")) {
            return "WEP"
        } else if (capabilities.contains("ESS")) {
            // 如果是ESS则没有密码
            return "Open"
        }
        return "未知"
    }

    enum class WifiCapability {
        WEP, WPA, NO_PASSWORD
    }

}