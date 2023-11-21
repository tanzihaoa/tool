package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.adapter.CurrencyAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.dialog.DialogUtils
import com.tzh.tools.vm.CurrencyActivityViewModel
import com.gyf.immersionbar.ImmersionBar


/**
 * 汇率换算
 */
class CurrencyLibActivity : BaseLibActivity<CurrencyActivityViewModel>() {

    private var rate = 0.1425
    private var fromCode = "CNY"
    private var toCode = "USD"
    private lateinit var mAdapter: CurrencyAdapter
    private var isOneEt = true            //切换的哪个币种

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init()

        findViewById<View>(R.id.iv_back).setOnClickListener { onBackPressed() }
        findViewById<TextView>(R.id.tv_title).text = "汇率换算"
        val crClOne = findViewById<View>(R.id.cr_cl_one)
        val crLeftTv = findViewById<TextView>(R.id.cr_left_tv)
        val crIvOne = findViewById<ImageView>(R.id.cr_iv_one)
        val crTvOne = findViewById<TextView>(R.id.cr_tv_one)
        val crEtOne = findViewById<EditText>(R.id.cr_et_one)
        val crRate = findViewById<TextView>(R.id.cr_rate)
        val crEtTwo = findViewById<EditText>(R.id.cr_et_two)

        val crClTwo = findViewById<View>(R.id.cr_cl_two)
        val crRightTv = findViewById<TextView>(R.id.cr_right_tv)
        val crIvTwo = findViewById<ImageView>(R.id.cr_iv_two)
        val crTvTwo = findViewById<TextView>(R.id.cr_tv_two)

        crClOne.setOnClickListener {
            DialogUtils.showMoneyDialog(this@CurrencyLibActivity, mAdapter) { icon, name, sName ->
                crLeftTv.text = "1 ${name} 约等于"
                crIvOne.setImageResource(icon)
                crTvOne.text = name
                crEtOne.hint = sName
                fromCode = sName
                isOneEt = true
                if (fromCode == toCode) {
                    rate = 1.0
                    crRate.text = "$rate"
                    crEtOne.text = crEtTwo.text
                } else {
                    mViewModel.getRate(fromCode, toCode)
                }
            }
        }
        crClTwo.setOnClickListener {
            DialogUtils.showMoneyDialog(this@CurrencyLibActivity, mAdapter) { icon, name, sName ->
                crRightTv.text = name
                crIvTwo.setImageResource(icon)
                crTvTwo.text = name
                crEtTwo.hint = sName
                toCode = sName
                isOneEt = false
                if (fromCode == toCode) {
                    rate = 1.0
                    crRate.text = "$rate"
                    crEtTwo.text = crEtOne.text
                } else {
                    mViewModel.getRate(fromCode, toCode)
                }
            }
        }
        crEtOne.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                crEtOne.addTextChangedListener(oneTextWath)
                crEtOne.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(9))
                crEtTwo.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(15))
            } else {
                crEtOne.removeTextChangedListener(oneTextWath)
            }
        }
        crEtTwo.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                crEtTwo.addTextChangedListener(twoTextWath)
                crEtOne.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(15))
                crEtTwo.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(9))
            } else {
                crEtTwo.removeTextChangedListener(twoTextWath)
            }
        }

    }

    override fun loadData() {
        super.loadData()
        mViewModel.getCurrencyList()
    }

    override fun initDataObserver() {

        val crEtOne = findViewById<EditText>(R.id.cr_et_one)
        val crRate = findViewById<TextView>(R.id.cr_rate)
        val crEtTwo = findViewById<EditText>(R.id.cr_et_two)

        mAdapter = CurrencyAdapter(mViewModel.mList)
        mViewModel.exchangeRateLiveData.observe(this) {
            rate = it.exchange?.toDouble() ?: 0.0
            crRate.text = "$rate"
            if (crEtTwo.text.isNotEmpty() && crEtOne.text.isNotEmpty()) {
                if (isOneEt) {
                    crEtOne.setText("${"%.2f".format(crEtTwo.text.toString().toFloat() / rate)}")
                } else {
                    crEtTwo.setText("${"%.2f".format(crEtOne.text.toString().toFloat() * rate)}")
                }
            }
        }


    }

    private val oneTextWath = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            val crEtTwo = findViewById<EditText>(R.id.cr_et_two)

            if (s?.isNotEmpty() == true && s.toString().toFloat() != 0f) {
                crEtTwo.setText("${"%.2f".format(s.toString().toFloat() * rate)}")
            } else {
                crEtTwo.setText("")
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    private val twoTextWath = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val crEtOne = findViewById<EditText>(R.id.cr_et_one)

            if (s?.isNotEmpty() == true && s.toString().toFloat() != 0f) {
                crEtOne.setText("${"%.2f".format(s.toString().toFloat() / rate)}")
            } else {
                crEtOne.setText("")
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, CurrencyLibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_currency
    }

    override fun getViewModel(): Class<CurrencyActivityViewModel> {
        return CurrencyActivityViewModel::class.java
    }

}