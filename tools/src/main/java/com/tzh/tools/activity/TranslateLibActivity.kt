package com.tzh.tools.activity


import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.net.apiLib
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.launch

/**
 * 简繁转换
 */
class TranslateLibActivity : BaseLibActivity<BaseViewModel<*>>() {

    companion object {
        const val Is_Bar_Dark = "isBarDark"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?,isBarDark:Boolean = false) {
            var intent = Intent(context, TranslateLibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
                intent.putExtra(Is_Bar_Dark,isBarDark)
            }
            context.startActivity(intent)
        }
    }

    private var isAdResume = false
    override fun getLayoutId(): Int {
        return R.layout.activity_translate
    }

    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

    override fun initView() {
        if(intent.getBooleanExtra(ZipCodeActivity.Is_Bar_Dark,false)){
            ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init()
        }

        findViewById<View>(R.id.iv_back).clickDelay { finish() }
        findViewById<TextView>(R.id.tv_simplified).clickDelay {
            if (checkInput()) {
                translate(findViewById<EditText>(R.id.et_input).text.toString(), "0")
            } else {
                showToast("请输入内容~")
            }
        }
        findViewById<TextView>(R.id.tv_traditional).clickDelay {
            if (checkInput()) {
                translate(findViewById<EditText>(R.id.et_input).text.toString(), "1")
            } else {
                showToast("请输入内容~")
            }
        }
        findViewById<TextView>(R.id.tv_leetspeak).clickDelay {
            if (checkInput()) {
                translate(findViewById<EditText>(R.id.et_input).text.toString(), "2")
            } else {
                showToast("请输入内容~")
            }
        }
    }

    private fun checkInput(): Boolean {
        return findViewById<EditText>(R.id.et_input).text.toString().isNotEmpty()
    }

    private fun translate(input: String, type: String) {
        launch {
            runCatching {
                val params = hashMapOf<String, String>()
//                params["projectId"] = "1"
//                params["appClient"] = Constants.appClient
//                params["channel"] = AppInfo.channel
//                params["version"] = AppInfo.version
                params["text"] = input
                params["type"] = type
                apiLib.charConvert(params)
            }.onSuccess {
                if (it.code == 200) {
                    findViewById<TextView>(R.id.tv_result).text = it.data.outstr
                }
            }
        }
    }

    override fun initDataObserver() {
    }


}