package com.tzh.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.RateModel

class RateAdapter(data:MutableList<RateModel>):BaseQuickAdapter<RateModel,BaseViewHolder>(R.layout.item_rate,data=data) {
    override fun convert(holder: BaseViewHolder, item: RateModel) {
        holder.setText(R.id.str1,item.str1)
        holder.setText(R.id.str2,item.str2)
        holder.setText(R.id.str3,item.str3)
    }
}