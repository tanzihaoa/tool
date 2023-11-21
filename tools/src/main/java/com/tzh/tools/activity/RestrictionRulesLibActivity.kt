package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.model.TrafficRestrictionResult
import com.tzh.tools.vm.RestrictionRulesViewModel
import com.gyf.immersionbar.ImmersionBar

/**
 * @author: Created by xyc
 * created on: 2022/7/28
 * description:限行规则
 */
class RestrictionRulesLibActivity : BaseLibActivity<RestrictionRulesViewModel>() {


    companion object {
        const val KEY_DATA = "trafficRestriction_data"

        fun startActivity(context: Context,trafficRestrictionResult:TrafficRestrictionResult, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, RestrictionRulesLibActivity::class.java)
            intent.putExtra(KEY_DATA, trafficRestrictionResult)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    private var trafficRestrictionResult = TrafficRestrictionResult()

    private var isAdResume = false
    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
        }
    }


    override fun getLayoutId() = R.layout.activity_restriction_rules
    override fun getViewModel(): Class<RestrictionRulesViewModel> {
        return RestrictionRulesViewModel::class.java
    }

    override fun initVar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarView(findViewById<View>(R.id.v_status_bar))
            .statusBarDarkFont(false)
            .init()
        super.initVar()
        val data = intent.getSerializableExtra(KEY_DATA)
        if (data is TrafficRestrictionResult) {
            trafficRestrictionResult = data
        }
        if (trafficRestrictionResult.limits.isNotEmpty()) {
            var data = trafficRestrictionResult.limits[0]
            val stringBuffer = StringBuffer()
            for (item in data.plates) {
                stringBuffer.append(",")
                stringBuffer.append(item)
            }
            if (stringBuffer.isNotEmpty()) {
                findViewById<TextView>(R.id.tv_day).text = stringBuffer.substring(1).toString()
            } else {
                findViewById<TextView>(R.id.tv_day).text = "不限行"
            }
            findViewById<TextView>(R.id.tv_today_date).text = "— ${data.date} —"
        }

        if (trafficRestrictionResult.limits.size > 1) {
            var data = trafficRestrictionResult.limits[1]
            val stringBuffer = StringBuffer()
            for (item in data.plates) {
                stringBuffer.append(",")
                stringBuffer.append(item)
            }
            if (stringBuffer.isNotEmpty()) {
                findViewById<TextView>(R.id.tv_date_1_status).text = stringBuffer.substring(1).toString()
            } else {
                findViewById<TextView>(R.id.tv_date_1_status).text = "不限行"
            }
            findViewById<TextView>(R.id.tv_today_date).text = "${data.date}"
        }

        if (trafficRestrictionResult.limits.size > 2) {
            var data = trafficRestrictionResult.limits[2]
            val stringBuffer = StringBuffer()
            for (item in data.plates) {
                stringBuffer.append(",")
                stringBuffer.append(item)
            }
            if (stringBuffer.isNotEmpty()) {
                findViewById<TextView>(R.id.tv_date_2_status).text = stringBuffer.substring(1).toString()
            } else {
                findViewById<TextView>(R.id.tv_date_2_status).text = "不限行"
            }
            findViewById<TextView>(R.id.tv_today_date).text = "${data.date}"
        }

        findViewById<TextView>(R.id.tv_content).text = "一、处罚规定: ${trafficRestrictionResult.penalty} \n" +
                "二、限行区域：${trafficRestrictionResult.region} \n" +
                "三、详细说明: ${trafficRestrictionResult.remarks}"
    }

    override fun initView() {
        findViewById<View>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }

    override fun initDataObserver() {

    }


}