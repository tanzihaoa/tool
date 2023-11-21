package com.tzh.tools.adapter

import android.graphics.Color
import android.widget.TextView
import com.allen.library.shape.ShapeLinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.SolarTermModel

/**
 * @author: Created by xyc
 * created on: 2022/7/19
 * description: 节气适配器
 */
class SolarTermAdapter :
    BaseQuickAdapter<SolarTermModel, BaseViewHolder>(R.layout.item_solar_term) {
    override fun convert(holder: BaseViewHolder, item: SolarTermModel) {
        holder.apply {
            val tvSolarTerm = getView<TextView>(R.id.tv_solar_term)
            val tvDate = getView<TextView>(R.id.tv_date)
            val llContent = getView<ShapeLinearLayout>(R.id.ll_content)
            tvSolarTerm.text = item.solarTerm
            tvDate.text = item.date
            llContent.shapeBuilder
                ?.setShapeSolidColor(
                    if (item.isSelect) {
                        Color.parseColor("#EEEEEE")
                    } else {
                        Color.parseColor("#FFFFFF")
                    }
                )
                ?.into(llContent)
        }
    }
}