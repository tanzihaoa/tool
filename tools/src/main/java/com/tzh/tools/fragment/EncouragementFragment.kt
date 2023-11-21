package com.tzh.tools.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import java.util.Random
import kotlin.math.min

/**
 * 励志名言
 */
class EncouragementFragment : BaseFragment<BaseViewModel<*>>() {
    companion object {
        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?) =
            EncouragementFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }

        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?,@LayoutRes activityLayoutResID: Int?) =
            EncouragementFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                    activityLayoutResID?.let {
                        putInt(KEY_ACTIVITY_LAYOUT, activityLayoutResID)
                    }
                }
            }

    }

    var listData = ArrayList<String>().apply {
        add("时间是公平的，它不会给任何人多一分，也不会给任何人少一秒；但时间也是有偏向的，惜时如金者往往会得到时间的奖励，虚掷光阴者则会徒留怅然。")
        add("选择是一时的人生，但人生是永恒的选择，关键是，为了什么去一往无前，如何才能锲而不舍。")
        add("再微小的光也是光，可以说再平凡的人也都有他们人生当中那些高光时刻。")
        add("路上，有风有雨是常态，风雨无阻是心态，风雨兼程是状态。")
        add("在这个美好又遗憾的世界里，你我皆是自远方而来的独行者，不断行走，不顾一切，哭着，笑着，留恋人间，只为不虚此行。")
        add("人生漫长，晴雨交加，但若是心怀热爱，即使岁月荒芜，亦能奔山赴海，静待一树花开。")
        add("命运，不会偏袒任何人，却会眷顾一直朝着光亮前行的人。")
        add("永远不要像任何人解释你自己。因为喜欢你的人不需要，不喜欢你的人不会相信。")
        add("你的假装努力，欺骗的只有你自己，永远不要用战术上的勤奋，来掩饰战略上的懒惰。")
        add("有志者、事竟成，破釜沉舟，百二秦关终属楚；苦心人、天不负，卧薪尝胆，三千越甲可吞吴。")
    }


    fun getBody():String{
        val i = Random().nextInt(listData.size)
        return listData[min(i,listData.size-1)]
    }

    private var mBody:TextView? = null

    override fun initView() {
        view?.findViewById<View>(R.id.but_refresh_encouragement)?.setOnClickListener {
            mBody?.text = getBody()
        }
        mBody = view?.findViewById(R.id.but_refresh_body)
        mBody?.text = getBody()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_lib_encouragement
    }
    override fun initDataObserver() {}
}