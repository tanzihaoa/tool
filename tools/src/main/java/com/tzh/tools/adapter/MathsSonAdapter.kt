package com.tzh.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R

/**
 * Description:
 * @Author: LYL
 * @CreateDate: 2023/7/13 16:15
 */
class MathsSonAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_maths_son) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_hide_formula, item)
    }

}