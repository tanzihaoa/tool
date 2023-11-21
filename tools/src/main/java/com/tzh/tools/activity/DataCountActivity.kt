package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.widget.addTextChangedListener
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.gyf.immersionbar.ImmersionBar
import java.util.*
import com.tzh.tools.R
import com.tzh.tools.util.TimeUtil
import com.tzh.tools.util.clickDelay
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import java.text.SimpleDateFormat
import kotlin.math.log

/**
 * 日期计算
 */
class DataCountActivity : BaseLibActivity<BaseViewModel<*>>() {
    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            val intent = Intent(context, DataCountActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var mDatePicker: DatePicker
    private var mDateEntity = DateEntity.today()
    private val mCalendar = Calendar.getInstance()
    private var startTime = System.currentTimeMillis()
    private var interval = 0L
    private val DAY = 24 * 60 * 60 * 1000L
    private val simpleDateFormat = SimpleDateFormat("公历yyyy年MM月dd日", Locale.getDefault())

    override fun initView() {
        ImmersionBar.with(this)
            .titleBarMarginTop(findViewById(R.id.rl_top))
            .statusBarDarkFont(true)
            .init()

        val ivBack = findViewById<ImageView>(R.id.iv_back)
        val tvCurrentTime = findViewById<TextView>(R.id.tv_current_time)
        val etTime = findViewById<EditText>(R.id.et_time)
        val tvResult = findViewById<TextView>(R.id.tv_result)

        tvCurrentTime.text = simpleDateFormat.format(startTime)
        tvResult.text = simpleDateFormat.format(startTime)

        mDatePicker = DatePicker(this)

        ivBack.clickDelay {
            finish()
        }

        tvCurrentTime.clickDelay {
            mDatePicker.wheelLayout?.let {
                it.setDateMode(DateMode.YEAR_MONTH_DAY)
                it.setDateLabel("年", "月", "日")
                it.setRange(
                    DateEntity.target(1900, 1, 1),
                    DateEntity.target(2100, 12, 31),
                    mDateEntity
                )
                it.setCurtainEnabled(false)
            }

            mDatePicker.setOnDatePickedListener { year, month, day ->
                mCalendar.set(year, month - 1, day)
                mDateEntity.year = year
                mDateEntity.month = month
                mDateEntity.day = day
                startTime = mCalendar.time.time
                tvCurrentTime.text = simpleDateFormat.format(startTime)
                tvResult.text = simpleDateFormat.format(startTime + interval)
            }
            mDatePicker.show()
        }

        etTime.addTextChangedListener {
            interval = if (it != null && it.isNotEmpty()) {
                it.toString().toInt() * DAY
            } else {
                0L
            }
            tvResult.text = simpleDateFormat.format(startTime + interval)
        }
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_data_count
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}