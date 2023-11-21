package com.tzh.tools.util

/**
 * 下载回调
 */
interface AriaDownloadCallback {
    fun onDownloadProgress(progress:Int,speed:Long)
    fun onDownloadComplete(isOK:Boolean)
}