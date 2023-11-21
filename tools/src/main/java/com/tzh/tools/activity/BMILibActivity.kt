package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes

import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityBmiBinding
import com.tzh.tools.util.BarUtil
import com.tzh.tools.util.LogUtil
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * BMI 指数
 */
class BMILibActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isAdResume = false
    override fun onResume() {
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
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?,adType: Int = 1) {
            var intent = Intent(context, BMILibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE,adType)
            context.startActivity(intent)
        }
    }


    @SuppressLint("SetTextI18n")
    override fun initView() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .statusBarDarkFont(true)
            .init()

        findViewById<TextView>(R.id.tv_title).text = "BMI计算器"

        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }
        findViewById<View>(R.id.but_start_bmi).setOnClickListener {
            val heightStr = findViewById<EditText>(R.id.et_height).editableText.toString()
            val weightStr = findViewById<EditText>(R.id.et_weight).editableText.toString()
            if (TextUtils.isEmpty(heightStr)) {
                showToast("请输入身高！")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(weightStr)) {
                showToast("请输入体重！")
                return@setOnClickListener
            }
            val height = (heightStr.toInt() / 100F)
            val weight = weightStr.toFloat()

            val bmi = weight / (height * height)

            LogUtil.e("height------>$height")
            LogUtil.e("weight------>$weight")
            LogUtil.e("ok------>$bmi")

            val format = DecimalFormat("#.#")
            format.roundingMode = RoundingMode.HALF_UP
            findViewById<TextView>(R.id.tv_bmi_data).text = format.format(bmi)

            findViewById<LinearLayout>(R.id.ll_bmi_result).visibility = View.VISIBLE

            hideInput(findViewById<EditText>(R.id.et_height))
            hideInput(findViewById<EditText>(R.id.et_weight))
        }
    }

    //隐藏输入法
    fun hideInput(view: EditText) {
        try {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0) //强制隐藏键盘
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bmi
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}