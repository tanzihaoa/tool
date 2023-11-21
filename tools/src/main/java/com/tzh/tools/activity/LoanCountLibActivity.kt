package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.adapter.LoanAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityLoanCountBinding
import com.tzh.tools.model.LoanModel

import com.google.android.material.tabs.TabLayout
import kotlin.math.floor

/**
 * 房贷计算结果
 */
class LoanCountLibActivity : BaseLibActivity<BaseViewModel<*>>() {

    //    private val wxWrapper by lazy { WXWrapper(requireContext()) }
    private var fundNum = 0                                   //公积金贷款金额
    private var fundRate = 0f                                 //公积金款利率
    private var busNum = 0                                    //商业贷款金额
    private var busRate = 0f                                  //商业贷款利率
    private var year = 0                                      //贷款年限
    private var totalMoney = 0.0                              //还款金额（本金+利息）
    private var totalInterests = 0.0                          //利息总额
    private lateinit var detailBus1: Array<DoubleArray>       //每月还款详情 商业 等额本息
    private lateinit var detailBus2: Array<DoubleArray>       //每月还款详情 商业 等额本金
    private lateinit var detailFund1: Array<DoubleArray>      //每月还款详情 公积金 等额本息
    private lateinit var detailFund2: Array<DoubleArray>      //每月还款详情 公积金 等额本金
    private var mList1 = mutableListOf<LoanModel>()           //等额本息
    private var mList2 = mutableListOf<LoanModel>()           //等额本金
    private var all1Reture = 0.0                                 //累计还款 等额本息
    private var all2Reture = 0.0                                 //累计还款 等额本金
    private var all1Interests = 0.0                              //累计利息
    private var all2Interests = 0.0                              //累计利息
    private lateinit var mAdapter: LoanAdapter


    companion object {
        fun startActivity(
            context: Context,
            title: String,
            fundNum: Int?,
            fundRate: Float?,
            busNum: Int?,
            busRate: Float?,
            year: Int,
            @LayoutRes layoutResID: Int?,
        ) {
            var intent = Intent(context, LoanCountLibActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("fundNum", fundNum)
            intent.putExtra("fundRate", fundRate)
            intent.putExtra("busNum", busNum)
            intent.putExtra("busRate", busRate)
            intent.putExtra("year", year)
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT and Intent.FLAG_ACTIVITY_NEW_TASK)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        fundNum = intent.getIntExtra("fundNum", 0) * 10000
        fundRate = intent.getFloatExtra("fundRate", 0f)
        busNum = intent.getIntExtra("busNum", 0) * 10000
        busRate = intent.getFloatExtra("busRate", 0f)
        year = intent.getIntExtra("year", 0)
        findViewById<View>(R.id.iv_back).setOnClickListener { onBackPressed() }
        findViewById<TextView>(R.id.tv_title).text = "${intent.getStringExtra("title")}计算"
        var lcTab = findViewById<TabLayout>(R.id.lc_tab)
        lcTab.addTab(lcTab.newTab().setText("等额本息"))
        lcTab.addTab(lcTab.newTab().setText("等额本金"))
        lcTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setCard(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        val lcBusNum = findViewById<TextView>(R.id.lc_bus_num)
        val lcBusRate = findViewById<TextView>(R.id.lc_bus_rate)
        val lcBusYear = findViewById<TextView>(R.id.lc_bus_year)
        val lcFundNum = findViewById<TextView>(R.id.lc_fund_num)
        val lcFundRate = findViewById<TextView>(R.id.lc_fund_rate)
        val lcFundYear = findViewById<TextView>(R.id.lc_fund_year)
        val lcFundCl = findViewById<View>(R.id.lc_fund_cl)
        val lcBusCl = findViewById<View>(R.id.lc_bus_cl)
        val lcInterest = findViewById<TextView>(R.id.lc_interest)
        val lcAllReturn = findViewById<TextView>(R.id.lc_all_return)
        val lcMonthReturn = findViewById<TextView>(R.id.lc_month_return)
        lcBusNum.text = "${busNum}"
        lcBusRate.text = "${intent.getFloatExtra("busRate", 0f)}%"
        lcBusYear.text = "${year}年"
        lcFundNum.text = "${fundNum}"
        lcFundRate.text = "${intent.getFloatExtra("fundRate", 0f)}%"
        lcFundYear.text = "${year}年"
        if (intent.getStringExtra("title").equals("商业贷款")) lcFundCl.visibility =
            View.GONE
        if (intent.getStringExtra("title").equals("公积金贷款")) lcBusCl.visibility =
            View.GONE

        detailBus1 = equalPrincipalandInterestMethod(busRate.toDouble(), busNum.toDouble())
        detailBus2 = EqualPrincipalMethod(busRate.toDouble(), busNum.toDouble())
        detailFund1 = equalPrincipalandInterestMethod(fundRate.toDouble(), fundNum.toDouble())
        detailFund2 = EqualPrincipalMethod(fundRate.toDouble(), fundNum.toDouble())
//        LogUtil.i(">>>busRate${busRate}  busNum${busNum} fundRate${fundRate}   fundNum${fundNum}")
        mList1.add(LoanModel("期数", "月供总额", "月供本金", "月供利息", "剩余本金"))
        mList2.add(LoanModel("期数", "月供总额", "月供本金", "月供利息", "剩余本金"))
        if (intent.getStringExtra("title").equals("商业贷款")) {
            initData(detailBus1, detailBus2)
        } else if (intent.getStringExtra("title").equals("公积金贷款")) {
            initData(detailFund1, detailFund2)
        } else if (intent.getStringExtra("title").equals("组合贷款")) {
            initData(detailBus1, detailBus2)
        }
        lcInterest.text = "累计利息(元) : ${"%.2f".format(all1Interests)}"
        lcAllReturn.text = "累计还款金额(元) : ${"%.2f".format(all1Reture)}"
        lcMonthReturn.text = mList1[1].str2
    }

    @SuppressLint("SetTextI18n")
    private fun setCard(tab: Int) {

        val lcInterest = findViewById<TextView>(R.id.lc_interest)
        val lcAllReturn = findViewById<TextView>(R.id.lc_all_return)
        val lcMonthReturn = findViewById<TextView>(R.id.lc_month_return)
        val lcTips = findViewById<TextView>(R.id.lc_tips)
        if (tab == 0) {         //等额本息
            val str1 =
                "<font color=\"#FBBE2B\">每月还款金额不变</font><font color=\"#000000\">，其中还款的本金逐月递增，利息逐月递减</font>"
            lcTips.text = Html.fromHtml(str1)
            mAdapter.setNewInstance(mList1)
            lcInterest.text = "累计利息(元) : ${"%.2f".format(all1Interests)}"
            lcAllReturn.text = "累计还款金额(元) : ${"%.2f".format(all1Reture)}"
            lcMonthReturn.text = mList1[1].str2
        } else {                //等额本金
            val str2 =
                "<font color=\"#FBBE2B\">每月还款金额递减</font><font color=\"#000000\">，其中每月还款的本金不变，利息逐月减少</font>"
            lcTips.text = Html.fromHtml(str2)
            mAdapter.setNewInstance(mList2)
            lcInterest.text = "累计利息(元) : ${"%.2f".format(all2Interests)}"
            lcAllReturn.text = "累计还款金额(元) : ${"%.2f".format(all2Reture)}"
            lcMonthReturn.text = mList2[1].str2
        }
        mAdapter.notifyDataSetChanged()
    }

    //商业 、公积金、组合
    private fun initData(list1: Array<DoubleArray>, list2: Array<DoubleArray>) {
        for (i in list1.indices) {                //等额本息
            mList1.add(
                LoanModel(
                    "${i + 1}",
                    "${list1[i][2]}",
                    "${list1[i][0]}",
                    "${list1[i][1]}",
                    "${list1[i][3]}"
                )
            )
        }
        for (i in list2.indices) {                //等额本金
            mList2.add(
                LoanModel(
                    "${i + 1}",
                    "${list2[i][2]}",
                    "${list2[i][0]}",
                    "${list2[i][1]}",
                    "${list2[i][3]}"
                )
            )
        }
        if (intent.getStringExtra("title").equals("组合贷款")) {
            mList1.clear()
            mList2.clear()
            mList1.add(LoanModel("期数", "月供总额", "月供本金", "月供利息", "剩余本金"))
            mList2.add(LoanModel("期数", "月供总额", "月供本金", "月供利息", "剩余本金"))
            for (i in list1.indices) {                //等额本息
                mList1.add(
                    LoanModel(
                        "${i + 1}",
                        "%.2f".format(detailBus1[i][2] + detailFund1[i][2]),
                        "%.2f".format(detailBus1[i][0] + detailFund1[i][0]),
                        "%.2f".format(detailBus1[i][1] + detailFund1[i][1]),
                        "%.2f".format(detailBus1[i][3] + detailFund1[i][3])
                    )
                )
            }
            for (i in list2.indices) {                //等额本金
                mList2.add(
                    LoanModel(
                        "${i + 1}",
                        "%.2f".format(detailBus2[i][2] + detailFund2[i][2]),
                        "%.2f".format(detailBus2[i][0] + detailFund2[i][0]),
                        "%.2f".format(detailBus2[i][1] + detailFund2[i][1]),
                        "%.2f".format(detailBus2[i][3] + detailFund2[i][3])
                    )
                )
            }
        }
        val lcRecy = findViewById<RecyclerView>(R.id.lc_recy)
        mAdapter = LoanAdapter(mList1)
        lcRecy.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        lcRecy.adapter = mAdapter
    }

    //等额本息
    fun equalPrincipalandInterestMethod(yIns: Double, loanMoney: Double): Array<DoubleArray> {
        totalMoney = 0.0
        totalInterests = 0.0
        val mIns: Double = yIns / 100 / 12 //月利率
        val months: Int = year * 12 //还款所需月份
        val pow = Math.pow(1 + mIns, months.toDouble())
        var remains: Double = loanMoney
        totalMoney = months * loanMoney * mIns * pow / (pow - 1) //总还款金额
        totalMoney = Math.floor(totalMoney * 100 + 0.5) / 100 //floor函数 保留两位小数
        totalInterests = totalMoney - loanMoney
        totalInterests = Math.floor(totalInterests * 100 + 0.5) / 100
        if (intent.getStringExtra("title").equals("商业贷款") && totalMoney > 0) {
            all1Interests = totalInterests
            all1Reture = totalMoney
        } else if (intent.getStringExtra("title").equals("公积金贷款") && totalMoney > 0) {
            all1Interests = totalInterests
            all1Reture = totalMoney
        } else if (intent.getStringExtra("title").equals("组合贷款") && totalMoney > 0) {
            all1Interests += totalInterests
            all1Reture += totalMoney
        }
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
        val months: Int = year * 12
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

        if (intent.getStringExtra("title").equals("商业贷款") && totalMoney > 0) {
            all2Interests = totalInterests
            all2Reture = totalMoney
        } else if (intent.getStringExtra("title").equals("公积金贷款") && totalMoney > 0) {
            all2Interests = totalInterests
            all2Reture = totalMoney
        } else if (intent.getStringExtra("title").equals("组合贷款") && totalMoney > 0) {
            all2Interests += totalInterests
            all2Reture += totalMoney
        }
//        LogUtil.i(">>>222all2Reture${all2Reture}   all2Interests${all2Interests}")
        return temp
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_loan_count
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}