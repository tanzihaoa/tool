package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.InputFilterMinMax

import com.google.android.material.tabs.TabLayout
import com.hjq.toast.ToastUtils

/**
 * 房贷计算输入界面
 */
class LoanLibActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var title="商业贷款"

    var laEtFundNum:EditText?=null
    var laEtFundRate:EditText?=null
    var laEtBusNum:EditText?=null
    var laEtBusRate:EditText?=null
    var laEtLoanYear:EditText?=null
    var laTab :TabLayout?=null
    override fun initView() {


        findViewById<View>(R.id.iv_back).setOnClickListener { onBackPressed() }
        findViewById<TextView>(R.id.tv_title).text = "房贷计算器"

        findViewById<View>(R.id.la_rate).setOnClickListener{
                startActivity(Intent(this@LoanLibActivity, LoanRateLibActivity::class.java))
            }

         laEtFundNum=  findViewById<EditText>(R.id.la_et_fund_num)
         laEtFundRate=  findViewById<EditText>(R.id.la_et_fund_rate)
         laEtBusNum=  findViewById<EditText>(R.id.la_et_bus_num)
         laEtBusRate=  findViewById<EditText>(R.id.la_et_bus_rate)
         laEtLoanYear=  findViewById<EditText>(R.id.la_et_loan_year)
         laTab=  findViewById<TabLayout>(R.id.la_tab)

        findViewById<View>(R.id.la_start).setOnClickListener{
                if(isStart(title)){
                    LoanCountLibActivity.startActivity(this@LoanLibActivity, title,
                        if (laEtFundNum?.text.isNullOrEmpty()) 0 else laEtFundNum?.text.toString()
                            .toInt(),
                        if (laEtFundRate?.text.isNullOrEmpty()) 0f else laEtFundRate?.text.toString()
                            .toFloat(),
                        if (laEtBusNum?.text.isNullOrEmpty()) 0 else laEtBusNum?.text.toString()
                            .toInt(),
                        if (laEtBusRate?.text.isNullOrEmpty()) 0f else laEtBusRate?.text.toString()
                            .toFloat(),
                        if (laEtLoanYear?.text.isNullOrEmpty()) 0 else laEtLoanYear?.text.toString()
                            .toInt(),null)
                }
            }
        laTab?.let {
            it.addTab(it.newTab().setText("商业贷款"))
            it.addTab(it.newTab().setText("公积金贷款"))
            it.addTab(it.newTab().setText("组合贷款"))
            it.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab) {
                    loanType(tab.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }

            laEtBusRate?.setText("4.1")
            laEtFundRate?.setText("3.25")
            laEtLoanYear?.filters= arrayOf<InputFilter>(InputFilterMinMax(0f,30f))
            laEtFundNum?.filters= arrayOf<InputFilter>(InputFilterMinMax(0f,9999f))
            laEtFundRate?.filters= arrayOf<InputFilter>(InputFilterMinMax(0f,10f))
            laEtBusNum?.filters= arrayOf<InputFilter>(InputFilterMinMax(0f,9999f))
            laEtBusRate?.filters= arrayOf<InputFilter>(InputFilterMinMax(0f,10f))

    }

    private fun loanType(loanType:Int){
        laEtBusNum?.setText("")
        laEtFundNum?.setText("")
        val laTv1 =findViewById<TextView>(R.id.la_tv1)
        val laTv2 =findViewById<TextView>(R.id.la_tv2)
        val laTv3 =findViewById<TextView>(R.id.la_tv3)
        val laTv4 =findViewById<TextView>(R.id.la_tv4)
        when(loanType){
            0->{
                title="商业贷款"
                laTv1.visibility=View.GONE
                laEtFundNum?.visibility=View.GONE
                laTv2.visibility=View.GONE
                laEtFundRate?.visibility=View.GONE
                laTv3?.visibility=View.VISIBLE
                laEtBusNum?.visibility=View.VISIBLE
                laTv4.visibility=View.VISIBLE
                laEtBusRate?.visibility=View.VISIBLE
            }
            1->{
                title="公积金贷款"
                laTv1.visibility=View.VISIBLE
                laEtFundNum?.visibility=View.VISIBLE
                laTv2.visibility=View.VISIBLE
                laEtFundRate?.visibility=View.VISIBLE
                laTv3.visibility=View.GONE
                laEtBusNum?.visibility=View.GONE
                laTv4.visibility=View.GONE
                laEtBusRate?.visibility=View.GONE
            }
            2->{
                title="组合贷款"
                laTv1.visibility=View.VISIBLE
                laEtFundNum?.visibility=View.VISIBLE
                laTv2.visibility=View.VISIBLE
                laEtFundRate?.visibility=View.VISIBLE
                laTv3.visibility=View.VISIBLE
                laEtBusNum?.visibility=View.VISIBLE
                laTv4.visibility=View.VISIBLE
                laEtBusRate?.visibility=View.VISIBLE
            }
        }

    }

    private fun isStart(title:String):Boolean{
        when(title){
            "商业贷款"->{
                if(laEtBusNum?.text.isNullOrEmpty()) {
                    ToastUtils.show("请填写贷款金额")
                    return false
                }
                if(laEtBusRate?.text.isNullOrEmpty())  {
                    ToastUtils.show("请填写贷款利率")
                    return false
                }
                if(laEtLoanYear?.text.isNullOrEmpty())  {
                    ToastUtils.show("请填写贷款年限")
                    return false
                }
                return true
            }
            "公积金贷款"->{
                if(laEtFundNum?.text.isNullOrEmpty()) {
                    ToastUtils.show("请填写贷款金额")
                    return false
                }
                if(laEtFundRate?.text.isNullOrEmpty())  {
                    ToastUtils.show("请填写贷款利率")
                    return false
                }
                if(laEtLoanYear?.text.isNullOrEmpty())  {
                    ToastUtils.show("请填写贷款年限")
                    return false
                }
                return true
            }
            "组合贷款"->{
                if(laEtFundNum?.text.isNullOrEmpty()) {
                    ToastUtils.show("请填写贷款金额")
                    return false
                }
                if(laEtFundRate?.text.isNullOrEmpty())  {
                    ToastUtils.show("请填写贷款利率")
                    return false
                }
                if(laEtBusNum?.text.isNullOrEmpty()) {
                    ToastUtils.show("请填写贷款金额")
                    return false
                }
                if(laEtBusRate?.text.isNullOrEmpty())  {
                    ToastUtils.show("请填写贷款利率")
                    return false
                }
                if(laEtLoanYear?.text.isNullOrEmpty())  {
                    ToastUtils.show("请填写贷款年限")
                    return false
                }
                return true
            }
        }
        return false
    }

    override fun initDataObserver() {
    }

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, LoanLibActivity::class.java)

            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_loan
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return  BaseViewModel::class.java
    }
}
