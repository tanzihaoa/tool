package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import java.text.SimpleDateFormat
import java.util.*


class DateIntervalActivity : BaseLibActivity<BaseViewModel<*>>() {
    companion object {
        /**
         * @param adType 0：插屏 1：激励视频
         */
        fun startActivity(
            context: Context,
            @LayoutRes layoutResID: Int?,
            statusBarDarkFont: Boolean = false,
            isShowAd: Boolean = false,
            adType: Int = 0
        ) {
            val intent = Intent(context, DateIntervalActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(KEY_DARK, statusBarDarkFont)
            intent.putExtra("isShowAd", isShowAd)
            intent.putExtra("adType", adType)
            context.startActivity(intent)
        }
    }

    private var isAdResume = false
    override fun onResume() {
        super.onResume()
        if (intent.getBooleanExtra("isShowAd", false)) {
            if (!isAdResume) {
                isAdResume = true
                if (intent.getIntExtra("adType", 0) == 0) {
                    LibAdBridge.instance.startInterstitial(this)
                } else if (intent.getIntExtra("adType", 0) == 1) {
                    LibAdBridge.instance.startRewardVideo(this)
                }
            }
        }
    }

    private val startDate = Calendar.getInstance()
    private val endDate = Calendar.getInstance()

    private val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:00 >", Locale.getDefault())

    override fun initView() {
        ImmersionBar.with(this)
            .statusBarDarkFont(darkFront)
            .init()

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val llStartTime = findViewById<LinearLayout>(R.id.llStartTime)
        val llEndTime = findViewById<LinearLayout>(R.id.llEndTime)
        val tvStartTime = findViewById<TextView>(R.id.tvStartTime)
        val tvEndTime = findViewById<TextView>(R.id.tvEndTime)
        val tvDay1 = findViewById<TextView>(R.id.tvDay1)
        val tvDay2 = findViewById<TextView>(R.id.tvDay2)
        val tvWeek = findViewById<TextView>(R.id.tvWeek)
        val tvMonth = findViewById<TextView>(R.id.tvMonth)
        val tvHour = findViewById<TextView>(R.id.tvHour)

        tvStartTime.text = simpleDateFormat.format(startDate.time)
        tvEndTime.text = simpleDateFormat.format(endDate.time)
        interval(startDate, endDate) { day1, day2, week, month, hour ->
            tvDay1.text = "${day1}天"
            tvDay2.text = "${day2}天"
            tvWeek.text = "${week}周"
            tvMonth.text = "${month}月"
            tvHour.text = "${hour}小时"
        }

        ivBack.clickDelay {
            finish()
        }

        llStartTime.clickDelay {
            showTimePicker(startDate, null, endDate) {
                startDate.time = it
                tvStartTime.text = simpleDateFormat.format(it)
                interval(startDate, endDate) { day1, day2, week, month, hour ->
                    tvDay1.text = "${day1}天"
                    tvDay2.text = "${day2}天"
                    tvWeek.text = "${week}周"
                    tvMonth.text = "${month}月"
                    tvHour.text = "${hour}小时"
                }
            }
        }

        llEndTime.clickDelay {
            showTimePicker(endDate, startDate, null) {
                endDate.time = it
                tvEndTime.text = simpleDateFormat.format(it)
                interval(startDate, endDate) { day1, day2, week, month, hour ->
                    tvDay1.text = "${day1}天"
                    tvDay2.text = "${day2}天"
                    tvWeek.text = "${week}周"
                    tvMonth.text = "${month}月"
                    tvHour.text = "${hour}小时"
                }
            }
        }
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_date_interval
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

    private fun interval(
        startDate: Calendar,
        endDate: Calendar,
        callback: (day1: Int, day2: Int, week: Int, month: Int, hour: Int) -> Unit
    ) {
        val intervalHour = ((endDate.timeInMillis - startDate.timeInMillis) / (1000L * 60 * 60)) + 1

        if (intervalHour < 0) {
            Toast.makeText(this, "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show()
            callback(0, 0, 0, 0, 0)
            return
        }

        val day1 = intervalHour / 24
        val week = intervalHour / 24 / 7

        val startDayOfWeek = startDate.get(Calendar.DAY_OF_WEEK)
        val endDayOfWeek = endDate.get(Calendar.DAY_OF_WEEK)
        val zeroOfStartDate =
            startDate.timeInMillis - (startDate.timeInMillis % (1000L * 60 * 60 * 24))
        val zeroOfEndDate = endDate.timeInMillis - (endDate.timeInMillis % (1000L * 60 * 60 * 24))
        var day2 = (zeroOfEndDate - zeroOfStartDate) / (1000L * 60 * 60 * 24)
        var weekend = 0L // 周末天数
        var start = 0L // 掐去到第一个周末之前的相差天数
        start += day2
        if ((zeroOfEndDate - zeroOfStartDate) / (1000L * 60 * 60 * 24) >= 7) {
            when (startDayOfWeek) {
                3 -> { // 周二
                    weekend += 2
                    start -= 5
                }
                4 -> { // 周三
                    weekend += 2
                    start -= 4
                }
                5 -> { // 周四
                    weekend += 2
                    start -= 3
                }
                6 -> { // 周五
                    weekend += 2
                    start -= 2
                }
                7 -> { // 周六
                    weekend += 2
                    start -= 1
                }
                1 -> { // 周日
                    weekend += 1
                }
            }
            weekend += (start / 7) * 2
            if (start % 7 != 0L) {
                if (endDayOfWeek == 1) {
                    weekend += 1
                }
            } else {
                weekend -= 1
            }
        } else {
            when (startDayOfWeek) {
                7 -> {
                    if (endDayOfWeek == 1) {
                        weekend += 1
                    } else {
                        weekend += 2
                    }
                }
                1 -> {
                    if (endDayOfWeek != 1) {
                        weekend += 1
                    }
                }
                else -> {
                    if (endDayOfWeek == 1) {
                        weekend += 1
                    } else {
                        val calendar = Calendar.getInstance()
                        calendar.time = startDate.time
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                        if (endDate.timeInMillis > calendar.timeInMillis) {
                            weekend += 2
                        }
                    }
                }
            }
        }
        day2 -= weekend

        var month = 0
        if ((endDate.timeInMillis - startDate.timeInMillis) / (1000L * 60 * 60 * 24 * 30) >= 1) {
            val startYear = startDate.get(Calendar.YEAR)
            val startMonth = startDate.get(Calendar.MONTH) + 1
            val endYear = endDate.get(Calendar.YEAR)
            val endMonth = endDate.get(Calendar.MONTH) + 1
            if (startYear != endYear) {
                month += (endYear - startYear) * 12
            }
            if (endMonth > startMonth) {
                month += endMonth - startMonth
            } else {
                month -= startMonth - endMonth
            }
        }

        callback(day1.toInt(), day2.toInt(), week.toInt(), month, intervalHour.toInt())
    }

    private fun showTimePicker(
        selectedDate: Calendar,
        startDate: Calendar?,
        endDate: Calendar?,
        callback: (data: Date) -> Unit
    ) {
        var sDate = Calendar.getInstance()
        var eDate = Calendar.getInstance()
        sDate.set(1900, 1 - 1, 1, 0, 0, 0)
        eDate.set(2100, 12 - 1, 31, 23, 59, 59)

        if (startDate != null) {
            sDate = startDate
        }

        if (endDate != null) {
            eDate = endDate
        }

        TimePickerBuilder(this) { date, v ->
            callback(date)
        }
//                .setTitleText("Title") // 标题文字
//                .setSubmitText("Sure") // 确认按钮文字
//                .setCancelText("Cancel") // 取消按钮文字
//                .setTitleSize(20)//标题文字大小
//                .setContentTextSize(18) // 滚轮文字大小
//                .setTitleColor(Color.BLACK) // 标题文字颜色
//                .setSubmitColor(Color.BLUE) // 确定按钮文字颜色
//                .setCancelColor(Color.BLUE) // 取消按钮文字颜色
//                .setTitleBgColor(Color.WHITE) // 标题背景颜色
//                .setBgColor(Color.WHITE) // 滚轮背景颜色
//                .isCyclic(true) // 是否循环滚动
//                .isDialog(true) // 是否显示为对话框样式

            .setType(booleanArrayOf(true, true, true, true, false, false)) // 默认全部显示
            .setOutSideCancelable(false) // 点击屏幕，点在控件外部范围时，是否取消显示
            .setDate(selectedDate) // 默认系统时间
            .setRangDate(sDate, eDate) // 起始终止年月日设定
            .isCenterLabel(true) // 是否只显示中间选中项的label文字
            .build()
            .show()
    }
}