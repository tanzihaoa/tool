package com.tzh.tools.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.PublicVacationChildModel

/**
 * @author: Created by xyc
 * created on: 2022/7/19
 * description: 公众节日子类适配器
 */
class PublicVacationChildAdapter :
    BaseQuickAdapter<PublicVacationChildModel, BaseViewHolder>(R.layout.item_public_vacation_child) {
    override fun convert(holder: BaseViewHolder, item: PublicVacationChildModel) {
        holder.apply {
            val tvMonth = getView<TextView>(R.id.tv_month)
            val tvDay = getView<TextView>(R.id.tv_day)
            val tvName = getView<TextView>(R.id.tv_name)
            val tvDate = getView<TextView>(R.id.tv_date)
            val tvAFewDays = getView<TextView>(R.id.tv_a_few_days)
            tvMonth.text = item.month
            tvDay.text = item.day
            tvName.text = item.name
            tvDate.text = item.lunar
            tvAFewDays.text = item.aFewDay
        }
    }
}