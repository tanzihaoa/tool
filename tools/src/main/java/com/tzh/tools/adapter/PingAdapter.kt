package com.tzh.tools.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tzh.tools.R
import com.tzh.tools.model.NetWorkPingBean

class PingAdapter(data: MutableList<NetWorkPingBean>) :
    BaseQuickAdapter<NetWorkPingBean, BaseViewHolder>(R.layout.item_ping, data = data) {
    override fun convert(holder: BaseViewHolder, item: NetWorkPingBean) {
        val netDelay = item.netDelay
        holder.getView<TextView>(R.id.tv_item_ping).text = if(netDelay > 80){"80+"}else{netDelay.toString()}
        val view = holder.getView<View>(R.id.vi_item_ping)
        val itemHig = context.resources.getDimension(R.dimen.ping_item_hig).toInt()
        val layoutH = itemHig/80
        val lp = view.layoutParams
        lp.height = if(netDelay >= 80){layoutH*80}else{(netDelay*layoutH).toInt()}
        view.layoutParams = lp
    }
}