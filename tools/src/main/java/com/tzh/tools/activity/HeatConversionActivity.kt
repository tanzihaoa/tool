package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Description:热量转换
 * @Author: LYL
 * @CreateDate: 2023/6/7 20:17
 */
class HeatConversionActivity : BaseLibActivity<BaseViewModel<*>>() {

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
    var et1: EditText? = null
    var et2: EditText? = null
    var et3: EditText? = null
    var et4: EditText? = null
    var et5: EditText? = null
    var et6: EditText? = null
    var et7: EditText? = null
    var et8: EditText? = null
    var et9: EditText? = null
    var et10: EditText? = null
    var et11: EditText? = null

    companion object {
        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?,adType: Int = 1) {
            var intent = Intent(context, HeatConversionActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE,adType)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_heat_conversion
    }


    override fun initView() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .statusBarDarkFont(true)
            .titleBar(findViewById(R.id.fl_top))
            .init()

        findViewById<TextView>(R.id.tv_title).text = "热量转换"

        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }

        et1 = findViewById<EditText>(R.id.et1)
        et2 = findViewById<EditText>(R.id.et2)
        et3 = findViewById<EditText>(R.id.et3)
        et4 = findViewById<EditText>(R.id.et4)
        et5 = findViewById<EditText>(R.id.et5)
        et6 = findViewById<EditText>(R.id.et6)
        et7 = findViewById<EditText>(R.id.et7)
        et8 = findViewById<EditText>(R.id.et8)
        et9 = findViewById<EditText>(R.id.et9)
        et10 = findViewById<EditText>(R.id.et10)
        et11 = findViewById<EditText>(R.id.et11)

        et1?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et1?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et1)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et1)
                            return@let
                        }
                        startCount(it.toString(), et1)
                    }
                }
            }

        })
        et2?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et2?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et2)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et2)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("0.001"))
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et2)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })
        et3?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et3?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et3)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et3)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("2.7778e-7"),RoundingMode.CEILING)
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et3)
                        } catch (e: Exception) {

                        }

                    }
                }
            }

        })
        et4?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et4?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et4)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et4)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("2.7778e-7"),RoundingMode.CEILING)
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et4)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })
        et5?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et5?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et5)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et5)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("0.102"),RoundingMode.CEILING)
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et5)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })
        et6?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et6?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et6)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et6)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("3.7767e-7"),RoundingMode.CEILING)
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et6)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })
        et7?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et7?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et7)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et7)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("3.7251e-7"),RoundingMode.CEILING)
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et7)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })
        et8?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et8?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et8)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et8)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("0.2389"),RoundingMode.CEILING)
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et8)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })

        et9?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et9?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et9)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et9)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("0.0002389"),RoundingMode.CEILING)
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et9)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })

        et10?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et10?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et10)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et10)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("0.0009478"),RoundingMode.CEILING)
                            extracted(bigDecimal)
                            startCount(bigDecimal.toString(), et10)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })

        et11?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (et11?.hasFocus() == true) {
                    s?.let {
                        if (it.isEmpty()) {
                            setValue("",et11)
                            return@let
                        }
                        if (it.toString()=="0") {
                            setValue("0",et11)
                            return@let
                        }
                        try {
                            val value = it.toString().toFloatOrNull() ?: 0f
                            val bigDecimal = BigDecimal(value.toString()).divide(BigDecimal("0.7376"),RoundingMode.CEILING)
                            et1?.setText(bigDecimal.stripTrailingZeros().toString())
                            startCount(bigDecimal.toString(), et11)
                        } catch (_: Exception) {

                        }

                    }
                }
            }

        })

    }



    private fun startCount(value: String, editText: EditText?) {
        if (editText?.id != et2?.id) {
            multiply(value, BigDecimal("0.001"), et2) // 转千焦
        }
        if (editText?.id != et3?.id) {
            multiply(value, BigDecimal("2.7778e-7"), et3) // 转千瓦/时

        }
        if (editText?.id != et4?.id) {
            multiply(value, BigDecimal("2.7778e-7"), et4) // 转度
        }
        if (editText?.id != et5?.id) {
            multiply(value, BigDecimal("0.102"), et5) // 转公斤/米
        }
        if (editText?.id != et6?.id) {
            multiply(value, BigDecimal("3.7767e-7"), et6) // 转米制马力/时
        }
        if (editText?.id != et7?.id) {
            multiply(value, BigDecimal("3.7251e-7"), et7) // 转英制马力/时
        }
        if (editText?.id != et8?.id) {
            multiply(value, BigDecimal("0.2389"), et8) // 转卡
        }
        if (editText?.id != et9?.id) {
            multiply(value, BigDecimal("0.0002389"), et9) // 转千卡
        }
        if (editText?.id != et10?.id) {
            multiply(value, BigDecimal("0.0009478"), et10) // 转英热单位
        }
        if (editText?.id != et11?.id) {
            multiply(value, BigDecimal("0.7376"), et11) // 转英尺/磅
        }
    }

    private fun setValue(text: String, editText: EditText?) {
        if (editText?.id != et1?.id) {
            et1?.setText(text)
        }
        if (editText?.id != et2?.id) {
            et2?.setText(text)
        }
        if (editText?.id != et3?.id) {
            et3?.setText(text)

        }
        if (editText?.id != et4?.id) {
            et4?.setText(text)
        }
        if (editText?.id != et5?.id) {
            et5?.setText(text)
        }
        if (editText?.id != et6?.id) {
            et6?.setText(text)
        }
        if (editText?.id != et7?.id) {
            et7?.setText(text)
        }
        if (editText?.id != et8?.id) {
            et8?.setText(text)
        }
        if (editText?.id != et9?.id) {
            et9?.setText(text)
        }
        if (editText?.id != et10?.id) {
            et10?.setText(text)
        }
        if (editText?.id != et11?.id) {
            et11?.setText(text)
        }
    }

    /**
     *  科学计数法转成普通数字
     */
    private fun onToNumber(number: String): String {
        val bd = BigDecimal(number)
        return bd.toPlainString()
    }

    private fun extracted(bigDecimal: BigDecimal) {
        et1?.setText(onToNumber(bigDecimal.stripTrailingZeros().toString()))
    }

    private fun multiply(value: String, divisor: BigDecimal, editText: EditText?) {
        try {
            val inputBigDecimal = BigDecimal(value)
            val tempValue = inputBigDecimal.multiply(divisor).setScale(7, RoundingMode.CEILING)
            editText?.setText(onToNumber(tempValue.stripTrailingZeros().toString()))
        } catch (_: Exception) {
        }

    }



    override fun initDataObserver() {

    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}