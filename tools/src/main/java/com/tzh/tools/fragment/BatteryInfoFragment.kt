package com.tzh.tools.fragment


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import com.tzh.tools.R
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.CommonUtil


/**
 * @author: Created by zfj
 * created on: 2023/4/25
 * description: 手机电池详情
 */
class BatteryInfoFragment : BaseFragment<BaseViewModel<*>>() {

    companion object {
        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?) =
            BatteryInfoFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }
        @JvmStatic
        fun newInstance() =
            BatteryInfoFragment().apply {
            }
    }

    override fun initView() {
        registerReceiver(requireContext(),batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED),RECEIVER_NOT_EXPORTED)  //获取电量
    }

//    override fun onDestroy() {
//        requireContext().
//        super.onDestroy()
//    }

    private var batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                loadBatteryInfo(intent)
            }
        }
    }

    //电池信息
    protected fun loadBatteryInfo(intent: Intent) {
//        val mLevel = intent.getIntExtra("level", 0);  //电池电量
        val BatteryV = intent.getIntExtra("voltage", 0);  //电池电压
        val BatteryT = intent.getIntExtra("temperature", 0);  //电池温度

        val ddBattery1=view?.findViewById<TextView>(R.id.dd_battery1)
        val ddBattery3=view?.findViewById<TextView>(R.id.dd_battery3)
        val ddBattery2=view?.findViewById<TextView>(R.id.dd_battery2)
        val ddBattery4=view?.findViewById<TextView>(R.id.dd_battery4)

        ddBattery1?.text="${"%.1f".format(BatteryT*0.1)}°"
        ddBattery3?.text="${"%.2f".format(BatteryV*0.001)}V"
        when (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
            BatteryManager.BATTERY_HEALTH_UNKNOWN->{
                ddBattery2?.text="未知错误"
            }
            BatteryManager.BATTERY_HEALTH_GOOD->{
                ddBattery2?.text="电池状态良好"
            }
            BatteryManager.BATTERY_HEALTH_DEAD->{
                ddBattery2?.text="电池没有电"
            }
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE->{
                ddBattery2?.text="电池电压过高"
            }
            BatteryManager.BATTERY_HEALTH_OVERHEAT->{
                ddBattery2?.text="电池过热"
            }
        }
        when (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
            BatteryManager.BATTERY_STATUS_CHARGING->{
                ddBattery4?.text="充电状态"
            }
            BatteryManager.BATTERY_STATUS_DISCHARGING->{
                ddBattery4?.text="放电状态"
            }
            BatteryManager.BATTERY_STATUS_NOT_CHARGING->{
                ddBattery4?.text="充满电"
            }
            BatteryManager.BATTERY_STATUS_FULL->{
                ddBattery4?.text="未知状态"
            }
        }

    }


    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_lib_battery_info
    }

}