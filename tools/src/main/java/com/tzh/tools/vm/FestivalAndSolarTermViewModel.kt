package com.tzh.tools.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tzh.tools.base.BaseRepository
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.extension.getStringColor
import com.tzh.tools.model.*
import com.tzh.tools.net.ToolsResult
import com.tzh.tools.net.apiLib
import com.tzh.tools.net.execute
import com.tzh.tools.util.LogUtil
import com.tzh.tools.util.MMKVUtil
import com.didichuxing.doraemonkit.util.ToastUtils
import com.google.gson.Gson
import com.nlf.calendar.Lunar
import com.nlf.calendar.util.SolarUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.gujun.android.span.span
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: Created by xyc
 * created on: 2022/7/19
 * description:
 */
class FestivalAndSolarTermViewModel : BaseViewModel<BaseRepository<*>>() {
    /**
     * 公众节日
     */
    private val _publicVacationModelLiveData = MutableLiveData<List<PublicVacationModel>>()
    val publicVacationModelLiveData = _publicVacationModelLiveData

    /**
     * 获取公众节日数据
     */
    fun doGetPublicVacationData() {
        viewModelScope.launch {
            val result = async(Dispatchers.IO) {
                // 今天日期
                val todayYearLunar = Lunar()
                // 获取所有节日
                val allFestival = mutableMapOf<String, String>()
                allFestival.putAll(SolarUtil.FESTIVAL)
                SolarUtil.OTHER_FESTIVAL.toList().forEach { pair ->
                    allFestival[pair.first] = pair.second.joinToString(",")
                }
                // 日期转时间戳
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val sortFestival = mutableMapOf<Long, String>()
                allFestival.toList().forEach { pair ->
                    val dateString = "${todayYearLunar.solar.year}-${pair.first}"
                    val date = simpleDateFormat.parse(dateString) ?: Date()
                    sortFestival[date.time] = pair.second
                }
                // 所有节日按时间戳重新排序
                val result = sortFestival.toList().sortedBy { (key, _) -> key }.toMap()
                // 组装数据
                val list = mutableListOf<PublicVacationChildModel>()
                result.toList().forEach { pair ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = pair.first
                    val lunar = Lunar(calendar.time)
                    val solar = lunar.solar
                    val diffDay = (calendar.timeInMillis - todayYearLunar.solar.calendar.timeInMillis) / 86400 / 1000
                    if (diffDay >= 0) {
                        list.add(
                            PublicVacationChildModel(
                                "${solar.month}月",
                                solar.day.toString(),
                                pair.second,
                                "${lunar.year}年${lunar.monthInChinese}月${lunar.dayInChinese}   周${lunar.weekInChinese}",
                                "${diffDay}天"
                            )
                        )
                    }
                }
                listOf(PublicVacationModel("${Lunar().solar.year}年", list))
            }
            _publicVacationModelLiveData.value = result.await()
        }
    }

    /**
     * 节气
     */
    private val _solarTermModelLiveData = MutableLiveData<List<SolarTermModel>>()
    val solarTermModelLiveData = _solarTermModelLiveData

    /**
     * 获取节气数据
     */
    fun doGetSolarTermData() {
        viewModelScope.launch {
            val result = async(Dispatchers.IO) {
                val list = mutableListOf<SolarTermModel>()
                val ignoreJieQiList = listOf("DA_XUE", "DONG_ZHI", "XIAO_HAN", "DA_HAN", "LI_CHUN")
                val lunar = Lunar()
                lunar.jieQiTable.toList().forEach {
                    val name = it.first
                    if (!ignoreJieQiList.contains(name)) {
                        val value = it.second
                        val isSelect = name.isNotEmpty() && lunar.jieQi.isNotEmpty() && (lunar.jieQi == name)
                        list.add(
                            SolarTermModel(
                                name,
                                "${value.year}年${value.month}月${value.day}日",
                                isSelect
                            )
                        )
                    }
                }
                list
            }
            _solarTermModelLiveData.value = result.await()
        }
    }

    /**
     * 节假日
     */
    private val _holidaysModelLiveData = MutableLiveData<List<HolidaysModel>>()
    val holidaysModelLiveData = _holidaysModelLiveData

    /**
     * 获取节假日数据
     */
    fun doGetHolidaysData() {
        viewModelScope.launch {
            val result = async(Dispatchers.IO) {
                val list = mutableListOf<HolidaysChildModel>()
                val result = getYearByHoliday()
                val simpleDateFormat = SimpleDateFormat("MM月dd日", Locale.getDefault())
                result.list.forEach { switchAfterHoliday ->
                    val span = span {
                        span("假期：${simpleDateFormat.format(switchAfterHoliday.startTime)}-${simpleDateFormat.format(switchAfterHoliday.endTime)}")
                        span("\t")
                        span("共${switchAfterHoliday.dayCount}天") {
                            textColor = getStringColor("#F30213")
                        }
                        span("\n")
                        span("调休：${switchAfterHoliday.tip}")
                    }
                    list.add(
                        HolidaysChildModel(
                            switchAfterHoliday.holiday,
                            switchAfterHoliday.startDayWeek,
                            switchAfterHoliday.name,
                            span.build()
                        )
                    )
                }
                HolidaysModel("${result.year}年", list)
            }
            _holidaysModelLiveData.value = listOf(result.await())
        }
    }

    /**
     * 获取节假日
     * @return YearByHoliday
     */
    private val HOLIDAY = "holiday"
    private suspend fun getYearByHoliday(): YearByHoliday {
        // 先尝试获取缓存
        val dataString = MMKVUtil.get(HOLIDAY, "") as String
        var yearByHoliday = Gson().fromJson(dataString, YearByHoliday::class.java)
        // 没有缓存
        if (null == yearByHoliday) {
            val result = withContext(Dispatchers.IO) {
                val params = hashMapOf<String, String>()
                params["year"] = Lunar().solar.year.toString()
                execute { apiLib.getYearHoliday(params) }
            }
            when (result) {
                is ToolsResult.Success -> {
                    val list = mutableListOf<SwitchAfterHoliday>()
                    for (item in result.data.list) {
                        val switchAfterHoliday = SwitchAfterHoliday()
                        switchAfterHoliday.name = item.name
                        switchAfterHoliday.startTime =
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).parse(item.startDay)?.time ?: 0L
                        switchAfterHoliday.endTime =
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).parse(item.endDay)?.time ?: 0L
                        switchAfterHoliday.holiday = item.holiday
                        switchAfterHoliday.tip = item.tip
                        switchAfterHoliday.dayCount = item.dayCount
                        switchAfterHoliday.startDayWeek = item.startDayWeek
                        list.add(switchAfterHoliday)
                    }
                    val newYearByHoliday = YearByHoliday()
                    newYearByHoliday.year = Calendar.getInstance().get(Calendar.YEAR)
                    newYearByHoliday.list = list
                    val stringData = Gson().toJson(newYearByHoliday)
                    MMKVUtil.save(HOLIDAY, stringData)
                    yearByHoliday = newYearByHoliday
                }
                is ToolsResult.Failure -> {
                    withContext(Dispatchers.Main) {
                        ToastUtils.showShort(result.errorMsg)
                    }
                }
                is ToolsResult.Error -> {
                    LogUtil.d("Error", result.t.message ?: "")
                    withContext(Dispatchers.Main) {
                        ToastUtils.showShort("数据出现点问题")
                    }
                }
                else -> {
                    ToastUtils.showShort("数据加载出现未知问题")
                }
            }
        }
        if (null == yearByHoliday) {
            return YearByHoliday().apply {
                year = Calendar.getInstance().get(Calendar.YEAR)
            }
        }
        return yearByHoliday
    }
}