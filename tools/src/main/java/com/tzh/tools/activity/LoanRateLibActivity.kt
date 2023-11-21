package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.adapter.RateAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.model.RateModel


/**
 * 房贷计算
 */
class LoanRateLibActivity : BaseLibActivity<BaseViewModel<*>>() {


    private var mList= mutableListOf<RateModel>()
    private lateinit var mAdapter: RateAdapter

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, LoanRateLibActivity::class.java)

            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_loan_rate
    }

    override fun initView() {

        findViewById<View>(R.id.iv_back).setOnClickListener { onBackPressed() }
        findViewById<TextView>(R.id.tv_title).text = "利率表"

        mList.run {
            add(RateModel("贷款年限","商业贷款","公积金贷款"))
            add(RateModel("5年以上","4.9%","3.25%"))
            add(RateModel("3-5年","4.75%","2.75%"))
            add(RateModel("1-3年","4.75%","2.75%"))
            add(RateModel("1年","4.35%","2.75%"))
            add(RateModel("6个月","4.35%","2.75%"))
        }
        mAdapter= RateAdapter(mList)
        findViewById<RecyclerView>(R.id.lr_recy).layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        findViewById<RecyclerView>(R.id.lr_recy).adapter = mAdapter
    }

    override fun initDataObserver() {
    }
    override fun getViewModel(): Class<BaseViewModel<*>> {
        return  BaseViewModel::class.java
    }
}