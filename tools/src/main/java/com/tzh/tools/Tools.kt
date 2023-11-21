package com.tzh.tools

import android.app.Application
import com.tzh.tools.util.AriaDownloadManagement
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

/**
 * @author: Created by zfj
 * created on: 2023/4/26
 * description: 初始化（必须调用）
 */
object Tools {
    lateinit var app: Application
    lateinit var token: String
    lateinit var version: String
    lateinit var channel: String
    lateinit var projectId: String
    lateinit var appClient: String

    /**
     * 不需要，隐私合规
     */
    fun init(app: Application,token:String,version: String, channel: String, projectId:String,appClient:String) {
        com.tzh.tools.Tools.app = app
        com.tzh.tools.Tools.token = token
        com.tzh.tools.Tools.version = version
        com.tzh.tools.Tools.channel = channel
        com.tzh.tools.Tools.projectId = projectId
        com.tzh.tools.Tools.appClient = appClient
        MMKV.initialize(app, MMKVLogLevel.LevelError)
        ToastUtils.init(app)
    }

    /**
     * 需要，隐私合规
     */
    fun initPrivacy(app: Application) {
        AriaDownloadManagement.instance.initDownload(app)
    }




}