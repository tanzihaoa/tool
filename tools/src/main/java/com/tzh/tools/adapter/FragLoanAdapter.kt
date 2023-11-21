package com.tzh.tools.adapter

import android.graphics.Color
import androidx.annotation.Keep
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R

class FragLoanAdapter(data: MutableList<LoanModel>) : BaseQuickAdapter<LoanModel, BaseViewHolder>(R.layout.item_frag_loan, data = data) {
    override fun convert(holder: BaseViewHolder, item: LoanModel) {
        holder.setText(R.id.il_str1, "第${item.str1}个月")
        holder.setText(R.id.il_str2, item.str2)
        holder.setText(R.id.il_str3, item.str3)
        holder.setText(R.id.il_str4, item.str4)

        if (item.str1.toInt() % 2 != 0) {
            holder.setBackgroundColor(R.id.il_bg, Color.parseColor("#ffffff"))
        }
    }
}

@Keep
data class LoanModel (val str1:String,val str2 :String,val str3:String,val str4:String,val str5:String)