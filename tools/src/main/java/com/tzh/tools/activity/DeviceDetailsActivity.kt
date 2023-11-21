package com.tzh.tools.activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.util.TestSpeed
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.DeviceDetailUtil
import com.tzh.tools.util.LogUtil
import com.tzh.tools.util.MemoryUtils
import com.tzh.tools.util.WifiUtils
import com.didichuxing.doraemonkit.util.ConvertUtils

/**
 * 设备详情
 */
open class DeviceDetailsActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isAdResume = false
    override fun onResume() {
        super.onResume()
        if(!isAdResume){
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }

    companion object {
        fun startActivity(context: Context,title: String, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, DeviceDetailsActivity::class.java)
            intent.putExtra("title", title)
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT and Intent.FLAG_ACTIVITY_NEW_TASK)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED));  //获取电量
    }

    var batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                loadBatteryInfo(intent)
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        loadWifiInfo()
        return super.handleMessage(msg)
    }

    override fun initView() {
        findViewById<View>(R.id.dd_top).findViewById<TextView>(R.id.tv_title).text =
            intent.getStringExtra("title")
        findViewById<View>(R.id.dd_top).findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }

        loadSdInfo()
        loadRamInfo()
        loadBaseInfo()

        mHandler.sendEmptyMessageDelayed(1,500)



//        mDataBinding.ddTop.findViewById<ImageView>(R.id.iv_back).setImageResource(R.drawable.ic_left_row)
//        mDataBinding.ddTop.findViewById<TextView>(R.id.tv_title).setTextColor(Color.WHITE)
    }

    //基本信息
    protected fun loadBaseInfo() {
        val ddBaseInfoResolutionH=    findViewById<TextView>(R.id.dd_baseInfo_ResolutionH)
        val ddBaseInfoResolutionW=    findViewById<TextView>(R.id.dd_baseInfo_ResolutionW)
        val ddBaseInfoFirm=    findViewById<TextView>(R.id.dd_baseInfo_firm)
        val ddBaseInfoType=    findViewById<TextView>(R.id.dd_baseInfo_type)
        val ddBaseInfoAndroid=    findViewById<TextView>(R.id.dd_baseInfo_android)
        val ddBaseInfoCpu=    findViewById<TextView>(R.id.dd_baseInfo_cpu)
        val ddBaseInfoTime=    findViewById<TextView>(R.id.dd_baseInfo_time)
        val ddBaseInfoRom=    findViewById<TextView>(R.id.dd_baseInfo_Rom)

         ddBaseInfoResolutionH.text ="${DeviceDetailUtil.getScreenHeight(this)}"
         ddBaseInfoResolutionW.text ="${DeviceDetailUtil.getScreenWidth(this)}"

         ddBaseInfoFirm.text = android.os.Build.BRAND
         ddBaseInfoType.text = android.os.Build.MODEL
         ddBaseInfoAndroid.text = android.os.Build.VERSION.RELEASE
         ddBaseInfoCpu.text = DeviceDetailUtil.getCPUAbi()
         ddBaseInfoTime.text = DeviceDetailUtil.getSystemStartupTime()
         ddBaseInfoRom.text = android.os.Build.MANUFACTURER
    }

    //网络信息
    private fun loadWifiInfo() {
        val ddNetworkType=    findViewById<TextView>(R.id.dd_network_type)
        val ddBaseInfoIp=    findViewById<TextView>(R.id.dd_baseInfo_ip)
        val ddBaseInfoDown=    findViewById<TextView>(R.id.dd_baseInfo_down)
        val ddNetworkUp=    findViewById<TextView>(R.id.dd_network_up)
         ddNetworkType.text = DeviceDetailUtil.getConnectedType(this)
        WifiUtils.getWifiScanRoute(this@DeviceDetailsActivity) { result, isEnd ->
            if (result.isFacility) {
                 ddBaseInfoIp.text = result.ip
            }
        }
        TestSpeed.instance.startTestSpeed{ bean, type->
            when(type){
                1->{
                    //下载速度
                    if(bean.listDownloadingSpeed.isNotEmpty()){
                        val longSpeed = bean.listDownloadingSpeed[bean.listDownloadingSpeed.size-1]
                        val mbps = WifiUtils.getBps(longSpeed,2)
                        val speed = ConvertUtils.byte2FitMemorySize(longSpeed,2)
                        LogUtil.e(">>>当前下载速度: $speed/s   当前Mbps: $mbps")
                         ddBaseInfoDown.text="$speed/s"
                    }
                }
                2->{
                    //下载速度完成
                    val longSpeed = bean.downloading
                    val mbps = WifiUtils.getBps(longSpeed,2)
                    val speed = ConvertUtils.byte2FitMemorySize(longSpeed,2)
                    LogUtil.e("当前下载完成速度: $speed/s   当前平均Mbps: $mbps")
                     ddBaseInfoDown.text="$speed/s"
                }
                3->{
                    LogUtil.e("测试下载速度: 失败")
                     ddBaseInfoDown.text="测试失败"
                }
                4->{
                    //上传速度
                    if(bean.listUploadSpeed.isNotEmpty()){
                        val longSpeed = bean.listUploadSpeed[bean.listUploadSpeed.size-1]
                        val mbps = WifiUtils.getBps(longSpeed,2)
                        val speed = ConvertUtils.byte2FitMemorySize(longSpeed,2)
                        LogUtil.e(">>>当前上传速度: $speed/s   当前Mbps: $mbps")
                         ddNetworkUp.text="$speed/s"
                    }
                }
                5->{
                    //上传完成
                    val longSpeed = bean.upload
                    val mbps = WifiUtils.getBps(longSpeed,2)
                    val speed = ConvertUtils.byte2FitMemorySize(longSpeed,2)
                    LogUtil.e("当前上传完成速度: $speed/s   当前平均Mbps: $mbps")
                     ddNetworkUp.text="$speed/s"
                }
                6->{
                    LogUtil.e("测试上传速度: 失败")
                     ddNetworkUp.text="测试失败"
                }
                7->{
                    val strType = when(bean.netType){
                        0->"无网"
                        ConnectivityManager.TYPE_MOBILE->"流量"
                        ConnectivityManager.TYPE_WIFI->"WIFI"
                        ConnectivityManager.TYPE_ETHERNET->"有线"
                        else->"未知"
                    }
                    LogUtil.e("网络类型为: $strType")
                }
                8->{
                    LogUtil.e("网络延迟为: ${bean.netDelay}ms")
                }
                9->{
                    LogUtil.e("网络测速完成: $bean")
                }
            }
        }
    }

    //内存信息
    protected fun loadRamInfo() {
        val ddRamLave=    findViewById<TextView>(R.id.dd_ram_lave)
        val ddRam1=    findViewById<TextView>(R.id.dd_ram1)
        val ddRam3=    findViewById<TextView>(R.id.dd_ram3)
         ddRamLave.text = "${MemoryUtils.instance.getMemoryPercentage(this)}%"
         ddRam1.text = MemoryUtils.instance.getTotalMem(this)
         ddRam3.text = MemoryUtils.instance.getAvailMem(this)

    }

    //电池信息
    protected fun loadBatteryInfo(intent:Intent) {
        val mLevel = intent.getIntExtra("level", 0);  //电池电量
        val BatteryV = intent.getIntExtra("voltage", 0);  //电池电压
        val BatteryT = intent.getIntExtra("temperature", 0);  //电池温度


        val ddBatteryLave=    findViewById<TextView>(R.id.dd_battery_lave)
        val ddBattery1=    findViewById<TextView>(R.id.dd_battery1)
        val ddBattery3=    findViewById<TextView>(R.id.dd_battery3)
        val ddBattery2=    findViewById<TextView>(R.id.dd_battery2)
        val ddBattery4=    findViewById<TextView>(R.id.dd_battery4)

         ddBatteryLave.text="$mLevel%"
         ddBattery1.text="${"%.1f".format(BatteryT*0.1)}°"
         ddBattery3.text="${"%.2f".format(BatteryV*0.001)}V"
        when (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
            BatteryManager.BATTERY_HEALTH_UNKNOWN->{
                 ddBattery2.text="未知错误"
            }
            BatteryManager.BATTERY_HEALTH_GOOD->{
                 ddBattery2.text="电池状态良好"
            }
            BatteryManager.BATTERY_HEALTH_DEAD->{
                 ddBattery2.text="电池没有电"
            }
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE->{
                 ddBattery2.text="电池电压过高"
            }
            BatteryManager.BATTERY_HEALTH_OVERHEAT->{
                 ddBattery2.text="电池过热"
            }
        }
        when (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
            BatteryManager.BATTERY_STATUS_CHARGING->{
                 ddBattery4.text="充电状态"
            }
            BatteryManager.BATTERY_STATUS_DISCHARGING->{
                 ddBattery4.text="放电状态"
            }
            BatteryManager.BATTERY_STATUS_NOT_CHARGING->{
                 ddBattery4.text="充满电"
            }
            BatteryManager.BATTERY_STATUS_FULL->{
                 ddBattery4.text="未知状态"
            }
        }

    }

    //存儲信息
    protected fun loadSdInfo() {
        val ddSd1=    findViewById<TextView>(R.id.dd_sd1)
        val ddSd2=    findViewById<TextView>(R.id.dd_sd2)
        val ddSd3=    findViewById<TextView>(R.id.dd_sd3)
        val ddSdLave=    findViewById<TextView>(R.id.dd_sd_lave)
         ddSd1.text = DeviceDetailUtil.getInternalMemorySize()
         ddSd3.text = DeviceDetailUtil.getAvailableInternalMemorySize()
         ddSd2.text = DeviceDetailUtil.getSdLave()
         ddSdLave.text = DeviceDetailUtil.getSdUsage()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeMessages(1)
        TestSpeed.instance.stopTestSpeed()
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_device_lib_details
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}