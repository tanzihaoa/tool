package com.tzh.tools.vm

import androidx.lifecycle.MutableLiveData
import com.tzh.tools.R
import com.tzh.tools.base.BaseRepository
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.constants.MMKVConstants
import com.tzh.tools.model.MoneyModel
import com.tzh.tools.model.RateBean
import com.tzh.tools.model.SwitchAfterHoliday
import com.tzh.tools.model.YearByHoliday
import com.tzh.tools.net.ToolsResult
import com.tzh.tools.net.apiLib
import com.tzh.tools.net.execute
import com.tzh.tools.util.CalendarTimeUtil
import com.tzh.tools.util.MMKVUtil
import com.google.gson.Gson
import java.util.*


class CalendarFragmentViewModel : BaseViewModel<BaseRepository<*>>() {
    var refreshHoliday: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 获取节假日
     */
    fun getYearHoliday() {
        launch({
            val params = HashMap<String, String>()
            params.put("year", Calendar.getInstance().get(Calendar.YEAR).toString())
            execute { apiLib.getYearHoliday(params) }
        }, {
            if (it is ToolsResult.Success) {
                val list = mutableListOf<SwitchAfterHoliday>()
                for (item in it.data.list) {
                    val switchAfterHoliday = SwitchAfterHoliday()
                    switchAfterHoliday.name = item.name
                    switchAfterHoliday.startTime =
                        CalendarTimeUtil.stringFormatDetailToDate(item.startDay, "yyyy-MM-dd").time
                    switchAfterHoliday.endTime =
                        CalendarTimeUtil.stringFormatDetailToDate(item.endDay, "yyyy-MM-dd").time
                    switchAfterHoliday.holiday = item.holiday
                    switchAfterHoliday.tip = item.tip
                    switchAfterHoliday.dayCount = item.dayCount
                    switchAfterHoliday.startDayWeek = item.startDayWeek

                    list.add(switchAfterHoliday)
                }
                val yearByHoliday = YearByHoliday()
                yearByHoliday.year = Calendar.getInstance().get(Calendar.YEAR)
                yearByHoliday.list = list
                val stringData = Gson().toJson(yearByHoliday)
                MMKVUtil.save(MMKVConstants.HOLIDAY, stringData)
                refreshHoliday.value = true
            } else {
                refreshHoliday.value = true
            }
        })
    }
}
