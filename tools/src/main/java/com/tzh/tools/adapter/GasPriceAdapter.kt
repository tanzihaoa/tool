package com.tzh.tools.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.GasBean

class GasPriceAdapter(val gasList: MutableList<GasBean>) :
    BaseQuickAdapter<GasBean, BaseViewHolder>(R.layout.item_gas_price, gasList) {


    override fun convert(holder: BaseViewHolder, item: GasBean) {

        if (gasList.indexOf(item) % 2 == 0) {
            holder.setBackgroundColor(R.id.ll_gas_bg, Color.parseColor("#FAFAFA"))
        }else{
            holder.setBackgroundColor(R.id.ll_gas_bg, Color.parseColor("#FFFFFF"))
        }

        holder.setText(R.id.tv_city, item.city)
        holder.setText(R.id.tv_92, item.`0h`)
        holder.setText(R.id.tv_95, item.`92h`)
        holder.setText(R.id.tv_98, item.`95h`)
        holder.setText(R.id.tv_oil, item.`98h`)
    }
}