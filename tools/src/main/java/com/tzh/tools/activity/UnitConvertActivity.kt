package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.widget.addTextChangedListener
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityUnitConvertBinding
import com.tzh.tools.util.LogUtil
import com.gyf.immersionbar.ImmersionBar
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 长度、温度、时间转换
 */
class UnitConvertActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var etMm: EditText? = null
    private var etM: EditText? = null
    private var etLm: EditText? = null
    private var etDm: EditText? = null
    private var etCm: EditText? = null
    private var etLkm: EditText? = null
    private var etC: EditText? = null
    private var etF: EditText? = null
    private var etL: EditText? = null

    private var etSsd: EditText? = null
    private var etHsd: EditText? = null
    private var etKsd: EditText? = null
    private var etLsd: EditText? = null
    private var etLsds: EditText? = null

    private var etTimeS: EditText? = null
    private var etTimeM: EditText? = null
    private var etTimeH: EditText? = null
    private var etTimeT: EditText? = null
    private var etTimeZ: EditText? = null
    private var etTimeN: EditText? = null
    private var etTimeHm: EditText? = null
    private var etTimeWm: EditText? = null
    private var etTimePm: EditText? = null


    private var isAdResume = false

    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }
    companion object {
        const val Is_Bar_Dark = "isBarDark"
        fun startActivity(context: Context,type: String, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, UnitConvertActivity::class.java)
            intent.putExtra("type", type)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
        fun startActivity(context: Context,type: String, @LayoutRes layoutResID: Int?,isBarDark:Boolean = false) {
            var intent = Intent(context, UnitConvertActivity::class.java)
            intent.putExtra("type", type)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
                intent.putExtra(Is_Bar_Dark,isBarDark)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        if(intent.getBooleanExtra(Is_Bar_Dark,false)){
            ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init()
        }
        etMm = findViewById(R.id.et_mm)
        etM = findViewById(R.id.et_m)
        etLm = findViewById(R.id.et_lm)
        etDm = findViewById(R.id.et_dm)
        etCm = findViewById(R.id.et_cm)
        etLkm = findViewById(R.id.et_lkm)
        etC = findViewById(R.id.et_c)
        etF = findViewById(R.id.et_f)
        etL = findViewById(R.id.et_l)

        etSsd = findViewById(R.id.et_ssd)
        etHsd = findViewById(R.id.et_hsd)
        etKsd = findViewById(R.id.et_ksd)
        etLsd = findViewById(R.id.et_lsd)
        etLsds = findViewById(R.id.et_lsds)

        etTimeS = findViewById(R.id.et_time_s)
        etTimeM = findViewById(R.id.et_time_m)
        etTimeH = findViewById(R.id.et_time_h)
        etTimeT = findViewById(R.id.et_time_t)
        etTimeZ = findViewById(R.id.et_time_z)
        etTimeN = findViewById(R.id.et_time_n)
        etTimeHm = findViewById(R.id.et_time_hm)
        etTimeWm = findViewById(R.id.et_time_wm)
        etTimePm = findViewById(R.id.et_time_pm)

        val type = intent.getStringExtra("type") as String
        findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_title).text = type

        findViewById<View>(R.id.ll_length).visibility =
            if (type == "长度转换") View.VISIBLE else View.GONE
        findViewById<View>(R.id.ll_temp).visibility =
            if (type == "温度转换") View.VISIBLE else View.GONE
        findViewById<View>(R.id.ll_times).visibility =
            if (type == "时间转换") View.VISIBLE else View.GONE
        ImmersionBar.with(this)
            .titleBar(findViewById<View>(R.id.title_bar))
            .init()
        initData()
    }


    @SuppressLint("SetTextI18n")
    private fun initData() {
        //长度
        etMm?.addTextChangedListener {
            if (etMm?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etM?.setText("${bigDecimalMath(toLong, 1000.toString(), DI)}")
                etLm?.setText("${bigDecimalMath(toLong, 1000000.toString(), DI)}")
                etDm?.setText("${bigDecimalMath(toLong, 100.toString(), DI)}")
                etCm?.setText("${bigDecimalMath(toLong, 10.toString(), DI)}")
                etLkm?.setText("${bigDecimalMath(toLong, 1000000.toString(), DI)}")
                etC?.setText("${bigDecimalMath(toLong, 0.03.toString(), MU)}")
                etF?.setText("${bigDecimalMath(toLong, 0.3.toString(), MU)}")
                etL?.setText("${bigDecimalMath(toLong, 3.toString(), MU)}")
            } else {
                etM?.setText("0")
                etLm?.setText("0")
                etDm?.setText("0")
                etCm?.setText("0")
                etLkm?.setText("0")
                etC?.setText("0")
                etF?.setText("0")
                etL?.setText("0")
            }
        }
        etM?.addTextChangedListener {
            if (etM?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etMm?.setText("${bigDecimalMath(toLong, 1000.toString(), MU)}")
                etLm?.setText("${bigDecimalMath(toLong, 1000.toString(), DI)}")
                etDm?.setText("${bigDecimalMath(toLong, 10.toString(), MU)}")
                etCm?.setText("${bigDecimalMath(toLong, 100.toString(), MU)}")
                etLkm?.setText("${bigDecimalMath(toLong, 1000.toString(), DI)}")
                etC?.setText("${bigDecimalMath(toLong, 30.toString(), MU)}")
                etF?.setText("${bigDecimalMath(toLong, 300.toString(), MU)}")
                etL?.setText("${bigDecimalMath(toLong, 3000.toString(), MU)}")
            } else {
                etMm?.setText("0")
                etLm?.setText("0")
                etDm?.setText("0")
                etCm?.setText("0")
                etLkm?.setText("0")
                etC?.setText("0")
                etF?.setText("0")
                etL?.setText("0")
            }
        }
        etLm?.addTextChangedListener {
            if (etLm?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etMm?.setText(
                    bigDecimalMath(toLong, 1000000.toString(), MU).toString()
                )
                etM?.setText(
                    bigDecimalMath(toLong, 1000.toString(), MU).toString()
                )
                etDm?.setText(
                    bigDecimalMath(toLong, 10000.toString(), MU).toString()
                )
                etCm?.setText(
                    bigDecimalMath(toLong, 100000.toString(), MU).toString()
                )
                etLkm?.setText(
                    bigDecimalMath(toLong, 1.toString(), MU).toString()
                )
                etC?.setText(
                    bigDecimalMath(toLong, 30000.toString(), MU).toString()
                )
                etF?.setText(
                    bigDecimalMath(toLong, 300000.toString(), MU).toString()
                )
                etL?.setText(
                    bigDecimalMath(toLong, 3000000.toString(), MU).toString()
                )
            } else {
                val toLong = it.toString()
                etMm?.setText(
                    0.toString()
                )
                etM?.setText(
                    0.toString()
                )
                etDm?.setText(
                    0.toString()
                )
                etCm?.setText(
                    0.toString()
                )
                etLkm?.setText(
                    0.toString()
                )
                etC?.setText(
                    0.toString()
                )
                etF?.setText(
                    0.toString()
                )
                etL?.setText(
                    0.toString()
                )
            }
        }
        etDm?.addTextChangedListener {
            if (etDm?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etMm?.setText(
                    bigDecimalMath(toLong, 100.toString(), MU).toString()
                )
                etM?.setText(
                    bigDecimalMath(toLong, 10.toString(), DI).toString()
                )
                etLm?.setText(
                    bigDecimalMath(toLong, 10000.toString(), DI).toString()
                )
                etCm?.setText(
                    bigDecimalMath(toLong, 10.toString(), MU).toString()
                )
                etLkm?.setText(
                    bigDecimalMath(toLong, 10000.toString(), DI).toString()
                )
                etC?.setText(
                    bigDecimalMath(toLong, 3.toString(), MU).toString()
                )
                etF?.setText(
                    bigDecimalMath(toLong, 30.toString(), MU).toString()
                )
                etL?.setText(
                    bigDecimalMath(toLong, 300.toString(), MU).toString()
                )
            } else {
                etMm?.setText(
                    0.toString()
                )
                etM?.setText(
                    0.toString()
                )
                etLm?.setText(
                    0.toString()
                )
                etCm?.setText(
                    0.toString()
                )
                etLkm?.setText(
                    0.toString()
                )
                etC?.setText(
                    0.toString()
                )
                etF?.setText(
                    0.toString()
                )
                etL?.setText(
                    0.toString()
                )
            }
        }
        etCm?.addTextChangedListener {
            if (etCm?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etMm?.setText(
                    bigDecimalMath(toLong, 10.toString(), MU).toString()
                )
                etM?.setText(
                    bigDecimalMath(toLong, 100.toString(), DI).toString()
                )
                etLm?.setText(
                    bigDecimalMath(toLong, 1000.toString(), DI).toString()
                )
                etDm?.setText(
                    bigDecimalMath(toLong, 10.toString(), DI).toString()
                )
                etLkm?.setText(
                    bigDecimalMath(toLong, 1000.toString(), DI).toString()
                )
                etC?.setText(
                    bigDecimalMath(toLong, 3.toString(), DI).toString()
                )
                etF?.setText(
                    bigDecimalMath(toLong, 0.3.toString(), DI).toString()
                )
                etL?.setText(
                    bigDecimalMath(toLong, 33.toString(), DI).toString()
                )
            } else {
                etMm?.setText(
                    0.toString()
                )
                etM?.setText(
                    0.toString()
                )
                etLm?.setText(
                    0.toString()
                )
                etDm?.setText(
                    0.toString()
                )
                etLkm?.setText(
                    0.toString()
                )
                etC?.setText(
                    0.toString()
                )
                etF?.setText(
                    0.toString()
                )
                etL?.setText(
                    0.toString()
                )
            }
        }
        etLkm?.addTextChangedListener {
            if (etLkm?.hasFocus() == false) return@addTextChangedListener

            if (iss(it)) {
                val toLong = it.toString()
                etMm?.setText(
                    bigDecimalMath(toLong, 1000000.toString(), MU).toString()
                )
                etM?.setText(
                    bigDecimalMath(toLong, 1000.toString(), MU).toString()
                )
                etDm?.setText(
                    bigDecimalMath(toLong, 10000.toString(), MU).toString()
                )
                etCm?.setText(
                    bigDecimalMath(toLong, 100000.toString(), MU).toString()
                )
                etLm?.setText(
                    bigDecimalMath(toLong, 1.toString(), MU).toString()
                )
                etC?.setText(
                    bigDecimalMath(toLong, 30000.toString(), MU).toString()
                )
                etF?.setText(
                    bigDecimalMath(toLong, 300000.toString(), MU).toString()
                )
                etL?.setText(
                    bigDecimalMath(toLong, 3000000.toString(), MU).toString()
                )
            } else {
                etMm?.setText(
                    0.toString()
                )
                etM?.setText(
                    0.toString()
                )
                etDm?.setText(
                    0.toString()
                )
                etCm?.setText(
                    0.toString()
                )
                etLm?.setText(
                    0.toString()
                )
                etC?.setText(
                    0.toString()
                )
                etF?.setText(
                    0.toString()
                )
                etL?.setText(
                    0.toString()
                )
            }
        }
        etC?.addTextChangedListener {
            if (etC?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etMm?.setText(
                    bigDecimalMath(toLong, 33.33.toString(), MU).toString()
                )
                etM?.setText(
                    bigDecimalMath(toLong, 0.03.toString(), MU).toString()
                )
                etDm?.setText(
                    bigDecimalMath(toLong, 0.3.toString(), MU).toString()
                )
                etCm?.setText(bigDecimalMath(toLong, 3.3.toString(), MU).toString())
                etLkm?.setText(
                    bigDecimalMath(toLong, 0.00003.toString(), MU).toString()
                )
                etLm?.setText(
                    bigDecimalMath(toLong, 0.00003.toString(), MU).toString()
                )
                etF?.setText(
                    bigDecimalMath(toLong, 10.toString(), MU).toString()
                )
                etL?.setText(
                    bigDecimalMath(toLong, 100.toString(), MU).toString()
                )
            } else {
                etMm?.setText(
                    0.toString()
                )
                etM?.setText(
                    0.toString()
                )
                etDm?.setText(
                    0.toString()
                )
                etCm?.setText(0.toString())
                etLkm?.setText(
                    0.toString()
                )
                etLm?.setText(
                    0.toString()
                )
                etF?.setText(
                    0.toString()
                )
                etL?.setText(
                    0.toString()
                )
            }
        }
        etF?.addTextChangedListener {
            if (etF?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etMm?.setText(
                    bigDecimalMath(toLong, 3.333.toString(), MU).toString()
                )
                etM?.setText(
                    bigDecimalMath(toLong, 0.003.toString(), MU).toString()
                )
                etDm?.setText(
                    bigDecimalMath(toLong, 0.03.toString(), MU).toString()
                )
                etCm?.setText(bigDecimalMath(toLong, 0.3.toString(), MU).toString())
                etLkm?.setText(bigDecimalMath(toLong, 0.000003.toString(), MU).toString())
                etLm?.setText(bigDecimalMath(toLong, 0.000003.toString(), MU).toString())
                etC?.setText(
                    bigDecimalMath(toLong, 10.toString(), DI).toString()
                )
                etL?.setText(
                    bigDecimalMath(toLong, 10.toString(), MU).toString()
                )
            } else {
                etMm?.setText(
                    0.toString()
                )
                etM?.setText(
                    0.toString()
                )
                etDm?.setText(
                    0.toString()
                )
                etCm?.setText(0.toString())
                etLkm?.setText(
                    0.toString()
                )
                etLm?.setText(
                    0.toString()
                )
                etC?.setText(
                    0.toString()
                )
                etL?.setText(
                    0.toString()
                )
            }
        }
        etL?.addTextChangedListener {
            if (etL?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etMm?.setText(bigDecimalMath(toLong, 0.3333.toString(), MU).toString())
                etM?.setText(bigDecimalMath(toLong, 0.0003.toString(), MU).toString())
                etDm?.setText(bigDecimalMath(toLong, 0.003.toString(), MU).toString())
                etCm?.setText(bigDecimalMath(toLong, 0.03.toString(), MU).toString())
                etLkm?.setText(bigDecimalMath(toLong, 0.00000003.toString(), MU).toString())
                etLm?.setText(bigDecimalMath(toLong, 0.00000003.toString(), MU).toString())
                etC?.setText(bigDecimalMath(toLong, 0.01.toString(), MU).toString())
                etF?.setText(bigDecimalMath(toLong, 0.1.toString(), MU).toString())
            } else {
                etMm?.setText(
                    0.toString()
                )
                etM?.setText(
                    0.toString()
                )
                etDm?.setText(
                    0.toString()
                )
                etCm?.setText(
                    0.toString()
                )
                etLkm?.setText(
                    0.toString()
                )
                etLm?.setText(
                    0.toString()
                )
                etC?.setText(
                    0.toString()
                )
                etF?.setText(
                    0.toString()
                )
            }
        }


        //温度
        etSsd?.addTextChangedListener {
            if (etSsd?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString().toDouble()
                etHsd?.setText(
                    "${
                        BigDecimal(33.8 + (1.8 * (if (toLong + 1 == 1.0) 0 else toLong.toLong() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        ).stripTrailingZeros()
                    }"
                )
                etKsd?.setText(
                    "${
                        BigDecimal(274.15 + (1 * (if (toLong + 1 == 1.0) 0 else toLong.toLong() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etLsd?.setText(
                    "${
                        BigDecimal(493.47 + (1.8 * (if (toLong + 1 == 1.0) 0 else toLong.toLong() - 1))).setScale(
                            1,
                            RoundingMode.UP
                        )
                    }"
                )
                etLsds?.setText("${bigDecimalMath(toLong.toString(), 0.8.toString(), MU)}")
            } else {
                etHsd?.setText("0")
                etKsd?.setText("0")
                etLsd?.setText("0")
                etLsds?.setText("0")
            }
        }
        etHsd?.addTextChangedListener {
            if (etHsd?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString().toDouble()
                etSsd?.setText(
                    "${
                        BigDecimal(-17.22 + (0.56 * (if (toLong.toFloat() + 1 == 1f) 0f else toLong.toFloat() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etKsd?.setText(
                    "${
                        BigDecimal(255.92 + (0.56 * (if (toLong.toFloat() + 1 == 1f) 0f else toLong.toFloat() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etLsd?.setText(
                    "${
                        BigDecimal(460.67 + (1 * (if (toLong.toFloat() + 1 == 1f) 0f else toLong.toFloat() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etLsds?.setText(
                    "${
                        BigDecimal(-13.77 + (0.55 * (if (toLong.toFloat() + 1 == 1f) 0f else toLong.toFloat() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
            } else {
                etSsd?.setText(
                    "0"
                )
                etKsd?.setText(
                    "0"
                )
                etLsd?.setText(
                    "0"
                )
                etLsds?.setText(
                    "0"
                )
            }
        }
        etKsd?.addTextChangedListener {
            if (etKsd?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString().toDouble()
                etSsd?.setText(
                    "${
                        BigDecimal(-272.15 + (0.56 * (if (toLong + 1 == 1.0) 0L else toLong.toLong() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        ).stripTrailingZeros()
                    }"
                )
                etLsd?.setText(
                    "${
                        BigDecimal(1.8 + (1.8 * (if (toLong + 1 == 1.0) 0L else toLong.toLong() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        ).stripTrailingZeros()
                    }"
                )
                etHsd?.setText(
                    "${
                        BigDecimal(-456.03 + (1 * (if (toLong + 1 == 1.0) 0 else toLong.toLong() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        ).stripTrailingZeros()
                    }"
                )
                etLsds?.setText(
                    "${
                        BigDecimal(-217.72 + (0.8 * (if (toLong + 1 == 1.0) 0 else toLong.toLong() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        ).stripTrailingZeros()
                    }"
                )
            } else {
                etSsd?.setText(
                    "0"
                )
                etKsd?.setText(
                    "0"
                )
                etHsd?.setText("0")
                etLsds?.setText(
                    "0"
                )
            }
        }
        etLsd?.addTextChangedListener {
            if (etLsd?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString().toDouble()
                etSsd?.setText(
                    "${
                        BigDecimal(-272.59 + (0.53 * (if (toLong + 1 == 1.0) 0 else toLong.toInt() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etKsd?.setText(
                    "${
                        BigDecimal(0.55 + (0.55 * (if (toLong + 1 == 1.0) 0 else toLong.toInt() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etHsd?.setText(
                    "${
                        BigDecimal(-458.67 + (1 * (if (toLong + 1 == 1.0) 0 else toLong.toInt() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etLsds?.setText(
                    "${
                        BigDecimal(-218.07 + (0.44 * (if (toLong + 1 == 1.0) 0 else toLong.toInt() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
            } else {
                etSsd?.setText(
                    "0"
                )
                etKsd?.setText(
                    "0"
                )
                etHsd?.setText("0")
                etLsds?.setText(
                    "0"
                )
            }
        }
        etLsds?.addTextChangedListener {
            if (etLsds?.hasFocus() == false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString().toDouble()
                etSsd?.setText(
                    "${bigDecimalMath(toLong.toString(), 1.25.toString(), MU)}"
                )
                etKsd?.setText(
                    "${
                        BigDecimal(274.4 + (1.25 * (if (toLong + 1 == 1.0) 0 else toLong.toInt() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etHsd?.setText(
                    "${
                        BigDecimal(34.25 + (2.25 * (if (toLong + 1 == 1.0) 0 else toLong.toInt() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
                etLsd?.setText(
                    "${
                        BigDecimal(493.92 + (2.25 * (if (toLong + 1 == 1.0) 0 else toLong.toInt() - 1))).setScale(
                            2,
                            RoundingMode.UP
                        )
                    }"
                )
            } else {
                etSsd?.setText(
                    "0"
                )
                etKsd?.setText(
                    "0"
                )
                etHsd?.setText(
                    "0"
                )
                etLsd?.setText(
                    "0"
                )
            }
        }


        //时间
        etTimeS?.addTextChangedListener {
            if (etTimeS?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeM?.setText(
                    bigDecimalMath(toLong, 60.toString(), DI).toString()
                )
                etTimeH?.setText(
                    bigDecimalMath(toLong, 3600.toString(), DI).toString()
                )
                etTimeT?.setText(
                    bigDecimalMath(toLong, 86400.toString(), DI).toString()
                )
                etTimeZ?.setText(
                    bigDecimalMath(toLong, 604800.toString(), DI).toString()
                )
                etTimeN?.setText(
                    bigDecimalMath(toLong, 31536000.toString(), DI).toString()
                )
                etTimeHm?.setText(
                    bigDecimalMath(toLong, 1000.toString(), MU).toString()
                )
                etTimeWm?.setText(
                    bigDecimalMath(toLong, 100000.toString(), MU).toString()
                )
                etTimePm?.setText(
                    bigDecimalMath(toLong, 10000000.toString(), MU).toString()
                )
            } else {
                etTimeM?.setText(
                    0.toString()
                )
                etTimeH?.setText(
                    0.toString()
                )
                etTimeT?.setText(
                    0.toString()
                )
                etTimeZ?.setText(
                    0.toString()
                )
                etTimeN?.setText(
                    0.toString()
                )
                etTimeHm?.setText(
                    0.toString()
                )
                etTimeWm?.setText(
                    0.toString()
                )
                etTimePm?.setText(
                    0.toString()
                )
            }
        }
        etTimeM?.addTextChangedListener {
            if (etTimeM?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeS?.setText(
                    bigDecimalMath(toLong, 60.toString(), MU).toString()
                )
                etTimeH?.setText(
                    bigDecimalMath(toLong, 60.toString(), DI).toString()
                )
                etTimeT?.setText(
                    bigDecimalMath(toLong, 1440.toString(), DI).toString()
                )
                etTimeZ?.setText(
                    bigDecimalMath(toLong, 10080.toString(), DI).toString()
                )
                etTimeN?.setText(
                    bigDecimalMath(toLong, 525600.toString(), DI).toString()
                )
                etTimeHm?.setText(
                    bigDecimalMath(toLong, 60000.toString(), MU).toString()
                )
                etTimeWm?.setText(
                    bigDecimalMath(toLong, 60000000.toString(), MU).toString()
                )
                etTimePm?.setText(
                    bigDecimalMath(toLong, 600000000000.toString(), MU).toString()
                )
            } else {
                etTimeS?.setText(
                    0.toString()
                )
                etTimeH?.setText(
                    0.toString()
                )
                etTimeT?.setText(
                    0.toString()
                )
                etTimeZ?.setText(
                    0.toString()
                )
                etTimeN?.setText(
                    0.toString()
                )
                etTimeHm?.setText(
                    0.toString()
                )
                etTimeWm?.setText(
                    0.toString()
                )
                etTimePm?.setText(
                    0.toString()
                )
            }
        }
        etTimeH?.addTextChangedListener {
            if (etTimeH?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeS?.setText(
                    bigDecimalMath(toLong, 3600.toString(), MU).toString()
                )
                etTimeM?.setText(
                    bigDecimalMath(toLong, 60.toString(), MU).toString()
                )
                etTimeT?.setText(
                    bigDecimalMath(toLong, 24.toString(), DI).toString()
                )
                etTimeZ?.setText(
                    bigDecimalMath(toLong, 168.toString(), DI).toString()
                )
                etTimeN?.setText(
                    bigDecimalMath(toLong, 8760.toString(), DI).toString()
                )
                etTimeHm?.setText(
                    bigDecimalMath(toLong, 3600000.toString(), MU).toString()
                )
                etTimeWm?.setText(
                    bigDecimalMath(toLong, 3600000000.toString(), MU).toString()
                )
                etTimePm?.setText(
                    bigDecimalMath(toLong, 3600000000000.toString(), MU).toString()
                )
            } else {
                etTimeS?.setText(
                    0.toString()
                )
                etTimeM?.setText(
                    0.toString()
                )
                etTimeT?.setText(
                    0.toString()
                )
                etTimeZ?.setText(
                    0.toString()
                )
                etTimeN?.setText(
                    0.toString()
                )
                etTimeHm?.setText(
                    0.toString()
                )
                etTimeWm?.setText(
                    0.toString()
                )
                etTimePm?.setText(
                    0.toString()
                )
            }
        }
        etTimeT?.addTextChangedListener {
            if (etTimeT?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeS?.setText(
                    bigDecimalMath(toLong, 86400.toString(), MU).toString()
                )
                etTimeM?.setText(
                    bigDecimalMath(toLong, 1440.toString(), MU).toString()
                )
                etTimeH?.setText(
                    bigDecimalMath(toLong, 24.toString(), MU).toString()
                )
                etTimeZ?.setText(
                    bigDecimalMath(toLong, 7.toString(), DI).toString()
                )
                etTimeN?.setText(
                    bigDecimalMath(toLong, 365.toString(), DI).toString()
                )
                etTimeHm?.setText(
                    bigDecimalMath(toLong, 86400000.toString(), MU).toString()
                )
                etTimeWm?.setText(
                    bigDecimalMath(toLong, 86400000000.toString(), MU).toString()
                )
                etTimePm?.setText(
                    bigDecimalMath(toLong, 8640000000000000.toString(), MU).toString()
                )
            } else {
                etTimeS?.setText(
                    0.toString()
                )
                etTimeM?.setText(
                    0.toString()
                )
                etTimeH?.setText(
                    0.toString()
                )
                etTimeZ?.setText(
                    0.toString()
                )
                etTimeN?.setText(
                    0.toString()
                )
                etTimeHm?.setText(
                    0.toString()
                )
                etTimeWm?.setText(
                    0.toString()
                )
                etTimePm?.setText(
                    0.toString()
                )
            }
        }
        etTimeZ?.addTextChangedListener {
            if (etTimeZ?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeS?.setText(
                    bigDecimalMath(toLong, 604800.toString(), MU).toString()
                )
                etTimeM?.setText(
                    bigDecimalMath(toLong, 10080.toString(), MU).toString()
                )
                etTimeH?.setText(
                    bigDecimalMath(toLong, 168.toString(), MU).toString()
                )
                etTimeT?.setText(
                    bigDecimalMath(toLong, 7.toString(), MU).toString()
                )
                etTimeN?.setText(
                    bigDecimalMath(toLong, 52.14.toString(), DI).toString()
                )
                etTimeHm?.setText(
                    bigDecimalMath(toLong, 604800000.toString(), MU).toString()
                )
                etTimeWm?.setText(
                    bigDecimalMath(toLong, 604800000000.toString(), MU).toString()
                )
                etTimePm?.setText(
                    bigDecimalMath(toLong, 604800000000000.toString(), MU).toString()
                )
            } else {
                etTimeS?.setText(
                    0.toString()
                )
                etTimeM?.setText(
                    0.toString()
                )
                etTimeH?.setText(
                    0.toString()
                )
                etTimeT?.setText(
                    0.toString()
                )
                etTimeN?.setText(
                    0.toString()
                )
                etTimeHm?.setText(
                    0.toString()
                )
                etTimeWm?.setText(
                    0.toString()
                )
                etTimePm?.setText(
                    0.toString()
                )
            }
        }
        etTimeN?.addTextChangedListener {
            if (etTimeN?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeS?.setText(
                    bigDecimalMath(toLong, 31536000.toString(), MU).toString()
                )
                etTimeM?.setText(
                    bigDecimalMath(toLong, 525600.toString(), MU).toString()
                )
                etTimeH?.setText(
                    bigDecimalMath(toLong, 8760.toString(), MU).toString()
                )
                etTimeT?.setText(
                    bigDecimalMath(toLong, 365.toString(), MU).toString()
                )
                etTimeZ?.setText(
                    bigDecimalMath(toLong, 52.14.toString(), MU).toString()
                )
                etTimeHm?.setText(
                    bigDecimalMath(toLong, 31536000000.toString(), MU).toString()
                )
                etTimeWm?.setText(
                    bigDecimalMath(toLong, 31536000000000.toString(), MU).toString()
                )
                etTimePm?.setText(
                    bigDecimalMath(toLong, 31536000000000000.toString(), MU).toString()
                )
            } else {
                etTimeS?.setText(
                    0.toString()
                )
                etTimeM?.setText(
                    0.toString()
                )
                etTimeH?.setText(
                    0.toString()
                )
                etTimeT?.setText(
                    0.toString()
                )
                etTimeZ?.setText(
                    0.toString()
                )
                etTimeHm?.setText(
                    0.toString()
                )
                etTimeWm?.setText(
                    0.toString()
                )
                etTimePm?.setText(
                    0.toString()
                )
            }
        }
        etTimeHm?.addTextChangedListener {
            if (etTimeHm?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeS?.setText(
                    bigDecimalMath(toLong, 1000.toString(), DI).toString()
                )
                etTimeM?.setText(
                    bigDecimalMath(toLong, 60.toString(), DI).toString()
                )
                etTimeH?.setText(
                    bigDecimalMath(toLong, 3600.toString(), DI).toString()
                )
                etTimeT?.setText(
                    bigDecimalMath(toLong, 86400.toString(), DI).toString()
                )
                etTimeZ?.setText(
                    bigDecimalMath(toLong, 604800.toString(), DI).toString()
                )
                etTimeN?.setText(
                    bigDecimalMath(toLong, 31536000.toString(), DI).toString()
                )
                etTimeWm?.setText(
                    bigDecimalMath(toLong, 1000.toString(), MU).toString()
                )
                etTimePm?.setText(
                    bigDecimalMath(toLong, 100000000.toString(), MU).toString()
                )
            } else {
                etTimeS?.setText(
                    0.toString()
                )
                etTimeM?.setText(
                    0.toString()
                )
                etTimeH?.setText(
                    0.toString()
                )
                etTimeT?.setText(
                    0.toString()
                )
                etTimeZ?.setText(
                    0.toString()
                )
                etTimeN?.setText(
                    0.toString()
                )
                etTimeWm?.setText(
                    0.toString()
                )
                etTimePm?.setText(
                    0.toString()
                )
            }
        }
        etTimeWm?.addTextChangedListener {
            if (etTimeWm?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeS?.setText(
                    bigDecimalMath(toLong, 10000.toString(), DI).toString()
                )
                etTimeM?.setText(
                    bigDecimalMath(toLong, 120000.toString(), DI).toString()
                )
                etTimeH?.setText(
                    bigDecimalMath(toLong, 3600000.toString(), DI).toString()
                )
                etTimeT?.setText(
                    bigDecimalMath(toLong, 86400000.toString(), DI).toString()
                )
                etTimeZ?.setText(
                    bigDecimalMath(toLong, 6048000000.toString(), DI).toString()
                )
                etTimeN?.setText(
                    bigDecimalMath(toLong, 3153600000000.toString(), DI).toString()
                )
                etTimeHm?.setText(
                    bigDecimalMath(toLong, 1000.toString(), DI).toString()
                )
                etTimePm?.setText(
                    bigDecimalMath(toLong, 1000.toString(), MU).toString()
                )
            } else {
                etTimeS?.setText(
                    0.toString()
                )
                etTimeM?.setText(
                    0.toString()
                )
                etTimeH?.setText(
                    0.toString()
                )
                etTimeT?.setText(
                    0.toString()
                )
                etTimeZ?.setText(
                    0.toString()
                )
                etTimeN?.setText(
                    0.toString()
                )
                etTimeHm?.setText(
                    0.toString()
                )
                etTimePm?.setText(
                    0.toString()
                )
            }
        }
        etTimePm?.addTextChangedListener {
            if (etTimePm?.hasFocus()== false) return@addTextChangedListener
            if (iss(it)) {
                val toLong = it.toString()
                etTimeS?.setText(
                    bigDecimalMath(toLong, 100000000.toString(), DI).toString()
                )
                etTimeM?.setText(
                    bigDecimalMath(toLong, 1200000.toString(), DI).toString()
                )
                etTimeH?.setText(
                    bigDecimalMath(toLong, 360000000.toString(), DI).toString()
                )
                etTimeT?.setText(
                    bigDecimalMath(toLong, 86400000000.toString(), DI).toString()
                )
                etTimeZ?.setText(
                    bigDecimalMath(toLong, 604800000000.toString(), DI).toString()
                )
                etTimeN?.setText(
                    bigDecimalMath(toLong, 3153600000000.toString(), DI).toString()
                )
                etTimeHm?.setText(
                    bigDecimalMath(toLong, 1000000.toString(), MU).toString()
                )
                etTimeWm?.setText(
                    bigDecimalMath(toLong, 100000.toString(), MU).toString()
                )
            } else {
                etTimeS?.setText(
                    0.toString()
                )
                etTimeM?.setText(
                    0.toString()
                )
                etTimeH?.setText(
                    0.toString()
                )
                etTimeT?.setText(
                    0.toString()
                )
                etTimeZ?.setText(
                    0.toString()
                )
                etTimeN?.setText(
                    0.toString()
                )
                etTimeHm?.setText(
                    0.toString()
                )
                etTimeWm?.setText(
                    0.toString()
                )
            }
        }
    }

    private val MU = "multiply"
    private val DI = "divide"

    private fun bigDecimalMath(argA: String, argB: String, operation: String): BigDecimal {
        return when (operation) {
            MU -> {
                BigDecimal(
                    BigDecimal(argA).multiply(BigDecimal(argB)).setScale(4, RoundingMode.DOWN)
                        .stripTrailingZeros().toPlainString()
                )
            }
            DI -> {
                if (BigDecimal(argA).divide(BigDecimal(argB), 1, RoundingMode.DOWN)
                        .toString().length > 7
                ) {
                    BigDecimal(argA).divide(BigDecimal(argB), 1, RoundingMode.UP)
                        .stripTrailingZeros()
                } else {
                    BigDecimal(
                        BigDecimal(argA).divide(BigDecimal(argB), 4, RoundingMode.DOWN)
                            .stripTrailingZeros().toPlainString()
                    )
                }
            }
            else -> {
                BigDecimal(0.0)
            }
        }
    }

    private fun iss(it: Editable?): Boolean {
        val result = it?.toString()?.toFloatOrNull()
        return (it != null && it.toString().isNotEmpty() && result != null)
    }


    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_unit_convert
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

}