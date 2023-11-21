package com.tzh.tools.fragment

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.vm.CalendarFragmentViewModel

class CalendarFragment : BaseFragment<CalendarFragmentViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }

        @JvmStatic
        fun newInstance() = CalendarFragment().apply {}
    }

    override fun initView() {
        mViewModel.getYearHoliday()
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_calendar
    }
}