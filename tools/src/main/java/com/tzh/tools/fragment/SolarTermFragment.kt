package com.tzh.tools.fragment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tzh.tools.R
import com.tzh.tools.adapter.SolarTermAdapter
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.extension.dp
import com.tzh.tools.extension.getStringColor
import com.tzh.tools.view.GridDividerItemDecoration
import com.tzh.tools.vm.FestivalAndSolarTermViewModel

/**
 * @author: Created by xyc
 * created on: 2022/7/19
 * description: 节气
 */
class SolarTermFragment : BaseFragment<FestivalAndSolarTermViewModel>() {

    companion object {
        fun newInstance() = SolarTermFragment().apply {
            arguments = Bundle()
        }
    }

    override fun getLayoutId() = R.layout.fragment_festival_solar_term

    private val solarTermAdapter by lazy { SolarTermAdapter() }

    private lateinit var recycleView: RecyclerView

    override fun initView() {
        recycleView = requireView().findViewById(R.id.recycle_view)
        recycleView.apply {
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(
                com.tzh.tools.view.GridDividerItemDecoration(
                    1.dp,
                    1.dp,
                    getStringColor("#EEEEEE")
                )
            )
            adapter = solarTermAdapter
        }
    }

    override fun initDataObserver() {
        mViewModel.solarTermModelLiveData.observe(this) {
            solarTermAdapter.setList(it)
        }
    }

    override fun loadData() {
        mViewModel.doGetSolarTermData()
    }
}