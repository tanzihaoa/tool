package com.tzh.tools.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.LoanModel

class LoanAdapter(data: MutableList<LoanModel>) : BaseQuickAdapter<LoanModel, BaseViewHolder>(R.layout.item_loan, data = data) {
    @SuppressLint("ResourceAsColor")
    override fun convert(holder: BaseViewHolder, item: LoanModel) {
        holder.setText(R.id.il_str1, item.str1)
        holder.setText(R.id.il_str2, item.str2)
        holder.setText(R.id.il_str3, item.str3)
        holder.setText(R.id.il_str4, item.str4)
        holder.setText(R.id.il_str5, item.str5)

        if (holder.layoutPosition != 0 && item.str1.toInt() % 2 != 0) {
            holder.setBackgroundColor(R.id.il_bg, Color.parseColor("#ffffff"))
        }
    }
}