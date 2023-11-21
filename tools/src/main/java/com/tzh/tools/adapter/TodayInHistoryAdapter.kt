package com.tzh.tools.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.TodayInHistoryData


class TodayInHistoryAdapter(mList: MutableList<TodayInHistoryData>,@LayoutRes private val layoutResId: Int) :
    BaseQuickAdapter<TodayInHistoryData, BaseViewHolder>(
        layoutResId,
        mList
    ) {
    override fun convert(holder: BaseViewHolder, item: TodayInHistoryData) {
        holder.setText(R.id.tv_history_today_date, item.dayMonth)
        if (item.list.size > 1) {
            var todayInHistoryBean1 = item.list[1]
            var todayInHistoryBean0 = item.list[0]
            holder.setText(
                R.id.tv_history_today_data1_year,
                todayInHistoryBean0.date + "年 "
            )
            holder.setText(
                R.id.tv_history_today_data1, todayInHistoryBean0.title
            )
            holder.setText(
                R.id.tv_history_today_data2_year,
                todayInHistoryBean1.date + "年 "
            )
            holder.setText(
                R.id.tv_history_today_data2,
                todayInHistoryBean1.title
            )

            holder.setVisible(R.id.shapeConstraintLayout, true)
        } else if (item.list.size == 1) {
            var todayInHistoryBean0 = item.list[0]
            holder.setText(
                R.id.tv_history_today_data1_year,
                todayInHistoryBean0.date + "年 "
            )
            holder.setText(
                R.id.tv_history_today_data1, todayInHistoryBean0.title
            )
            holder.setVisible(R.id.shapeConstraintLayout, false)
        } else {
            holder.setVisible(R.id.shapeConstraintLayout, false)
        }
    }
}