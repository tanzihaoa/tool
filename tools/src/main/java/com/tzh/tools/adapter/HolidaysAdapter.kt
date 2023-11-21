package com.tzh.tools.adapter

import android.graphics.Color
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.extension.dp
import com.tzh.tools.model.HolidaysModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

/**
 * @author: Created by xyc
 * created on: 2022/7/19
 * description: 节假日适配器
 */
class HolidaysAdapter : BaseQuickAdapter<HolidaysModel, BaseViewHolder>(R.layout.item_holidays) {
    override fun convert(holder: BaseViewHolder, item: HolidaysModel) {
        holder.apply {
            val tvYear = getView<TextView>(R.id.tv_year)
            val recycleView = getView<RecyclerView>(R.id.recycle_view)
            tvYear.text = item.year
            recycleView.apply {
                if (null == layoutManager) {
                    isNestedScrollingEnabled = false
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(
                        HorizontalDividerItemDecoration.Builder(context)
                            .size(1.dp)
                            .margin(12.dp)
                            .color(Color.parseColor("#EEEEEE"))
                            .build()
                    )
                    adapter = HolidaysChildAdapter()
                }
            }
            (recycleView.adapter as? HolidaysChildAdapter)?.setList(item.list)
        }
    }
}