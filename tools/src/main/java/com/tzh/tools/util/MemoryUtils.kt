package com.tzh.tools.util

import android.app.ActivityManager
import android.content.Context
import java.math.BigDecimal

class  MemoryUtils {
    companion object {
        val instance: MemoryUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MemoryUtils()
        }
    }

    var activityManager: ActivityManager? = null
    var memoryInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()


    fun getMemoryPercentage(context: Context): String {
        if(null == activityManager){
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE)
            if(manager is ActivityManager)activityManager = manager
        }
        activityManager?.getMemoryInfo(memoryInfo)
        LogUtil.i(">>>availMem ${memoryInfo.availMem/(1024*1024)}  totalMem ${memoryInfo.totalMem/(1024*1024)}   threshold ${memoryInfo.threshold/(1024*1024)}")
        return "${BigDecimal((memoryInfo.availMem.toDouble()/memoryInfo.totalMem.toDouble())*100).setScale(0,BigDecimal.ROUND_HALF_UP)}"
    }

    fun getAvailMem(context: Context):String{
        if(null == activityManager){
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE)
            if(manager is ActivityManager)activityManager = manager
        }
        activityManager?.getMemoryInfo(memoryInfo)
        return "${BigDecimal(memoryInfo.availMem/(1024*1024*1024)).setScale(1)}G"
    }

    fun getTotalMem(context: Context):String{
        if(null == activityManager){
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE)
            if(manager is ActivityManager)activityManager = manager
        }
        activityManager?.getMemoryInfo(memoryInfo)
        return "${BigDecimal(memoryInfo.totalMem/(1024*1024*1024)).setScale(1)}G"
    }
}