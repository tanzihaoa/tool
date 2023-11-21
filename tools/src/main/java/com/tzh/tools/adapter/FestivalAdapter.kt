package com.tzh.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.UpcomingFestivals

/**
 * 将要来临的节日
 */
class FestivalAdapter(layout: Int, listData: MutableList<UpcomingFestivals>?) :
    BaseQuickAdapter<UpcomingFestivals, BaseViewHolder>(layout, listData) {
    override fun convert(holder: BaseViewHolder, item: UpcomingFestivals) {
        holder.setText(R.id.tv_day_of_month,item.dayOfMonth)
        holder.setText(R.id.tv_month,"${item.month}")
        holder.setText(R.id.tv_festival,item.festival)
        holder.setText(R.id.tv_data,item.data)
        holder.setText(R.id.tv_differ_day,item.differDay+"天")
    }
}