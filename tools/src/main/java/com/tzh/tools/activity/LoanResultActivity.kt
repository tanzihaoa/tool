package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.adapter.FragLoanAdapter
import com.tzh.tools.adapter.LoanModel
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityLoanResultBinding
import com.gyf.immersionbar.ImmersionBar
import kotlin.math.floor

/**
 * 房贷计算结果页
 */
class LoanResultLibActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isInterestType = true
    private var loanNum = 0
    private var loanYear = 0
    private var loanRate = 0f
    private var totalMoney = 0.0                              //还款金额（本金+利息）
    private var totalInterests = 0.0                          //利息总额
    private var all1Reture = 0.0                                 //累计还款 等额本息
    private var allReture = 0.0                                 //累计还款 等额本金
    private var allInterests = 0.0                              //累计利息
    private lateinit var detailLoan: Array<DoubleArray>
    private lateinit var mAdapter: FragLoanAdapter
    private var mList = mutableListOf<LoanModel>()

    companion object {
        fun startActivity( context: Context, isInterestType: Boolean,
                           loanNum: Int, loanYear: Int, loanRate: Float, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, LoanResultLibActivity::class.java)
            intent.putExtra("isInterestType", isInterestType)
            intent.putExtra("loanNum", loanNum)
            intent.putExtra("loanYear", loanYear)
            intent.putExtra("loanRate", loanRate)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .autoDarkModeEnable(true)
            .init()
        isInterestType = intent.getBooleanExtra("isInterestType", true)
        loanNum = intent.getIntExtra("loanNum", 0) * 10000
        loanYear = intent.getIntExtra("loanYear", 0)
        loanRate = intent.getFloatExtra("loanRate", 0f)

        findViewById<View>(R.id.iv_back).setOnClickListener { finish() }

        if (isInterestType)
            detailLoan = equalPrincipalandInterestMethod(loanRate.toDouble(), loanNum.toDouble())
        else
            detailLoan = EqualPrincipalMethod(loanRate.toDouble(), loanNum.toDouble())
        for (i in detailLoan.indices) {                //等额本息
            mList.add(
                LoanModel(
                    "${i + 1}",
                    "${detailLoan[i][2]}",
                    "${detailLoan[i][0]}",
                    "${detailLoan[i][1]}",
                    "${detailLoan[i][3]}"
                )
            )
        }
        mAdapter = FragLoanAdapter(mList)
        findViewById<RecyclerView>(R.id.recy_lr).layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        findViewById<RecyclerView>(R.id.recy_lr).adapter = mAdapter

        findViewById<TextView>(R.id.tv_loan_every_month).text = mList[0].str2
        findViewById<TextView>(R.id.tv_loan_all).text = "%.2f".format(totalMoney)
        findViewById<TextView>(R.id.tv_loan_interest_all).text = "%.2f".format(totalInterests)
    }

    //等额本息
    fun equalPrincipalandInterestMethod(yIns: Double, loanMoney: Double): Array<DoubleArray> {
        totalMoney = 0.0
        totalInterests = 0.0
        val mIns: Double = yIns / 100 / 12 //月利率
        val months: Int = loanYear * 12 //还款所需月份
        val pow = Math.pow(1 + mIns, months.toDouble())
        var remains: Double = loanMoney
        totalMoney = months * loanMoney * mIns * pow / (pow - 1) //总还款金额
        totalMoney = Math.floor(totalMoney * 100 + 0.5) / 100 //floor函数 保留两位小数
        totalInterests = totalMoney - loanMoney
        totalInterests = Math.floor(totalInterests * 100 + 0.5) / 100

        allInterests = totalInterests
        allReture = totalMoney
//        LogUtil.i(">>>111all1Reture${all1Reture}   all1Interests${all1Interests}")
        val temp = Array(months) {
            DoubleArray(
                4
            )
        }
        for (i in 0 until months) {
            if (i == months - 1) {
                temp[i][1] = remains * mIns
                temp[i][1] = Math.floor(temp[i][1] * 100 + 0.5) / 100
                temp[i][0] = remains
                temp[i][0] = Math.floor(temp[i][0] * 100 + 0.5) / 100
                temp[i][2] = temp[i][0] + temp[i][1]
                temp[i][2] = Math.floor(temp[i][2] * 100 + 0.5) / 100
                break
            }
            //由于精度问题 最后一个月实际的本金会有差别 需要单独计算
            temp[i][1] = remains * mIns
            temp[i][1] = Math.floor(temp[i][1] * 100 + 0.5) / 100
            temp[i][2] = totalMoney / months
            temp[i][2] = Math.floor(temp[i][2] * 100 + 0.5) / 100
            temp[i][0] = temp[i][2] - temp[i][1]
            temp[i][0] = Math.floor(temp[i][0] * 100 + 0.5) / 100
            remains -= temp[i][0]
            temp[i][3] = floor(remains)
        }
        //temp[][0]为每月还款本金 temp[][1]为每月还款利息 temp[][2]为每月还款总额
        return temp
    }

    //等额本金
    fun EqualPrincipalMethod(yIns: Double, loanMoney: Double): Array<DoubleArray> {
        totalMoney = 0.0
        totalInterests = 0.0
        val mIns: Double = yIns / 100 / 12 //月利率
        val months: Int = loanYear * 12
        var remains: Double = loanMoney
        var sum = 0.0 // 总计还款金额
        val temp = Array(months) {
            DoubleArray(
                4
            )
        }
        for (i in 0 until months) {
            temp[i][0] = loanMoney / months
            temp[i][0] = Math.floor(temp[i][0] * 100 + 0.5) / 100
            temp[i][1] = remains * mIns
            temp[i][1] = Math.floor(temp[i][1] * 100 + 0.5) / 100
            remains -= temp[i][0]
            temp[i][2] = temp[i][0] + temp[i][1]
            temp[i][2] = Math.floor(temp[i][2] * 100 + 0.5) / 100
            sum += temp[i][2]
            temp[i][3] = if (remains > 0) floor(remains) else 0.00
        }
        //temp[][0]为每月还款本金 temp[][1]为每月还款利息 temp[][2]为每月还款总额
        totalMoney = sum
        totalMoney = Math.floor(totalMoney * 100 + 0.5) / 100
        totalInterests = totalMoney - loanMoney
        totalInterests = Math.floor(totalInterests * 100 + 0.5) / 100

        allInterests = totalInterests
        allReture = totalMoney
//        LogUtil.i(">>>222all2Reture${all2Reture}   all2Interests${all2Interests}")
        return temp
    }

    override fun initDataObserver() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_loan_result
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}