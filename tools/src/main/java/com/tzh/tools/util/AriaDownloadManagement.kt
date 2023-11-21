package com.tzh.tools.util

import android.content.Context
import android.os.Environment
import com.arialyy.annotations.Download
import com.arialyy.annotations.Upload
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.inf.TaskSchedulerType
import com.arialyy.aria.core.task.DownloadTask
import com.arialyy.aria.core.task.UploadTask
import com.didichuxing.doraemonkit.util.FileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * 下载管理工具
 * TTTT
 */
class AriaDownloadManagement {
    private val TAG = AriaDownloadManagement::class.java.simpleName
    lateinit var context: Context
    //设置选中壁纸存放路径
    var DOWNLOAD_SELECT_DIR_PATH = ""
    var downloadTask: DownloadTask? =null
    var uploadTask: UploadTask? =null

    companion object {
        val instance: AriaDownloadManagement by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AriaDownloadManagement()
        }
    }

    //设置文件下载目录
    var DOWNLOAD_DIR_PATH = ""

    //设置选中动态壁纸存放路径
    var DOWNLOAD_SELECT_DYNAMIC_DIR_PATH = ""

    //单独文件下载目录
    var DOWNLOAD_ONE_DIR_PATH = ""

    //存入下载回调
    private val backDownloadHashMap = HashMap<String, AriaDownloadCallback>()

    //存入上传回调
    private val backUploadHashMap = HashMap<String, AriaDownloadCallback>()

    /**
     * 初始化下载
     */
    fun initDownload(context: Context) {
        val config = Aria.init(context)
        this.context = context
        config.downloadConfig.isConvertSpeed = true
        config.uploadConfig.isConvertSpeed = true
        DOWNLOAD_DIR_PATH = context.cacheDir.absolutePath
//        val externalCacheDir = context.externalCacheDir
//        DOWNLOAD_DIR_PATH = if (null != externalCacheDir) {
//            externalCacheDir.absolutePath
//        } else {
//            context.cacheDir.absolutePath
//        }
        DOWNLOAD_DIR_PATH = DOWNLOAD_DIR_PATH + File.separator + "download_file" + File.separator
        DOWNLOAD_SELECT_DYNAMIC_DIR_PATH =
            context.filesDir.absolutePath + File.separator + "selectWallpaperVideo" + File.separator
        DOWNLOAD_SELECT_DIR_PATH =
            context.filesDir.absolutePath + File.separator + "selectWallpaperImage" + File.separator
        val file = File(DOWNLOAD_DIR_PATH)
        FileUtils.createOrExistsDir(file)
        FileUtils.createOrExistsDir(DOWNLOAD_SELECT_DYNAMIC_DIR_PATH)
        FileUtils.createOrExistsDir(DOWNLOAD_SELECT_DIR_PATH)
        //清理文件
        clearFile()
        Aria.get(context).downloadConfig.maxTaskNum = 1
        Aria.get(context).downloadConfig.updateInterval = 500
        Aria.get(context).uploadConfig.maxTaskNum = 1
        Aria.get(context).uploadConfig.reTryNum = 2
        Aria.download(this).register()
        Aria.upload(this).register()
        val sdPath =
            Environment.getExternalStorageDirectory().absoluteFile.absolutePath + File.separator + "Download" + File.separator
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
//            //有存储卡
            DOWNLOAD_ONE_DIR_PATH = sdPath + "wallpaper" + File.separator
            FileUtils.createOrExistsDir(DOWNLOAD_ONE_DIR_PATH)
        }
    }

    //清理文件
    fun clearFile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(DOWNLOAD_DIR_PATH)
                val files = FileUtils.listFilesInDir(file, true)
                if (null != files && files.size > 0) {
                    for (item in files) {
                        if (item.isFile) item.delete()
                    }
                }
            } catch (ee: Exception) {
                ee.printStackTrace()
            }
        }
    }


    @Download.onTaskRunning
    fun onTaskRunning(task: DownloadTask?) {
        this.downloadTask =task
        val url = task?.key
        if (null != url) {
            if (backDownloadHashMap.containsKey(url)) {
                val progress = task.percent
                val speed = task.speed
                LogUtil.e(TAG, "下载：$url   进度：$progress  速度：$speed")
                backDownloadHashMap[url]?.onDownloadProgress(progress, speed)
            }
        }
    }

    @Upload.onTaskRunning
    fun onTaskUploadRunning(task: UploadTask?) {
        this.uploadTask =task
        val url = task?.key
        if (null != url) {
            if (backUploadHashMap.containsKey(url)) {
                val progress = task.percent
                val speed = task.speed
                LogUtil.e(TAG, "上传：$url   进度：$progress  速度：$speed")
                backUploadHashMap[url]?.onDownloadProgress(progress, speed)
            }
        }
    }

    @Download.onTaskFail
    fun onTaskFail(task: DownloadTask?) {
        this.downloadTask =task
        val url = task?.key
        if (null != url) {
            LogUtil.e(TAG, "下载$url 失败!")
            if (backDownloadHashMap.containsKey(url)) {
                backDownloadHashMap[url]?.onDownloadComplete(false)
                backDownloadHashMap.remove(url)
            }
        }
    }

    @Upload.onTaskFail
    fun onTaskUploadFail(task: UploadTask?) {
        this.uploadTask =task
        val url = task?.key
        if (null != url) {
            LogUtil.e(TAG, "上传$url 失败!")
            if (backUploadHashMap.containsKey(url)) {
                backUploadHashMap[url]?.onDownloadComplete(false)
                backUploadHashMap.remove(url)
            }
        }
    }

    @Download.onTaskComplete
    fun onTaskComplete(task: DownloadTask?) {
        this.downloadTask =task
        val url = task?.key
        if (null != url) {
            LogUtil.e(TAG, "下载$url 成功!")
            if (backDownloadHashMap.containsKey(url)) {
                backDownloadHashMap[url]?.onDownloadComplete(true)
                backDownloadHashMap.remove(url)
            }
        }
    }

    @Upload.onTaskComplete
    fun onTaskUploadComplete(task: UploadTask?) {
        this.uploadTask =task
        val url = task?.key
        if (null != url) {
            LogUtil.e(TAG, "上传$url 成功!")
            if (backUploadHashMap.containsKey(url)) {
                backUploadHashMap[url]?.onDownloadComplete(true)
                backUploadHashMap.remove(url)
            }
        }
    }

    /**
     * 创建并下载文件
     *  -1 异常  0正常  -2已存在
     */
    fun startDownloadFile(url: String, fileName: String, callback: AriaDownloadCallback) {
        try {
            val file = File(DOWNLOAD_DIR_PATH, fileName)
            if (file.exists()) FileUtils.delete(file)
            val taskId = Aria.download(context)
                .load(url)     //读取下载地址
                .ignoreCheckPermissions()
                .ignoreFilePathOccupy()
                .setFilePath(file.absolutePath) //设置文件保存的完整路径
                .create()   //创建并启动下载
            if (taskId != -1L) {
                backDownloadHashMap[url] = callback
                return
            }
        } catch (ee: Exception) {
            ee.printStackTrace()
        }
        callback.onDownloadComplete(false)
    }

    /**
     * 创建并下载文件
     *  -1 异常  0正常  -2已存在
     */
    fun startUploadFile(url: String, fileName: String, callback: AriaDownloadCallback) {
        try {
            val file = File(DOWNLOAD_DIR_PATH, fileName)
            if (!file.exists()) {
                callback.onDownloadComplete(false)
                return
            }
            val taskId = Aria.upload(context)
                .load(file.absolutePath)     //读取下载地址
                .setUploadUrl(url)
                .ignoreCheckPermissions()
                .ignoreFilePathOccupy()
                .create()   //创建并启动下载
            if (taskId != -1L) {
                backUploadHashMap[file.absolutePath] = callback
                return
            } else {
                callback.onDownloadComplete(false)
            }
        } catch (ee: Exception) {
            ee.printStackTrace()
        }

    }


    /**
     * 停止所有下载任务
     */
//    private var mJob:Job? = null
    fun stopAllDownloadFile(isAppStop: Boolean) {
//        if(backDownloadHashMap.size > 0){
//            backDownloadHashMap.map {
//                it.value.onDownloadComplete(isAppStop)
//            }
//        }
        uploadTask?.cancel()
        downloadTask?.cancel()
//        if(backUploadHashMap.size > 0){
//            backUploadHashMap.map {
//                it.value.onDownloadComplete(isAppStop)
//            }
//        }
//        val download = Aria.download(this)
//        download.stopAllTask()
//        download.removeAllTask(false)
//        val upload = Aria.upload(this)
//        upload.stopAllTask()
//        upload.removeAllTask(true)

//        mJob?.cancel()
//        mJob = CoroutineScope(Dispatchers.IO).launch {
//            val download = Aria.download(this)
//            download.stopAllTask()
//            download.removeAllTask(true)
//            val upload = Aria.upload(this)
//            upload.stopAllTask()
//            upload.removeAllTask(true)
//
//            var listDownload = download.allNotCompleteTask
//            while (null != listDownload && listDownload.isNotEmpty()){
//                delay(1000)
//                listDownload = download.allNotCompleteTask
//            }
//            backDownloadHashMap.clear()
//            var listUpload = upload.allNotCompleteTask
//            while (null != listUpload && listUpload.isNotEmpty()){
//                delay(1000)
//                listUpload = upload.allNotCompleteTask
//            }
//            backUploadHashMap.clear()
//
//            withContext(Dispatchers.Main){
//                backData.invoke(true)
//            }
//        }
    }


}