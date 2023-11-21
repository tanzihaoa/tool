package com.tzh.tools.activity;

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.bigkoo.pickerview.utils.ChinaDate
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.clickDelay
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils
import java.util.Calendar

/**
 * Description:公历转农历
 *
 * @Author: LYL
 * @CreateDate: 2023/5/29 20:38
 */
class GregorianToDataActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isAdResume = false

    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, GregorianToDataActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }

        fun startActivity(
            context: Context,
            @LayoutRes layoutResID: Int?,
            statusBarDarkFont: Boolean,
        ) {
            var intent = Intent(context, GregorianToDataActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(KEY_DARK, statusBarDarkFont)
            context.startActivity(intent)
        }
    }

    private val mCalendar = Calendar.getInstance()
    private var mDatePicker: DatePicker? = null
    private var mDateEntity: DateEntity = DateEntity.today()

    override fun initView() {

        ImmersionBar.with(this)
            .statusBarDarkFont(darkFront)
            .init()

        findViewById<TextView>(R.id.tv_title).text = "公历转农历"

        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }

        val tvCurrentTime = findViewById<TextView>(R.id.tv_current_time)

        val tvResultContent = findViewById<TextView>(R.id.tv_result_content)

        findViewById<TextView>(R.id.tv_current_time).clickDelay {
            if (mDatePicker == null) {
                mDatePicker = DatePicker(this)
            }
            mDatePicker?.wheelLayout?.let {
                it.setDateMode(DateMode.YEAR_MONTH_DAY)
                it.setDateLabel("年", "月", "日")
                it.setRange(
                    DateEntity.target(1920, 1, 1),
                    DateEntity.target(2100, 1, 1),
                    mDateEntity
                )
                it.setCurtainEnabled(false)
            }
            mDatePicker?.setOnDatePickedListener { year, month, day ->
                tvCurrentTime.text = "${year}年${month}月${day}日"
                mDateEntity.day = day
                mDateEntity.month = month
                mDateEntity.year = year
                mCalendar.set(year, month, day)
                tvResultContent.text =
                    toDayData(mDateEntity.year, mDateEntity.month, mDateEntity.day)
            }
            mDatePicker?.show()


        }
    }

    private fun toDayData(year: Int, month: Int, day: Int): StringBuffer? {
        try {
            val l = ChinaDate.calElement(year, month, day)
            var sToday: StringBuffer? = StringBuffer()
            sToday!!.append(" 农历")
            sToday.append(l[0].toInt())
            sToday.append("年")
            sToday.append(l[1].toInt())
            sToday.append("月")
            sToday.append(l[2].toInt())
            sToday.append("日")

            sToday.toString()
            return sToday
        } catch (_: Exception) {
            ToastUtils.show("日期转换错误")
        }
        return null
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_gregorian_to_data
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

}
