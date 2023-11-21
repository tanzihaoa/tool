package com.tzh.tools.fragment

import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.net.apiLib
import com.tzh.tools.util.clickDelay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * 随机笑话组件
 */
class JokeFragment : BaseFragment<BaseViewModel<*>>() {
    private val mData = arrayListOf<String>()
    private var mPos = 0

    companion object {
        @JvmStatic
        fun newInstance(@LayoutRes layoutResID: Int?) =
            JokeFragment().apply {
                arguments = Bundle().apply {
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }

        @JvmStatic
        fun newInstance() = JokeFragment().apply {}
    }

    override fun initView() {
        val tv_joke_update = view?.findViewById<TextView>(R.id.tv_joke_update)
        val tv_joke_content = view?.findViewById<TextView>(R.id.tv_joke_content)

        launch(Dispatchers.IO) {
            runCatching {
                val params = HashMap<String, String>()
                params["postcode"] = "417700"
                apiLib.getJoke(params)
            }.onSuccess {
                if (it.code == 200) {
                    withContext(Dispatchers.Main) {
                        mData.clear()
                        it.data.list.forEach {
                            mData.add(it.content)
                        }
                        tv_joke_content?.text = mData[mPos]
                    }
                }
            }
        }

        tv_joke_update?.clickDelay {
            if (mData.isNotEmpty()) {
                mPos++
                if (mPos >= mData.size) {
                    mPos = 0
                }
                tv_joke_content?.text = mData[mPos]
            }
        }
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_joke
    }
}