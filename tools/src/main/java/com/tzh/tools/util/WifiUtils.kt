package com.tzh.tools.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.text.TextUtils
import com.tzh.tools.model.NetWorkPingBean
import com.tzh.tools.model.NetWorkWifiBean
import com.tzh.tools.model.NetWorkWifiRouteBean
import com.didichuxing.doraemonkit.constant.MemoryConstants
import com.didichuxing.doraemonkit.util.NetworkUtils
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileReader
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException

/**
 * WIFI工具
 * TTTT
 */
object WifiUtils {


    /**
     * 字节转换Mbps
     */
    fun getBps(byteSize: Long, precision:Int):String{
        val bit = byteSize*8L
        if (byteSize < 0) {
            throw IllegalArgumentException("byteSize shouldn't be less than zero!");
        } else if (bit < 1024) {
            return String.format("%." + precision + "fBps", bit.toDouble())
        } else if (bit < (1024 * 1024)) {
            return String.format("%." + precision + "fKbps", (bit / MemoryConstants.KB).toDouble())
        } else if (bit < (1024 * 1024 * 1024)) {
            return String.format("%." + precision + "fMbps", (bit / MemoryConstants.MB).toDouble())
        } else {
            return String.format("%." + precision + "fGbps", (bit / MemoryConstants.GB).toDouble())
        }
    }

    /**
     * 获取IP的延迟 或者 域名 多次
     * 算平均值延迟
     */
    fun getDelayedNet(host:String,count:Int,backData:(time:Long)->Unit): Job{
        return CoroutineScope(Dispatchers.IO).launch {
            var data = 0L
            var index = 0

            repeat(count){
                val beforeTime = System.currentTimeMillis()
                try {
                    if(InetAddress.getByName(host).isReachable(1000)){
                        index++
                        data += (System.currentTimeMillis() - beforeTime)
                    }
                }catch (ee:Exception){}
            }
            withContext(Dispatchers.Main){
                if(data != 0L){
                    backData.invoke(data/index)
                }else{
                    backData.invoke(-1)
                }
            }
        }
    }
    /**
     * 获取IP的延迟 或者 域名 单次
     */
    fun getDelayedNet(host:String,backData:(time:Long)->Unit): Job{
        return CoroutineScope(Dispatchers.IO).launch {
            val beforeTime = System.currentTimeMillis()
            InetAddress.getByName(host).isReachable(1000)
            withContext(Dispatchers.Main){
                backData.invoke(System.currentTimeMillis() - beforeTime)
            }
        }
    }

    /**
     * ping 数据 某个连接
     * Job 用于停止
     */
    fun pingHost(host:String,backData:(time: NetWorkPingBean)->Unit): Job{
        return CoroutineScope(Dispatchers.IO).launch {
            var rx = 0
            var tx = 0
            var lossRate = 0.0F
            while (true){
                val beforeTime = System.currentTimeMillis()
                var time = 0L
                tx++
                try {
                    if(InetAddress.getByName(host).isReachable(1000)){
                        rx++
                        time = System.currentTimeMillis() - beforeTime
                    }
                }catch (ee:Exception){}
                if(time == 0L)time = 100
                lossRate = ((tx - rx).toFloat() / tx.toFloat()) * 100F
                withContext(Dispatchers.Main){
                    val data = NetWorkPingBean(lossRate,tx,rx,time)
                    LogUtil.e("当前ping数据：$data")
                    backData.invoke(data)
                }
                delay(1000)
            }
        }
    }

    /**
     * 获取 Wifi 路由
     */
    @SuppressLint("MissingPermission")
    fun getWifiScanRoute(context: Context, backData:(dataBean: NetWorkWifiRouteBean, isOK:Boolean)->Unit): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            try {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                if(null != connectivityManager && connectivityManager is ConnectivityManager) {
                    val info = connectivityManager.activeNetworkInfo
                    if (info != null && info.isConnected) {
                        if (info.type == ConnectivityManager.TYPE_WIFI) {
                            val mWifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE)
                            if(null != mWifiManager && mWifiManager is WifiManager){
                                val wifiInfo = mWifiManager.connectionInfo
                                val ip = intIP2StringIP(wifiInfo.ipAddress)//得到IPV4地址
                                val subnet = intIP2StringIPs(mWifiManager.dhcpInfo.gateway)
                                var mapList = getMacAddressFromIP()
                                for (i in 1..255){
                                    val host = subnet + i
                                    withContext(Dispatchers.IO){
                                        if (InetAddress.getByName(host).isReachable(50)){
                                            if(!mapList.containsKey(host)){
                                                mapList = getMacAddressFromIP()
                                            }
                                            var strMacAddress = mapList[host]
                                            if(TextUtils.isEmpty(strMacAddress))strMacAddress = ""
                                            if(null != strMacAddress){
                                                val data = if(host == ip){
                                                    NetWorkWifiRouteBean(host,strMacAddress,true,wifiInfo.ssid)
                                                }else{
                                                    NetWorkWifiRouteBean(host,strMacAddress)
                                                }
                                                withContext(Dispatchers.Main){
                                                    backData.invoke(data,false)
                                                }
                                            }
//                                            LogUtil.e("当前IP：$host  MAC地址：$strMacAddress")
                                        }
                                    }
                                }
                                withContext(Dispatchers.Main){
                                    backData.invoke(NetWorkWifiRouteBean("",""),true)
                                }
                            }
                        }
                    }
                }
            }catch (ee:Exception){
                ee.printStackTrace()
            }
        }
    }

    /**
     * 获取当前网络类型
     *  0 无网络 1流量 2WIFI 3有线
     */
    @SuppressLint("MissingPermission")
    fun getNetWorkType(context: Context):Int{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        if(null != connectivityManager && connectivityManager is ConnectivityManager) {
            val info = connectivityManager.activeNetworkInfo
            if (info != null && info.isConnected) {
                return info.type
            }
        }
        return 0
    }

    /**
     * 获取当前WIFI或者有线IPV4地址
     */
    @SuppressLint("MissingPermission")
    fun getIPAddress(context: Context):Any? {
        val netType = getNetWorkType(context)
        when(netType){
            ConnectivityManager.TYPE_MOBILE->{
                try {
                    val en = NetworkInterface.getNetworkInterfaces()
                    for(intf in en){
                        val enumIpAddr = intf.inetAddresses
                        for (inetAddress in enumIpAddr){
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                val itemComap = inetAddress.getHostAddress()

                                return itemComap
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }
            }
            ConnectivityManager.TYPE_WIFI->{
                val wifiManagerService = context.applicationContext.getSystemService(Context.WIFI_SERVICE)
                if(null != wifiManagerService && wifiManagerService is WifiManager){
                    val wifiInfo = wifiManagerService.connectionInfo
                    val ip = intIP2StringIP(wifiInfo.ipAddress)//得到IPV4地址
                    val rssi = wifiInfo.rssi
                    val netmask = NetworkUtils.getNetMaskByWifi()
                    val linkSpeed = wifiInfo.linkSpeed
                        LogUtil.e("---->" + wifiManagerService.dhcpInfo.toString())
                    return NetWorkWifiBean(ip,rssi,linkSpeed,netmask)
                }
            }
            ConnectivityManager.TYPE_ETHERNET->{
                //有线网络
                try {
                    // 获取本地设备的所有网络接口
                    val enumerationNi = NetworkInterface.getNetworkInterfaces()
                    while (enumerationNi.hasMoreElements()) {
                        val networkInterface = enumerationNi.nextElement()
                        val interfaceName = networkInterface.displayName
                        LogUtil.i("tag", "网络名字$interfaceName")
                        // 如果是有线网卡
                        if (interfaceName.equals("eth0")) {
                            val enumIpAddr = networkInterface.inetAddresses
                            while (enumIpAddr.hasMoreElements()) {
                                // 返回枚举集合中的下一个IP地址信息
                                val inetAddress = enumIpAddr.nextElement()
                                // 不是回环地址，并且是ipv4的地址
                                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                    LogUtil.i("tag", inetAddress.getHostAddress() + " ")
                                    return inetAddress.getHostAddress()
                                }
                            }
                        }
                    }
                } catch (e:SocketException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    fun intIP2StringIP(ip:Int):String {
        val ipStr = StringBuffer()
        ipStr.append((ip and 0xFF))
        ipStr.append(".")
        ipStr.append(((ip ushr 8) and 0xFF))
        ipStr.append(".")
        ipStr.append(((ip ushr 16) and 0xFF))
        ipStr.append(".")
        ipStr.append((ip ushr 24 and 0xFF))
        return ipStr.toString()
    }
    //用于拼接IP 扫描路由
    fun intIP2StringIPs(ip:Int):String {
        val ipStr = StringBuffer()
        ipStr.append((ip and 0xFF))
        ipStr.append(".")
        ipStr.append(((ip ushr 8) and 0xFF))
        ipStr.append(".")
        ipStr.append(((ip ushr 16) and 0xFF))
        ipStr.append(".")
        return ipStr.toString()
    }

    /**
     * 获取系统
     */
    fun getMacAddressFromIP():HashMap<String,String>{
        val adddressHash = HashMap<String,String>()
        var bufferedReader:BufferedReader? = null
        try {
            bufferedReader = BufferedReader( FileReader("/proc/net/arp"))
            var line = bufferedReader.readLine()
            while (null != line) {
//                    LogUtil.e("MAC地址：$line")
                val splitted = line.split(" ")
                var ip = ""
                var mac = ""
                for (item in splitted){
                    if(!TextUtils.isEmpty(item)){
                        //判断是不是IP地址
                        if (isCorrectIp2(item)) {
                            ip = item
                        }else if (item.matches(Regex("..:..:..:..:..:.."))) {
                            //判断是不是MAC地址
                            mac = item
                        }
                    }
                    if(!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(mac))break
                }
//                    LogUtil.e("MAC地址：$mac  IP:$ip")
                if(!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(mac)){
                    adddressHash[ip] = mac
                }
                line = bufferedReader.readLine()
            }
        }catch (ee:Exception){
            ee.printStackTrace()
        }finally{
            try {
                bufferedReader?.close()
            } catch (_:Exception) {}
        }
        return adddressHash
    }
    //利用正则表达式判断字符是否为IP
    fun isCorrectIp2(ipString:String):Boolean {
//        LogUtil.e("------>$ipString<------")
        //1、判断是否是7-15位之间（0.0.0.0-255.255.255.255.255）
        if (ipString.length < 7 || ipString.length > 15) return false
        //2、判断是否能以小数点分成四段
        val ipArray = ipString.split(".")
        if (ipArray.size != 4) return false
        for (i in ipArray.indices) {
            //3、判断每段是否都是数字
            try {
                val number = ipArray[i].toInt()
                //4.判断每段数字是否都在0-255之间
                if (number < 0 || number > 255) return false
            } catch (e: java.lang.Exception) {
                return false
            }
        }
        return true
    }
}