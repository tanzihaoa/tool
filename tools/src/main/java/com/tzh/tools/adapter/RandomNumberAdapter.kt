package com.tzh.tools.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R

class RandomNumberAdapter:
    BaseQuickAdapter<Long, BaseViewHolder>(R.layout.item_random_number, data = mutableListOf()) {
    override fun convert(holder: BaseViewHolder, item: Long) {
        holder.getView<TextView>(R.id.tv_random_number).text = item.toString()
    }

}