package com.tzh.tools.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import com.tzh.tools.R
import com.tzh.tools.activity.LoanResultLibActivity
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.dialog.DialogUtils
import com.tzh.tools.util.InputFilterMinMax
import com.didichuxing.doraemonkit.util.TimeUtils
import com.hjq.toast.ToastUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * 首页小说与头条 过渡
 */
class DataCountFragment : BaseFragment<BaseViewModel<*>>() {
    companion object {
        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?) =
            DataCountFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }
    }


    override fun initView() {
        initDataCountView()
    }

    @SuppressLint("SetTextI18n")
    private fun defaultTime(view: TextView) {
        view.text = "公元 ${
            TimeUtils.getNowString(
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.CHINESE
                )
            )
        } ${TimeUtils.getChineseWeek(System.currentTimeMillis())}"
    }

    @SuppressLint("SetTextI18n")
    private fun initDataCountView() {
        val tv_select_time = view?.findViewById<TextView>(R.id.tv_select_time)
        val tv_before = view?.findViewById<TextView>(R.id.tv_before)
        val tv_after = view?.findViewById<TextView>(R.id.tv_after)
        val et_select_time_before = view?.findViewById<TextView>(R.id.et_select_time_before)
        val et_select_time_after = view?.findViewById<TextView>(R.id.et_select_time_after)

        tv_select_time?.apply {
            defaultTime(this)
        }
        tv_after?.apply {
            defaultTime(this)
        }
        tv_before?.apply {
            defaultTime(this)
        }
        et_select_time_before?.apply {
            addTextChangedListener {
                if (it != null && it.toString().isNotEmpty() && it.isDigitsOnly()) {
                    val resultMillis =
                        System.currentTimeMillis() - (it.toString().toLong() * 86400000)
                    tv_before?.text = "公元 ${
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.CHINESE
                        ).format(Date(resultMillis))
                    } ${TimeUtils.getChineseWeek(resultMillis)}"
                } else {
                    defaultTime(tv_before ?: TextView(context))
                }
            }
        }

        et_select_time_after?.apply {
            addTextChangedListener {
                if (it != null && it.toString().isNotEmpty() && it.toString()
                        .isDigitsOnly()
                ) {
                    val resultMillis =
                        System.currentTimeMillis() + (it.toString().toLong() * 86400000)
                    tv_after?.text = "公元 ${
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.CHINESE
                        ).format(Date(resultMillis))
                    } ${TimeUtils.getChineseWeek(resultMillis)}"
                } else {
                    defaultTime(tv_after ?: TextView(context))
                }
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_data_count
    }

    override fun initDataObserver() {}
}