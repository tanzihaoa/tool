package com.tzh.tools.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.adapter.ScanningInternetAdapter
import com.tzh.tools.base.BaseAdActivity
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityScanningInternetBinding
import com.tzh.tools.model.ScanningInternetModel
import com.tzh.tools.util.WifiUtils
import com.gyf.immersionbar.ImmersionBar

/**
 * 蹭网检测
 */
class ScanningInternetLibActivity : BaseAdActivity<BaseViewModel<*>, ActivityScanningInternetBinding>() {

    private var isAdResume = false

    private var type = 0

    private val mList = mutableListOf<ScanningInternetModel>()

    private lateinit var mAdapter: ScanningInternetAdapter


    override fun onResume() {
        super.onResume()
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            if (intent.getIntExtra(GOTO_TYPE, 0) == 1) {
                LibAdBridge.instance.startInterstitial(this)
            } else if (intent.getIntExtra(GOTO_TYPE, 0) == 2) {
                LibAdBridge.instance.startRewardVideo(this)
            }
        }
    }

    companion object {

        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            val intent = Intent(context, ScanningInternetLibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(BaseLibActivity.KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE, adType)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()
        type = intent.getIntExtra(GOTO_TYPE, 0)
        mDataBinding.top.findViewById<TextView>(R.id.tv_title).text = "蹭网检测"
        mDataBinding.top.findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }

        mAdapter = ScanningInternetAdapter(mList)
        mDataBinding.rcvScanningInternet.adapter = mAdapter
    }

    override fun loadData() {

        WifiUtils.getWifiScanRoute(this@ScanningInternetLibActivity) { result, isEnd ->
            if (!isEnd) {
                if (result.isFacility) {
                    mDataBinding.tvSsid.text = "${result.ssid}检测中...."
                    mList.add(
                        0,
                        ScanningInternetModel(
                            R.mipmap.scanning_myself,
                            "${result.ssid}(当前设备)",
                            result.ip,
                            "通用设备"
                        )
                    )
                    runOnUiThread {
                        mAdapter.notifyItemInserted(0)
                    }
                } else {
                    mList.add(
                        ScanningInternetModel(
                            R.mipmap.scanning_other,
                            result.ssid,
                            result.ip,
                            "未知"
                        )
                    )
                    runOnUiThread {
                        mAdapter.notifyItemInserted(mList.size)
                    }
                }
                mDataBinding.tvList.text = mList.size.toString()

            } else {
                mDataBinding.lottieScanning.cancelAnimation()
                mDataBinding.lottieScanning.visibility = View.GONE
                mDataBinding.tvSsid.text = "${mList[0].ip} 本机"
                mDataBinding.clTop.visibility = View.GONE
            }

        }

    }


    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_scanning_internet
    }
}