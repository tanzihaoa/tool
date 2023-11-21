package com.tzh.tools.fragment

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.adapter.FestivalAdapter
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.extension.dp
import com.tzh.tools.model.UpcomingFestivals
import com.tzh.tools.net.apiLib
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author: Created by zfj
 * created on: 2023/5/16
 * description:
 */
class FestivalFragment : BaseFragment<BaseViewModel<*>>() {


    companion object {
        private const val ITEM_RES_ID = "ITEM_RES_ID"
        private const val TITLE = "TITLE"

        @JvmStatic
        fun newInstance(title: String, @LayoutRes layoutResID: Int?, @LayoutRes itemLayoutResID: Int?) =
            FestivalFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    itemLayoutResID?.let {
                        putInt(ITEM_RES_ID, itemLayoutResID)
                    }
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }

        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?, @LayoutRes itemLayoutResID: Int?) =
            FestivalFragment().apply {
                arguments = Bundle().apply {
                    itemLayoutResID?.let {
                        putInt(ITEM_RES_ID, itemLayoutResID)
                    }
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }

        @JvmStatic
        fun newInstance() = FestivalFragment()

        @JvmStatic
        fun newInstance(title: String) =
            FestivalFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                }
            }
    }

    override fun initView() {
        var tipsView = view?.findViewById<TextView>(R.id.tv_tips)
        val title = arguments?.getString(TITLE)
        title?.let {
            tipsView?.text = it
        }
        preLoadInComingFestival()
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_festival
    }

    private fun preLoadInComingFestival() {
        launch(Dispatchers.IO) {
            runCatching {
                apiLib.latelyFestival(hashMapOf())
            }.onSuccess {
                if (it.code == 200) {
                    val list = mutableListOf<UpcomingFestivals>()
                    val remoteList = it.data.list
                    if (remoteList != null) {
                        for (item in remoteList) {
                            var upcomingFestivals = UpcomingFestivals()
                            val calendar = Calendar.getInstance()
                            calendar.time = Date(item.longTime ?: 0)
                            upcomingFestivals.festival = item.name ?: ""
                            upcomingFestivals.month = (calendar.get(Calendar.MONTH) + 1).toString()
                            upcomingFestivals.dayOfMonth =
                                calendar.get(Calendar.DAY_OF_MONTH).toString()
                            upcomingFestivals.differDay = item.countDownDay.toString()
                            upcomingFestivals.data = item.date ?: ""
                            list.add(upcomingFestivals)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        var layoutResID = arguments?.getInt(ITEM_RES_ID) ?: 0
                        val mAdapter = FestivalAdapter(if (layoutResID != 0) layoutResID else R.layout.festival_child_item_layout, list)

                        var recyclerView = view?.findViewById<RecyclerView>(R.id.rcv_festival)
                        recyclerView?.addItemDecoration(
                            HorizontalDividerItemDecoration.Builder(requireContext())
                                .size(8.dp)
                                .color(Color.TRANSPARENT)
                                .build()
                        )
                        recyclerView?.adapter = mAdapter
                    }
                }

            }
        }
    }
}