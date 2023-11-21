package com.tzh.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.TestSpeedModel

class TestSpeedAdapter(data: MutableList<TestSpeedModel>) :
    BaseQuickAdapter<TestSpeedModel, BaseViewHolder>(R.layout.item_test_speed, data = data) {
    override fun convert(holder: BaseViewHolder, item: TestSpeedModel) {
        holder.setImageResource(R.id.iv_ic, item.icon)
        holder.setText(R.id.tv_name, item.name)
        holder.setText(R.id.tv_speed, item.speed)
    }
}