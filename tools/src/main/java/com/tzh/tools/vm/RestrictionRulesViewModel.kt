package com.tzh.tools.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.tzh.tools.base.BaseRepository
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.model.RestrictionRulesModel
import com.nlf.calendar.Lunar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: Created by xyc
 * created on: 2022/7/26
 * description:限行规则
 */
class RestrictionRulesViewModel : BaseViewModel<BaseRepository<*>>() {
    //日期格式化
    private val simpleDateFormat1 = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private val simpleDateFormat2 = SimpleDateFormat("MM月dd日", Locale.getDefault())

    /**
     * 限行规则
     */
    private val _sestrictionRulesLiveData: MutableLiveData<RestrictionRulesModel> = MutableLiveData()
    val sestrictionRulesLiveData: LiveData<RestrictionRulesModel> = _sestrictionRulesLiveData

    /**
     * 获取城市限行数趣
     * @param city String 城市
     */
    fun doGetData(city: String) {
        viewModelScope.launch {
            val result = async(Dispatchers.IO) {
                val lunar = Lunar()
                val lunar1 = lunar.next(1)
                val lunar2 = lunar.next(2)
                val lunar3 = lunar.next(3)
                val solar = lunar.solar
                RestrictionRulesModel(
                    solar.day.toString(),
                    "— ${simpleDateFormat1.format(lunar.solar.calendar.time.time)} —",
                    "不限行",
                    simpleDateFormat2.format(lunar1.solar.calendar.time.time),
                    "不限行",
                    simpleDateFormat2.format(lunar2.solar.calendar.time.time),
                    "不限行",
                    simpleDateFormat2.format(lunar3.solar.calendar.time.time),
                    "中华人民共和国和以色列国建交联合公报是中华人民共和国和以色列国建交联合公报是中华人民共和国和以色列国建交联合公报是中华人民共和国和以色列国建交联合公报是中华人民共和国和以色列国建交联合公报是中华人民共和国和以色列国建交联合公报是"
                )
            }
            _sestrictionRulesLiveData.value = result.await()
        }
    }

}


