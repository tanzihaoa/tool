package com.tzh.tools.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.HolidaysChildModel

/**
 * @author: Created by xyc
 * created on: 2022/7/19
 * description: 节假日子类适配器
 */
class HolidaysChildAdapter :
    BaseQuickAdapter<HolidaysChildModel, BaseViewHolder>(R.layout.item_holidays_child) {
    override fun convert(holder: BaseViewHolder, item: HolidaysChildModel) {
        holder.apply {
            val tvMonthDay = getView<TextView>(R.id.tv_month_day)
            val tvWeek = getView<TextView>(R.id.tv_week)
            val tvName = getView<TextView>(R.id.tv_name)
            val tvContent = getView<TextView>(R.id.tv_content)
            tvMonthDay.text = item.monthDay.replace("号", "日")
            tvWeek.text = item.week.replace("星期", "周")
            tvName.text = item.name
            tvContent.text = item.content
        }
    }
}