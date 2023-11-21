package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityBirthdayBinding
import com.tzh.tools.dialog.SelectBirthdayDialogFragment
import com.tzh.tools.net.apiLib
import com.tzh.tools.util.MMKVUtil
import com.tzh.tools.util.clickDelay

import kotlinx.coroutines.launch

/**
 * 生日密码
 */
class BirthdayLibActivity : BaseLibActivity<BaseViewModel<*>>() {

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, BirthdayLibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }


    private var isAdResume = false
    override fun onResume() {
        super.onResume()
        if(!isAdResume){
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun reloadData() {
        val tvResult=  findViewById<TextView>(R.id.tv_result)
        val tvPasswordTitle=  findViewById<TextView>(R.id.tv_password_title)
        val tvStart=  findViewById<TextView>(R.id.tv_start)
        val group=  findViewById<View>(R.id.group)
        launch {
            runCatching {
                val params = hashMapOf<String, String>()
                params["birthday"] = MMKVUtil.get("birthday", "01-01") as String
                apiLib.birthdayPassword(params)
            }.onSuccess {
                val result = it.data
                if (it.code == 200){
                    val finalString =
                        "静思语:${result.aphorism} \n 名人:${result.celebrity} \n 生日密码:${result.description} \n 缺点:${result.disadvantage} \n 优点:${result.advantage} \n 健康:${result.health} \n 幸运数字&守护星:${result.lucky} \n 塔罗牌:${result.tarot} \n 建议:${result.suggest}"
                    tvResult.text = Html.fromHtml(finalString)
                    tvPasswordTitle.text = "${it.data.birthday}生日密码"
                    tvStart.text = "    继续测算    "
                    group.visibility = View.VISIBLE
                }else if (it.code == 201){
                    showToast("请确认日期合理~")
                }else{
                    showToast("服务器异常~")
                }


            }
        }
    }

    override fun initView() {
        findViewById<View>(R.id.tv_select).clickDelay {
            val selectBirthdayDialogFragment = SelectBirthdayDialogFragment()
            selectBirthdayDialogFragment.apply {
                setonOk {
                    findViewById<TextView>(R.id.tv_select).text = MMKVUtil.get("birthday", "01-01") as String
                }
                show(supportFragmentManager, "start")
            }
        }
        findViewById<View>(R.id.tv_start).clickDelay {
            reloadData()
        }
        findViewById<View>(R.id.iv_back).clickDelay { finish() }
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_birthday
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
       return  BaseViewModel::class.java
    }
}