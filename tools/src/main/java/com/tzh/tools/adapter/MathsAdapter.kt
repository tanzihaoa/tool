package com.tzh.tools.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.FormulaMathsBean

/**
 * Description:
 * @Author: LYL
 * @CreateDate: 2023/7/13 16:15
 */
class MathsAdapter :
    BaseQuickAdapter<FormulaMathsBean, BaseViewHolder>(R.layout.item_maths_formula) {

    override fun convert(holder: BaseViewHolder, item: FormulaMathsBean) {
        holder.setText(R.id.tv_formula_name, item.name)

        holder.setText(R.id.tv_hide_notes, item.notes)

        val recycleView = holder.getView<RecyclerView>(R.id.rv_maths_son)

        recycleView.apply {
            if (null == layoutManager) {
                isNestedScrollingEnabled = false
                layoutManager = GridLayoutManager(context, 2)
                adapter = MathsSonAdapter()
            }
        }
        (recycleView.adapter as? MathsSonAdapter)?.setList(item.formulaList)
    }

}