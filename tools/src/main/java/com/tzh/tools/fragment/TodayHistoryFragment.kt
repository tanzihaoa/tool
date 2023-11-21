package com.tzh.tools.fragment


import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.adapter.TodayInHistoryAdapter
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.FragmentTodayHistoryBinding
import com.tzh.tools.model.TodayInHistoryData
import com.tzh.tools.net.apiLib
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


/**
 * @author: Created by zfj
 * created on: 2023/4/25
 * description: 历史今天
 */
class TodayHistoryFragment : BaseFragment<BaseViewModel<*>>() {

    companion object {
        private const val STYLE = "STYLE"
        private const val ITEM_RES_ID = "ITEM_RES_ID"

        @JvmStatic
        fun newInstance(type: Int,@LayoutRes layoutResID: Int?) =
            TodayHistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(STYLE, type)
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }

        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?, @LayoutRes itemLayoutResID: Int?) =
            TodayHistoryFragment().apply {
                arguments = Bundle().apply {
                    itemLayoutResID?.let {
                        putInt(ITEM_RES_ID, itemLayoutResID)
                    }
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }
    }

    override fun initView() {
        getTodayInHistory()
    }

    private fun getTodayInHistory() {
        var calendar = Calendar.getInstance()

        var month =
            "${(calendar.get(Calendar.MONTH) + 1)}"
        var day = "${calendar.get(Calendar.DAY_OF_MONTH)}"
        launch(Dispatchers.IO) {
            runCatching {
                val params = HashMap<String, String>()
                params["month"] = month
                params["day"] = day
                apiLib.todayInHistory(params)
            }.onSuccess {
                if (it.code == 200) {
                    withContext(Dispatchers.Main) {
                        view?.findViewById<RecyclerView>(R.id.recycleView)?.adapter =
                            TodayInHistoryAdapter(mutableListOf(TodayInHistoryData("${month}月${day}日", it.data)), getLayout())
                    }
                }
            }
        }
    }

    private fun getLayout(): Int {
        var layoutResID = arguments?.getInt(ITEM_RES_ID) ?: 0
        return if(layoutResID!=0){
            layoutResID
        }else{
            when (arguments?.getInt(STYLE) ?: 0) {
                2 -> {
                    R.layout.item_history_today_style2_layout
                }
                else -> {
                    R.layout.item_history_today_layout
                }
            }
        }
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_today_history
    }

}