package com.tzh.tools.model

import androidx.annotation.Keep
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

@Keep
class CalendarItemData<T>(data: T) : MultiItemEntity {
    var position = 0
    var data: T = data

    override val itemType: Int
        get() {
            var type = 0
            when (position) {
                0 -> {
                    type = Calendar
                }
                1, 3, 5, 7, 9, 11 -> {
                    type = Ad
                }
                2 -> {
                    type = Constellation
                }
                4 -> {
                    type = GoodIllLuck
                }
                6 -> {
                    type = Festival
                }
                8 -> {
                    type = TrafficRestriction
                }
                10 -> {
                    type = SpiritualSwab
                }
                12 -> {
                    type = HistoryToday
                }
            }
            return type
        }


    companion object {
        const val Calendar = 0 // 日历
        const val Ad = 1 // 广告
        const val Welfare = 3 // 福利
        const val Tool = 4 // 工具
        const val Constellation = 5 // 星座
        const val GoodIllLuck = 6 // 吉凶运势
        const val Festival = 7 //节日
        const val TrafficRestriction = 8 // 限行
        const val SpiritualSwab = 9 // 灵签
        const val HistoryToday = 10 // 历史今天
    }

}

@Keep
data class CommonItemData<T>(var title: String = "0", var list: List<T> = mutableListOf())

@Keep
data class WelfareData(var name: String = "", var icon: String = "", var url: String = "")

/**
 * fortuneLevel 综合运势
 * paired_constellations  速配星座
 * lucky_number 幸运数字
 * lucky_color 颜色
 *
 */
@Keep
data class ConstellationData(
    var fortuneLevel: Int = 0, var paired_constellations: String = "", var lucky_number: String = "",
    var lucky_color: String = "", var content: String = "", var index: Int = 0
)


@Keep
data class CalendarHoliday(var load: Boolean)