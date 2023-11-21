package com.tzh.tools.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.adapter.HolidaysAdapter
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.extension.dp
import com.tzh.tools.extension.getStringColor
import com.tzh.tools.vm.FestivalAndSolarTermViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

/**
 * @author: Created by xyc
 * created on: 2022/7/19
 * description: 节假日
 */
class HolidaysFragment : BaseFragment<FestivalAndSolarTermViewModel>() {

    companion object {
        fun newInstance() = HolidaysFragment().apply {
            arguments = Bundle()
        }
    }

    override fun getLayoutId() = R.layout.fragment_festival_solar_term

    private val holidaysAdapter by lazy { HolidaysAdapter() }

    private lateinit var recycleView: RecyclerView

    override fun initView() {
        recycleView = requireView().findViewById(R.id.recycle_view)
        recycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(requireContext())
                    .size(12.dp)
                    .color(getStringColor("#F5F5F8"))
                    .build()
            )
            adapter = holidaysAdapter
        }
    }

    override fun initDataObserver() {
        mViewModel.holidaysModelLiveData.observe(this) {
            holidaysAdapter.setList(it)
        }
    }

    override fun loadData() {
        mViewModel.doGetHolidaysData()
    }
}