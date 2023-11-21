package com.tzh.tools.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tzh.tools.util.getClazz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2019-02-15
 */
abstract class BaseFragment<VM : BaseViewModel<*>> : Fragment(),
    CoroutineScope by MainScope() {

    protected lateinit var mViewModel: VM

    /**
     * 初始化变量
     */
    protected open fun initVar() {}

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    /**
     * 初始化LiveData数据观察者
     */
    protected abstract fun initDataObserver()

    /**
     * 加载数据
     */
    protected open fun loadData() {}

    /**
     * 引入布局
     */
    protected abstract fun getLayoutId(): Int

    private  var view: View?=null
    companion object{
        const val KEY_LAYOUT ="layoutResID"
        const val KEY_ACTIVITY_LAYOUT ="activityLayoutResID"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments?.getInt(KEY_LAYOUT, 0)
        view = if (layoutId != null && layoutId != 0) {
            LayoutInflater.from(requireContext()).inflate(layoutId, null)
        } else {
            LayoutInflater.from(requireContext()).inflate(getLayoutId(), null)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(getClazz(this))


    }

    private var isLoad = true
    override fun onResume() {
        super.onResume()
        if (isLoad) {
            isLoad = false
            initVar()
            initView()
            initDataObserver()
            loadData()

            if (regEvent() && !EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
    }

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    open fun regEvent(): Boolean {
        return false
    }

    override fun onDestroy() {
        if (regEvent()) {
            EventBus.getDefault().unregister(this)
        }
        cancel()
        super.onDestroy()
    }
}
