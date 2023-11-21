package com.tzh.tools.fragment

import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.MMKVUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * 倒计时组件
 */
class CountdownFragment : BaseFragment<BaseViewModel<*>>() {
    companion object {
        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?) =
            CountdownFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }

        @JvmStatic
        fun newInstance() = CountdownFragment().apply {}
    }

    private val KEY_FIRST_USE_APP_DATE = "first_use_app_Date"
    private val day = 24 * 60 * 60 * 1000L

    override fun initView() {
        val curTime = System.currentTimeMillis()

        var firstUseAppDate = MMKVUtil.get(KEY_FIRST_USE_APP_DATE, 0L) as Long
        if (firstUseAppDate == 0L) {
            firstUseAppDate = curTime
            MMKVUtil.save(KEY_FIRST_USE_APP_DATE, firstUseAppDate)
        }

        val calendarWeek = Calendar.getInstance()
        calendarWeek.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        val weekend = calendarWeek.time.time

        val calendarYear = Calendar.getInstance()
        calendarYear.add(Calendar.YEAR, 1)
        calendarYear.set(Calendar.DAY_OF_YEAR, calendarYear.getActualMinimum(Calendar.DAY_OF_YEAR))
        val year = calendarYear.time.time

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        view?.findViewById<TextView>(R.id.tvCountdownDate1)?.text =
            simpleDateFormat.format(firstUseAppDate)
        view?.findViewById<TextView>(R.id.tvCountdownDate2)?.text = simpleDateFormat.format(year)
        view?.findViewById<TextView>(R.id.tvCountdownDate3)?.text = simpleDateFormat.format(weekend)
        view?.findViewById<TextView>(R.id.tvCountdownDay1)?.text =
            "${(curTime - firstUseAppDate) / day}"
        view?.findViewById<TextView>(R.id.tvCountdownDay2)?.text = "${(year - curTime) / day}"
        view?.findViewById<TextView>(R.id.tvCountdownDay3)?.text = "${(weekend - curTime) / day}"
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_countdown
    }
}