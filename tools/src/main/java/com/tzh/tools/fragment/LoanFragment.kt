package com.tzh.tools.fragment

import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.activity.LoanResultLibActivity
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.FragmentLoanBinding
import com.tzh.tools.dialog.DialogUtils
import com.tzh.tools.util.InputFilterMinMax
import com.hjq.toast.ToastUtils

/**
 * 首页小说与头条 过渡
 */
class LoanFragment : BaseFragment<BaseViewModel<*>>() {
    companion object {
        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?) =
            LoanFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }

        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?,@LayoutRes activityLayoutResID: Int?) =
            LoanFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                    activityLayoutResID?.let {
                        putInt(KEY_ACTIVITY_LAYOUT, activityLayoutResID)
                    }
                }
            }

    }

    var isInterestType = true
    var loanYearNum = 5
    var loanRate = 3.25f

    override fun initView() {
//        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
//        mDataBinding.lifecycleOwner = this
        initLoan()
    }

    private fun initLoan() {
        val etLoanNum =view?.findViewById<EditText>(R.id.et_loan_num)
        val tvToolLoanType =view?.findViewById<TextView>(R.id.tv_tool_loan_type)
        val tvToolLoanTime =view?.findViewById<TextView>(R.id.tv_tool_loan_time)
        val tvToolLoanRate =view?.findViewById<TextView>(R.id.tv_tool_loan_rate)
        val tvStartLoan =view?.findViewById<TextView>(R.id.tv_start_loan)


            etLoanNum?.filters = arrayOf<InputFilter>(InputFilterMinMax(0f, 9999f))
            tvToolLoanType?.setOnClickListener {
                val list = listOf("等额本息(每月等额还款)", "等额本金(每月递减还款)")
                DialogUtils.showLoanTypeDialog(requireActivity(), list, "还款方式") {
                    tvToolLoanType.text = it
                    isInterestType = it == "等额本息(每月等额还款)"
                }
            }
            tvToolLoanTime?.setOnClickListener {
                val list = listOf("5年(60个月)",
                    "10年(120个月)",
                    "15年(180个月)",
                    "20年(240个月)",
                    "25年(300个月)",
                    "30年(360个月)")
                DialogUtils.showLoanTypeDialog(requireActivity(), list, "还款年数") {
                    tvToolLoanTime.text = it
                    loanYearNum = it.substring(0, it.indexOf("年")).toInt()
                }
            }
            tvToolLoanRate?.setOnClickListener {
                val list = listOf("基准利率(3.25%)", "1.1倍(3.575%)", "1.2倍(3.9%)")
                DialogUtils.showLoanTypeDialog(requireActivity(), list, "袋款利率") {
                    tvToolLoanRate.text = it
                    loanRate = it.substring(it.indexOf("(") + 1, it.lastIndexOf(")") - 1).toFloat()
                }
            }
            tvStartLoan?.setOnClickListener {

                val activityLayoutResID = arguments?.getInt(KEY_ACTIVITY_LAYOUT)

                if (!etLoanNum?.text.isNullOrEmpty()) {
                    LoanResultLibActivity.startActivity(requireContext(),
                        isInterestType,
                        etLoanNum?.text.toString().toInt(),
                        loanYearNum,
                        loanRate,activityLayoutResID)
                } else {
                    ToastUtils.show("请填写贷款金额")
                }
            }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_loan
    }
    override fun initDataObserver() {}
}