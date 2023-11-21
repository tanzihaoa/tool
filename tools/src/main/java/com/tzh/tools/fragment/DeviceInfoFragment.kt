package com.tzh.tools.fragment


import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.CommonUtil


/**
 * @author: Created by zfj
 * created on: 2023/4/25
 * description: 设备信息
 */
class DeviceInfoFragment : BaseFragment<BaseViewModel<*>>() {

    companion object {
        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?) =
            DeviceInfoFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }
        @JvmStatic
        fun newInstance() =
            DeviceInfoFragment().apply {
            }
    }

    override fun initView() {
        view?.findViewById<TextView>(R.id.tv_band)?.text = Build.BRAND
        view?.findViewById<TextView>(R.id.tv_version)?.text = Build.VERSION.RELEASE
        view?.findViewById<TextView>(R.id.tv_start)?.text = CommonUtil.getCPUAbi()
        view?.findViewById<TextView>(R.id.tv_system)?.text = CommonUtil.getSystemStartupTime()
    }


    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_device_info
    }

}