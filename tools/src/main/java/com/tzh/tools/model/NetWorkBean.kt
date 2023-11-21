package com.tzh.tools.model


/**
 * WIFI 信息
 *  ip地址
 *  intensity 信号强度 0-50最好  50-70偏差 小于70最差
 *  linkSpeed 连接速度
 */
data class NetWorkWifiBean(var ip:String,var intensity:Int,var linkSpeed:Int,var netmask:String)

/**
 * 路由列表数据
 *  isFacility 是否本机
 */
data class NetWorkWifiRouteBean(var ip:String,var mac:String,var isFacility: Boolean = false,var ssid:String = "router.ctc")
/**
 *  当前 上传与下载速度
 *  rx 接收数据
 *  tx 上传数据
 *  Mbps
 */
data class NetWorkSpeedBean(var rx:Long,var tx:Long,var rxMbps:Float,var txMbps:Float)
/**
 *  速度测试
 *  downloading 下载速度
 *  upload 上传速度
 *  netType 网络类型 0 无网络 1流量 2WIFI 3有线
 *   netDelay 网络延迟
 */
data class NetWorkTestSpeedBean(var downloading:Long = -1,var upload:Long = -1,var netType:Int = -1,var netDelay:Long = -1,var listDownloadingSpeed:ArrayList<Long> = arrayListOf(),var listUploadSpeed:ArrayList<Long> = arrayListOf())
/**
 *  ping 相关数据
 *  lossRate 丢失率(百分比)
 *  tx 发送
 *  rx 接收
 *  netDelay 网络延迟
 */
data class NetWorkPingBean(var lossRate:Float,var tx:Int,var rx:Int,var netDelay:Long)