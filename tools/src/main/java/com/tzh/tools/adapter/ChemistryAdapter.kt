package com.tzh.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.FormulaPhysicsBean

/**
 * Description:
 * @Author: LYL
 * @CreateDate: 2023/7/13 16:15
 */
class ChemistryAdapter :
    BaseQuickAdapter<FormulaPhysicsBean, BaseViewHolder>(R.layout.item_chemistry_formula) {

    override fun convert(holder: BaseViewHolder, item: FormulaPhysicsBean) {
        holder.setText(R.id.tv_formula_name, item.name)
        holder.setText(R.id.tv_hide_formula, item.formula)
        holder.setText(R.id.tv_hide_notes, item.notes)
    }

}