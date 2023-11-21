package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.net.apiLib
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.HashMap

/**
 * 随机笑话
 */
class JokeActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isAdResume = false

    private val mData = arrayListOf<String>()
    private var mPos = 0

    companion object {
        // 0:无广告    1:插屏    2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"

        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            startActivity(context, layoutResID, adType, true)
        }

        fun startActivity(
            context: Context,
            @LayoutRes layoutResID: Int?,
            adType: Int = 1,
            statusBarDarkFont: Boolean
        ) {
            val intent = Intent(context, JokeActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE, adType)
            intent.putExtra(KEY_DARK, statusBarDarkFont)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .statusBarDarkFont(darkFront)
            .init()

        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }

        val tv_joke_refresh = findViewById<TextView>(R.id.tv_joke_refresh)
        val tv_joke_content = findViewById<TextView>(R.id.tv_joke_content)

        launch(Dispatchers.IO) {
            runCatching {
                val params = HashMap<String, String>()
                params["postcode"] = "417700"
                apiLib.getJoke(params)
            }.onSuccess {
                if (it.code == 200) {
                    withContext(Dispatchers.Main) {
                        mData.clear()
                        it.data.list.forEach {
                            mData.add(it.content)
                        }
                        tv_joke_content?.text = mData[mPos]
                    }
                }
            }
        }

        tv_joke_refresh?.clickDelay {
            if (mData.isNotEmpty()) {
                mPos++
                if (mPos >= mData.size) {
                    mPos = 0
                }
                tv_joke_content?.text = mData[mPos]
            }
        }
    }

    override fun initDataObserver() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_joke
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            when (intent.getIntExtra(GOTO_TYPE, 0)) {
                1 -> {
                    LibAdBridge.instance.startInterstitial(this)
                }
                2 -> {
                    LibAdBridge.instance.startRewardVideo(this)
                }
            }
        }
    }
}