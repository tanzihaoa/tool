package com.tzh.tools.util

import com.tzh.tools.model.NetWorkTestSpeedBean
import kotlinx.coroutines.*
import java.util.*


/**
 * 首页测速工具
 */
class TestSpeed {
    companion object {
        val instance: TestSpeed by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TestSpeed()
        }
    }
//    private var mHandler:Handler? = null
//    private var mHandlerThread:HandlerThread? = null
//    private var mLastRxBytes: Long = 0
//    private var mLastTxBytes: Long = 0
//    //监听当前系统网络速度
//    private fun startSpeed(){
//        if(null == mHandlerThread || !mHandlerThread!!.isAlive){
//            mHandlerThread = HandlerThread("speed")
//            mHandlerThread?.start()
//            mHandler = Handler(mHandlerThread!!.looper){
//                val uidRxBytes = TrafficStats.getUidRxBytes(Process.myUid())
//                val uidTxBytes = TrafficStats.getUidTxBytes(Process.myUid())
//                //当前速度
//                val rx = max(uidRxBytes - mLastRxBytes,0L)
//                val tx = max(uidTxBytes - mLastTxBytes,0L)
//                //计算Mbps
//                val rxMbps = if(rx > 0){max(rx/128F,0F)}else{0F}
//                val txMbps = if(tx > 0){max(tx/128F,0F)}else{0F}
////                LogUtil.e("发送EventBus-->当前接收：$rx   当前上传：$tx   当前Mbps接收：$rxMbps   当前Mbps上传：$txMbps")
//                org.greenrobot.eventbus.EventBus.getDefault().post(NetWorkSpeedBean(rx,tx,rxMbps,txMbps))
//                mLastRxBytes = uidRxBytes
//                mLastTxBytes = uidTxBytes
//                mHandler?.sendEmptyMessageDelayed(1,1000)
//                return@Handler false
//            }
//        }
//        mHandler?.sendEmptyMessage(1)
//    }
//
//    private fun startHandler(){
//    if(null == mHandlerThread || !mHandlerThread!!.isAlive){
//        mHandlerThread = HandlerThread("speed")
//        mHandlerThread?.start()
//        mHandler = Handler(mHandlerThread!!.looper){
//            return@Handler false
//        }
//    }
//    }
//
//    fun initSpeed(){
//        startHandler()
//    }

    private val speed_download_test = "https://resource.csshuqu.cn/download_test"
    private val speed_upload_test = "https://wifi-api-cdn.csshuqu.cn/upload/testNetSpeedUpload"
    private val max_speed_data = 20
    private var mNetWorkTestSpeedBean: NetWorkTestSpeedBean? = null
    private var isRun = false
    private var fileName = ""
    /**
     * 开始WIFI测速
     * type 1下载速度 2下载成功 3下载失败
     *      4上传速度 5上传成功 6上传失败
     *      7 网络类型 8 网络延迟 9完成
     */
    private var mJob:Job? = null
    fun startTestSpeed(backData:(data:NetWorkTestSpeedBean,type:Int)->Unit){
        mJob?.cancel()
        mJob = CoroutineScope(Dispatchers.Main).launch{
            delay(2000)
            if(!isRun){
                isRun = true
                mNetWorkTestSpeedBean = null
                fileName = UUID.randomUUID().toString() +"_"+System.currentTimeMillis()
                nextTestSpeed(1,backData)
            }
        }
    }
    private fun nextTestSpeed(index:Int,backData:(data:NetWorkTestSpeedBean,type:Int)->Unit){
        when(index){
            1->{
                //网络类型
                val type = WifiUtils.getNetWorkType(com.tzh.tools.Tools.app)
                if(null == mNetWorkTestSpeedBean)mNetWorkTestSpeedBean = NetWorkTestSpeedBean()
                mNetWorkTestSpeedBean?.netType = type
                backData.invoke(mNetWorkTestSpeedBean!!,7)
                if(isRun){
                    if(type != 0){
                        isRun = true
                        nextTestSpeed(2,backData)
                        return
                    }
                }
                //没联网
                isRun = false
                backData.invoke(mNetWorkTestSpeedBean!!,9)
            }
            2->{
                //网络延迟
                WifiUtils.getDelayedNet("www.baidu.com",3){
                    if(null == mNetWorkTestSpeedBean)mNetWorkTestSpeedBean = NetWorkTestSpeedBean()
                    mNetWorkTestSpeedBean?.netDelay = it
                    if(isRun){
                        backData.invoke(mNetWorkTestSpeedBean!!,8)
                        nextTestSpeed(3,backData)
                    }else{
                        isRun = false
                        backData.invoke(mNetWorkTestSpeedBean!!,9)
                    }
                }
            }
            3->{
                //下载测试
                if(!isRun){
                    backData.invoke(mNetWorkTestSpeedBean!!,9)
                    return
                }
                if(null == mNetWorkTestSpeedBean)mNetWorkTestSpeedBean = NetWorkTestSpeedBean()
                mNetWorkTestSpeedBean!!.listDownloadingSpeed.clear()
                AriaDownloadManagement.instance.clearFile()
                AriaDownloadManagement.instance.startDownloadFile(speed_download_test,fileName,object :
                    AriaDownloadCallback {
                    override fun onDownloadProgress(progress: Int,speed:Long) {
                        if(null == mNetWorkTestSpeedBean)mNetWorkTestSpeedBean = NetWorkTestSpeedBean()
                        val list = mNetWorkTestSpeedBean!!.listDownloadingSpeed
                        list.add(speed)
                        backData.invoke(mNetWorkTestSpeedBean!!,1)
//                        //有20个数据差不多了
                        if(!isRun){
                            AriaDownloadManagement.instance.stopAllDownloadFile(true)
                            onDownloadComplete(true)
                        }
                    }
                    override fun onDownloadComplete(isOK: Boolean) {
                          if(null == mNetWorkTestSpeedBean)mNetWorkTestSpeedBean = NetWorkTestSpeedBean()
                          if(isOK){
                              //计算Mbps
                              val listDownloadingSpeed = mNetWorkTestSpeedBean!!.listDownloadingSpeed
//                              if(listDownloadingSpeed.size > max_speed_data){
//                                  for(item in 0 until listDownloadingSpeed.size){
//                                      if(max_speed_data >= item) listDownloadingSpeed.removeAt(item)
//                                  }
//
//                              }
                              if(listDownloadingSpeed.isNotEmpty()){
                                  val dataCount = listDownloadingSpeed.size
                                  var dataSpeed = 0L
                                  for (item in listDownloadingSpeed) dataSpeed += item
                                  val speedVal = dataSpeed/dataCount
                                  mNetWorkTestSpeedBean?.downloading = speedVal
                              }
                              backData.invoke(mNetWorkTestSpeedBean!!,2)
                          }else{
                              backData.invoke(mNetWorkTestSpeedBean!!,3)
                          }
                        CoroutineScope(Dispatchers.Main).launch{
                            delay(2000)
                            nextTestSpeed(4,backData)
                        }
                    }
                })
            }
            4->{
               //上传测试
                if(!isRun){
                    backData.invoke(mNetWorkTestSpeedBean!!,9)
                    return
                }
                if(null == mNetWorkTestSpeedBean)mNetWorkTestSpeedBean = NetWorkTestSpeedBean()
                mNetWorkTestSpeedBean!!.listUploadSpeed.clear()
                AriaDownloadManagement.instance.startUploadFile(speed_upload_test,fileName,object :
                    AriaDownloadCallback {
                    override fun onDownloadProgress(progress: Int,speed:Long) {
                        if(null == mNetWorkTestSpeedBean)mNetWorkTestSpeedBean = NetWorkTestSpeedBean()
                        val list = mNetWorkTestSpeedBean!!.listUploadSpeed
                        list.add(speed)
                        backData.invoke(mNetWorkTestSpeedBean!!,4)
                        //有20个数据差不多了
                        if(list.size >= max_speed_data  || !isRun){
                            AriaDownloadManagement.instance.stopAllDownloadFile(true)
                            onDownloadComplete(true)
                        }
                    }

                    override fun onDownloadComplete(isOK: Boolean) {
                        if(null == mNetWorkTestSpeedBean)mNetWorkTestSpeedBean = NetWorkTestSpeedBean()
                        if(isOK){
                            //计算Mbps
                            val listUploadSpeed = mNetWorkTestSpeedBean!!.listUploadSpeed
                            if(listUploadSpeed.isNotEmpty()){
                                val dataCount = listUploadSpeed.size
                                var dataSpeed = 0L
                                for (item in listUploadSpeed) dataSpeed += item
                                val speedVal = dataSpeed/dataCount
                                mNetWorkTestSpeedBean?.upload = speedVal
                            }
                            backData.invoke(mNetWorkTestSpeedBean!!,5)
                        }else{
                            val listUploadSpeed = mNetWorkTestSpeedBean!!.listUploadSpeed
                            if(listUploadSpeed.isNotEmpty()){
                                onDownloadComplete(true)
                                return
                            }
                            backData.invoke(mNetWorkTestSpeedBean!!,6)
                        }
                        isRun = false
                        backData.invoke(mNetWorkTestSpeedBean!!,9)
                    }
                })
            }
            }
    }

    /**
     * 停止WIFI测速
     */
    fun stopTestSpeed(){
        AriaDownloadManagement.instance.stopAllDownloadFile(true)
        isRun = false
//        backData.invoke(true)
    }


}