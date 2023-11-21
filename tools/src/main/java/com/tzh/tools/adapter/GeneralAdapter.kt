package com.tzh.tools.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tzh.tools.R
import com.tzh.tools.model.YearByHoliday
import com.tzh.tools.util.CalendarTimeUtil
import com.necer.painter.CalendarAdapter
import com.necer.utils.CalendarUtil
import org.joda.time.LocalDate

class GeneralAdapter(yearByHoliday: YearByHoliday?) : CalendarAdapter() {
    private lateinit var context: Context
    private val yearByHoliday = yearByHoliday


    override fun getCalendarItemView(context: Context): View {
        this.context = context
        return LayoutInflater.from(context).inflate(R.layout.item_calendar_date_new, null)
    }

    // 设置今天的属性
    override fun onBindToadyView(
        calendarItemView: View?,
        localDate: LocalDate?,
        totalCheckedDateList: MutableList<LocalDate>?
    ) {
        val clContent = calendarItemView?.findViewById<View>(R.id.cl_date)
        // 阳历日期
        val tyDay = calendarItemView?.findViewById<TextView>(R.id.tv_day)
        // 农历日期
        val tvLunarDay = calendarItemView?.findViewById<TextView>(R.id.tv_lunar_day)

        val ivWorkStatus = calendarItemView?.findViewById<TextView>(R.id.iv_work_status)

        setSolar(tyDay, localDate)  // 设置阳历
        setLunar(tvLunarDay, localDate) // 设置农历
        setHoliday(tvLunarDay, ivWorkStatus, localDate) // 设置农历位置的节假日
        setSelectDay(clContent, tvLunarDay, tyDay, localDate, totalCheckedDateList) // 设置选中

        clContent?.alpha = 1f
    }

    // 设置当前周的视图
    override fun onBindCurrentMonthOrWeekView(
        calendarItemView: View?,
        localDate: LocalDate?,
        totalCheckedDateList: MutableList<LocalDate>?
    ) {
        val clContent = calendarItemView?.findViewById<View>(R.id.cl_date)

        // 阳历日期
        val tyDay = calendarItemView?.findViewById<TextView>(R.id.tv_day)
        // 农历日期
        val tvLunarDay = calendarItemView?.findViewById<TextView>(R.id.tv_lunar_day)

        val ivWorkStatus = calendarItemView?.findViewById<TextView>(R.id.iv_work_status)

        setSolar(tyDay, localDate)  // 设置阳历
        setLunar(tvLunarDay, localDate) // 设置农历
        setHoliday(tvLunarDay, ivWorkStatus, localDate) // 设置农历位置的节假日
        setSelectDay(clContent, tvLunarDay, tyDay, localDate, totalCheckedDateList) // 设置选中

        clContent?.alpha = 1f
    }

    override fun onBindLastOrNextMonthView(
        calendarItemView: View?,
        localDate: LocalDate?,
        totalCheckedDateList: MutableList<LocalDate>?
    ) {
        val clContent = calendarItemView?.findViewById<View>(R.id.cl_date)
        // 阳历日期
        val tyDay = calendarItemView?.findViewById<TextView>(R.id.tv_day)
        // 农历日期
        val tvLunarDay = calendarItemView?.findViewById<TextView>(R.id.tv_lunar_day)

        val ivWorkStatus = calendarItemView?.findViewById<TextView>(R.id.iv_work_status)

        setSolar(tyDay, localDate)  // 设置阳历
        setLunar(tvLunarDay, localDate) // 设置农历
        setHoliday(tvLunarDay, ivWorkStatus, localDate) // 设置农历位置的节假日

        clContent?.setBackgroundResource(R.drawable.bg_calendar_line_white)
        clContent?.alpha = 0.4f
    }


    private fun setSelectDay(
        clContent: View?, tvLunarDay: TextView?, tyDay: TextView?, localDate: LocalDate?,
        totalCheckedDateList: MutableList<LocalDate>?
    ) {
        if (totalCheckedDateList!!.contains(localDate!!)) {
            clContent?.setBackgroundResource(R.drawable.bg_red_border_conner6)
        } else {
            clContent?.setBackgroundResource(R.drawable.bg_calendar_line_white)
        }

        // 是否是今天
        var month =
            if (localDate.monthOfYear > 9) "${localDate.monthOfYear}" else "0${localDate.monthOfYear}"
        var day =
            if (localDate.dayOfMonth > 9) "${localDate.dayOfMonth}" else "0${localDate.dayOfMonth}"

        if (CalendarTimeUtil.getCurrDayString() == "${localDate.year}-$month-$day") {

            clContent?.setBackgroundResource(R.drawable.bg_red_conner6)
            // 选中的话覆盖之前设置的颜色
            tyDay?.setTextColor(context.resources.getColor(R.color.white))
            tvLunarDay?.setTextColor(context.resources.getColor(R.color.white))
        }
    }

    private fun setHoliday(tvLunarDay: TextView?, ivWorkStatus: TextView?, localDate: LocalDate?) {
        if (localDate == null) {
            return
        }
        var calendarDate = CalendarUtil.getCalendarDate(localDate)
        var holiday = ""
        if (!calendarDate.solarHoliday.isNullOrEmpty()) {
            holiday = calendarDate.solarHoliday
        } else if (!calendarDate.lunarHoliday.isNullOrEmpty()) {
            holiday = calendarDate.lunarHoliday
        } else if (!calendarDate.solarTerm.isNullOrEmpty()) {
            holiday = calendarDate.solarTerm
        }

        if (holiday.isNotEmpty()) {
            tvLunarDay?.let {
                it.text = holiday
                it.setTextColor(context.resources.getColor(R.color.color_1C9F48))
            }
        }
        yearByHoliday?.let {
            if (localDate.year == yearByHoliday.year) {
                val list = yearByHoliday.list
                for (item in list) {
                    val currentTime = localDate.toDate().time
                    if (currentTime >= item.startTime && currentTime <= item.endTime) {
                        ivWorkStatus?.visibility = View.VISIBLE
                    }
                }
            }
        }


    }

    private fun setSolar(tyDay: TextView?, localDate: LocalDate?) {
        if (localDate == null) {
            return
        }
        tyDay?.let {
            it.setTextColor(Color.GRAY)
            it.text = localDate!!.dayOfMonth.toString()
            if (localDate.dayOfWeek == 6 || localDate.dayOfWeek == 7) {
                it.setTextColor(context.resources.getColor(R.color.cFF2B18))
            } else {
                it.setTextColor(context.resources.getColor(R.color.color_black))
            }
        }
    }

    // 设置农历
    private fun setLunar(tvLunarDay: TextView?, localDate: LocalDate?) {
        if (localDate == null) {
            return
        }
        val calendarDate = CalendarUtil.getCalendarDate(localDate)
        tvLunarDay?.let {
            it.text = calendarDate.lunar.lunarOnDrawStr
            if (localDate.dayOfWeek == 6 || localDate.dayOfWeek == 7) {
                it.setTextColor(context.resources.getColor(R.color.cFF2B18))
            } else {
                it.setTextColor(context.resources.getColor(R.color.color_black))
            }
        }

    }
}