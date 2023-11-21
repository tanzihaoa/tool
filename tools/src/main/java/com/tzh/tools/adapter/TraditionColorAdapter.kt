package com.tzh.tools.adapter

import android.graphics.Color
import android.util.Log
import com.allen.library.shape.ShapeTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.TraditionColorBean

class TraditionColorAdapter(private val colorList: MutableList<TraditionColorBean>) :
    BaseQuickAdapter<TraditionColorBean, BaseViewHolder>(R.layout.item_tradition_color, colorList) {


    override fun convert(holder: BaseViewHolder, item: TraditionColorBean) {
        val view = holder.getView<ShapeTextView>(R.id.color_view)
        try {
            view.attributeSetData.solidColor = Color.parseColor(item.color)
        }catch (e : Exception){
            Log.e("TraditionColorAdapter==",""+item.color)
        }
        view.shapeBuilder?.init(view,view.attributeSetData)
        holder.setText(R.id.tv_name,item.name)
        holder.setText(R.id.tv_color,item.color)
    }
}