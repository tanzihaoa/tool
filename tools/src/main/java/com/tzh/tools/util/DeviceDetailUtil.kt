package com.tzh.tools.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.WindowManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

object DeviceDetailUtil {

    /** 计算屏幕尺寸 */
    fun getMeasure(height: Int, width: Int, dpi: Int): String {
        val x: Double =
            kotlin.math.sqrt((((height * height) + (width * width)) / (dpi * dpi)).toDouble());
        val decimalFormat = DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        val str = decimalFormat.format(x);  //format 返回的是字符串
        return str;   //返回屏幕的宽度
    }

    /** 获取当前屏幕的高度 */
    fun getScreenHeight(context: Context): Int {
        //获取系统服务
        val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        val dm: DisplayMetrics = DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);  //获取显示参数保存到dm中
        return dm.heightPixels;   //返回屏幕的宽度
    }

    /** 获取当前屏幕的宽度 */
    fun getScreenWidth(context: Context): Int {
        //获取系统服务
        val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        val dm: DisplayMetrics = DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);  //获取显示参数保存到dm中
        return dm.widthPixels;   //返回屏幕的宽度
    }

    /** cpu架构 */
    fun getCPUAbi(): String {
        val os_cpuabi = BufferedReader(
            InputStreamReader(
                Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream()
            )
        ).readLine()
        return if (os_cpuabi.contains("x86")) {
            "x86"
        } else if (os_cpuabi.contains("armeabi-v7a")) {
            "armeabi-v7a"
        } else if (os_cpuabi.contains("arm64-v8a")) {
            "arm64-v8a"
        } else {
            "armeabi"
        }
    }

    /** 设备开机时间 */
    fun getSystemStartupTime(): String {

        val time = SystemClock.elapsedRealtime()
//        val time = System.currentTimeMillis() - SystemClock.elapsedRealtime()
        val day = (floor((time/(1000*60*60*24)).toDouble())).toInt()
        val hours = (floor(((time/(1000*60*60))%24).toDouble())).toInt()
        val minutes = (floor(((time/(1000*60))%60).toDouble())).toInt()
        val seconds = (floor(((time/1000)%60).toDouble())).toInt()
        return "${day}天${hours}小时${minutes}分钟${seconds}秒"
//        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        val d1 = Date(time)
//        return format.format(d1);
    }

    /** 获取当前网络连接的类型信息 */
    fun getConnectedType(context: Context): String {
        val mConnectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
        val mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            return when (mNetworkInfo.getType()) {
                0 -> "数据流量"
                1 -> "WIFI"
                else -> "未知网络"
            }
        }
        return "无网络"
    }

    /** 获取手机内部存储空间，以M,G为单位的容量 */
    fun getInternalMemorySize(): String {
        val file = Environment.getDataDirectory()
        val statFs = StatFs(file.getPath())
        val blockSizeLong = statFs.getBlockSizeLong()
        val blockCountLong = statFs.getBlockCountLong()
        val size = blockCountLong * blockSizeLong
//        return Formatter.formatFileSize(context, size);
        return "${BigDecimal(size/(1024*1024*1024)).setScale(2)}G"
    }

    /** 获取手机内部可用存储空间，以M,G为单位的容量 */
    fun getAvailableInternalMemorySize(): String {
        val file = Environment . getDataDirectory ()
        val statFs =  StatFs(file.getPath())
        val availableBlocksLong = statFs . getAvailableBlocksLong ()
        val blockSizeLong = statFs . getBlockSizeLong ()
//        return Formatter.formatFileSize(context, availableBlocksLong * blockSizeLong)
        return "${BigDecimal((availableBlocksLong * blockSizeLong)/(1024*1024*1024)).setScale(2)}G"
    }
    /** 已使用空间 */
    fun getSdLave():String{
        val file = Environment . getDataDirectory ()
        val statFs =  StatFs(file.getPath())

        val blockSizeLong = statFs.getBlockSizeLong()
        val blockCountLong = statFs.getBlockCountLong()
        val size = blockCountLong * blockSizeLong

        val availableBlocksLong = statFs . getAvailableBlocksLong ()

        return "${BigDecimal((size-availableBlocksLong * blockSizeLong)/(1024*1024*1024))}G"
    }
    /** 剩余空间（使用率） */
    fun getSdUsage():String{
        val file = Environment . getDataDirectory ()
        val statFs =  StatFs(file.getPath())

        val blockSizeLong = statFs.getBlockSizeLong()
        val blockCountLong = statFs.getBlockCountLong()
        val size = blockCountLong * blockSizeLong

        val availableBlocksLong = statFs . getAvailableBlocksLong ()
        return "${((availableBlocksLong * blockSizeLong*100)/size)}%"
    }

}