package com.tzh.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.ScanningInternetModel

class ScanningInternetAdapter(data: MutableList<ScanningInternetModel>) :
    BaseQuickAdapter<ScanningInternetModel, BaseViewHolder>(
        R.layout.item_scanning_internet,
        data = data
    ) {
    override fun convert(holder: BaseViewHolder, item: ScanningInternetModel) {
        holder.setImageResource(R.id.iv_ic, item.icon)
        holder.setText(R.id.tv_name, item.name)
        holder.setText(R.id.tv_ip, item.ip)
        holder.setText(R.id.tv_devices_type, item.devicesType)
    }
}