package com.tzh.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.MoneyModel


class CurrencyAdapter(data:MutableList<MoneyModel>): BaseQuickAdapter<MoneyModel, BaseViewHolder>(R.layout.item_data_currency, data = data) {
    override fun convert(holder: BaseViewHolder, item: MoneyModel) {
        holder.setImageResource(R.id.item_currency_iv,item.icon)
        holder.setText(R.id.item_currency_tv,item.name+" "+item.sName)
    }
}