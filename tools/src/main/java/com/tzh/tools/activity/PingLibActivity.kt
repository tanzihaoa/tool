package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.adapter.PingAdapter
import com.tzh.tools.base.BaseActivity
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityPingBinding
import com.tzh.tools.model.NetWorkPingBean
import com.tzh.tools.util.BarUtil
import com.tzh.tools.util.WifiUtils
import com.tzh.tools.util.clickDelay
import kotlinx.coroutines.Job

/**
 * ping测速
 */
class PingLibActivity : BaseActivity<BaseViewModel<*>, ActivityPingBinding>() {

    private var isAdResume = false

    override fun onResume() {
        super.onResume()
        super.onResume()
        if(!isAdResume){
            isAdResume = true
            if(intent.getIntExtra(GOTO_TYPE, 0)==1){
                LibAdBridge.instance.startInterstitial(this)
            }else if(intent.getIntExtra(GOTO_TYPE, 0)==2){
                LibAdBridge.instance.startRewardVideo(this)
            }
        }
    }

    companion object {
        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            val intent = Intent(context, PingLibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(BaseLibActivity.KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE,adType)
            context.startActivity(intent)
        }
    }

    private var isStartPing = false
    private var mJob: Job? = null
    private var mPingAdapter: PingAdapter? = null
    private val pingMsList = ArrayList<NetWorkPingBean>()
    private var hostUrl = ""

    override fun initView() {
        var actionBarHeight = 0
        val tv = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight =
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        actionBarHeight += BarUtil.getStatusBarHeight()
        val lp = mDataBinding.titleLayout.layoutParams
        lp.height = actionBarHeight
        mDataBinding.titleLayout.layoutParams = lp
        mDataBinding.butTitleBack.setOnClickListener { onBackPressed() }
        mDataBinding.tvTitleBody.text = getString(R.string.ping_1)

        mDataBinding.butStartPing.clickDelay {
            //开始ping
            mDataBinding.editPingUrlLayout.visibility = View.GONE
            startPing(hostUrl)
        }

        mDataBinding.editPingUrlLayout.setOnClickListener {
            isEditViewShow()
        }
        mDataBinding.butTitleEdit.setOnClickListener {
            if (isStartPing) {
                startPing("")
            }
            isEditViewShow()
        }
        mDataBinding.editPingUrl.addTextChangedListener {
            if (it != null && it.toString().isNotEmpty()) {
                hostUrl = it.toString()
            }
        }
        mDataBinding.editPingUrl.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val content = mDataBinding.editPingUrl.editableText.toString()
                hostUrl = content
                if (startPing(content)) {
                    isEditViewShow()
                }
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener true
        }

        mDataBinding.pingChart.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mPingAdapter = PingAdapter(pingMsList)
        mDataBinding.pingChart.adapter = mPingAdapter

        setViewShow(isStartPing)
    }

    private fun isEditViewShow() {
        //显示输入
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE)
        if (mDataBinding.editPingUrlLayout.visibility == View.GONE) {
            mDataBinding.editPingUrlLayout.visibility = View.VISIBLE

            mDataBinding.editPingUrl.isFocusableInTouchMode = true
            mDataBinding.editPingUrl.isFocusable = true
            mDataBinding.editPingUrl.requestFocus()
            if (imm is InputMethodManager) {
                imm.showSoftInput(mDataBinding.editPingUrl, 0);
            }
        } else {
            mDataBinding.editPingUrl.editableText.clear()
            mDataBinding.editPingUrl.isFocusableInTouchMode = false
            mDataBinding.editPingUrl.isFocusable = false
            mDataBinding.editPingUrl.requestFocus()
            mDataBinding.editPingUrlLayout.visibility = View.GONE

            if (imm is InputMethodManager) {
                imm.hideSoftInputFromWindow(mDataBinding.editPingUrl.windowToken, 0)
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun startPing(editUrl: String): Boolean {
        var host = "www.baidu.com"
        if (!TextUtils.isEmpty(editUrl)) host = editUrl
        if (!isStartPing) {
            mDataBinding.tvPingUrl.text = host
            mJob = WifiUtils.pingHost(host) {
                mDataBinding.tvPing.text = String.format("%.2f", it.lossRate.toDouble()) + "%"
                mDataBinding.tvPutPing.text = it.tx.toString()
                mDataBinding.tvRedPing.text = it.rx.toString()
                val percent = if (80 >= it.netDelay) {
                    it.netDelay.toInt()
                } else {
                    100
                }
                mDataBinding.pingDashboard.setPercent(percent)
                if (it.lossRate <= 30) {
                    mDataBinding.tvPingState.text = "网络极好"
                } else if (it.lossRate <= 60) {
                    mDataBinding.tvPingState.text = "网络良好"
                } else {
                    mDataBinding.tvPingState.text = "网络很差"
                }
                pingMsList.add(it)
                mPingAdapter?.notifyItemInserted(pingMsList.size - 1)
                mDataBinding.pingChart.scrollToPosition(pingMsList.size - 1)
            }
            setViewShow(true)
            isStartPing = true
        } else {
            mDataBinding.pingDashboard.setPercent(0)
            pingMsList.clear()
            mJob?.cancel()
            setViewShow(false)
            mPingAdapter?.notifyDataSetChanged()
            isStartPing = false
        }
        return true
    }


    /**
     * UI显示逻辑
     */
    private fun setViewShow(isStart: Boolean) {
        if (isStart) {
            mDataBinding.llPingInfo.visibility = View.VISIBLE
            mDataBinding.tvPingState.visibility = View.VISIBLE
            mDataBinding.tvPingUrl.visibility = View.VISIBLE
            mDataBinding.pingChart.visibility = View.VISIBLE
            mDataBinding.butStartPing.text = "停止"
            mDataBinding.butStartPing.setBackgroundResource( R.drawable.shape_c_primary_20)

        } else {
            mDataBinding.llPingInfo.visibility = View.GONE
            mDataBinding.tvPingState.visibility = View.GONE
            mDataBinding.tvPingUrl.visibility = View.GONE
            mDataBinding.pingChart.visibility = View.GONE

            mDataBinding.butStartPing.text = "Ping测试"
            mDataBinding.butStartPing.setBackgroundResource(R.drawable.shape_c_primary_20)
        }
    }

    override fun initDataObserver() {}
    override fun getLayoutId(): Int {
        return R.layout.activity_ping
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob?.cancel()
    }
}