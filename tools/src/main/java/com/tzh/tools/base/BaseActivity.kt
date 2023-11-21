package com.tzh.tools.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.*
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.tzh.tools.util.MMKVUtil
import com.tzh.tools.util.TimeUtil
import com.tzh.tools.util.getClazz
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2018-10-26
 */
@SuppressLint("Registered")
abstract class BaseActivity<VM : BaseViewModel<*>, DB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope by MainScope() {
    protected lateinit var mViewModel: VM
    protected lateinit var mDataBinding: DB
    private var isSecondActivity: Boolean = false
    private var mStartActivityTag: String? = null
    private var mStartActivityTime: Long = 0

    companion object {
        const val KEY_LAYOUT = "layoutResID"
    }

    protected val mHandler by lazy {
        Handler(Looper.getMainLooper()) {
            return@Handler handlerMsg(it)
        }
    }

    // 当前显示日期
    var lastResumeDate = TimeUtil.getCurrDayString()


    protected open fun init() {
        initVar()
        initView()
    }

    private fun isDevelop(): Boolean {
        return false
    }


    override fun getResources(): Resources {
        val res = super.getResources()
        val configuration: Configuration = res.configuration
        if (configuration.fontScale - 1.0f > 1e-5) {
            configuration.fontScale = 1.0f
            res.updateConfiguration(configuration, res.displayMetrics)
        }
        return res
    }

    /**
     * Handler消息返回处理
     */
    protected open fun handlerMsg(msg: Message): Boolean {
        return false
    }

    /**
     * 初始化变量
     */
    protected open fun initVar() {}

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    /**
     * 加载数据
     */
    protected open fun loadData() {}

    /**
     * 初始化LiveData数据观察者
     */
    protected abstract fun initDataObserver()

    protected abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutId = intent.getIntExtra(KEY_LAYOUT, 0)

        setContentView(if (layoutId != 0) layoutId else getLayoutId())

        ImmersionBar.with(this)
            .transparentStatusBar()
            .init()
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mDataBinding.lifecycleOwner = this
        mViewModel = ViewModelProvider(this).get(getClazz(this))

        init()
        initDataObserver()
        loadData()

        if (regEvent() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    open fun regEvent(): Boolean {
        return false
    }

    open fun isSecondActivity(
        secondActivity: Boolean
    ) {
        isSecondActivity = secondActivity
    }

    override fun onResume() {
        super.onResume()
        if (isSecondActivity) {
            showInsertAd()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        overrideFontScale(newBase)
    }

    /**
     * 重置配置 fontScale：保持字体比例不变，始终为 1.
     */
    private fun overrideFontScale(context: Context?) {
        if (context == null) return
        context.resources.configuration.let {
            it.fontScale = 1f // 保持字体比例不变，始终为 1.
            applyOverrideConfiguration(it) // 应用新的配置
        }
    }

    open fun showInsertAd() {}

    /**
     * 防 Activity 多重跳转
     */
    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode, options)
        }
    }

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent 用于跳转的 Intent 对象
     * @return 检查通过返回true, 检查不通过返回false
     */
    protected fun startActivitySelfCheck(intent: Intent): Boolean {
        // 默认检查通过
        var result = true
        // 标记对象
        val tag: String
        when {
            intent.component != null -> {
                // 显式跳转
                tag = intent.component!!.className
            }
            intent.action != null -> {
                // 隐式跳转
                tag = intent.action!!
            }
            else -> {
                // 其他方式
                return true
            }
        }

        if (tag == mStartActivityTag && mStartActivityTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false
        }

        mStartActivityTag = tag
        mStartActivityTime = SystemClock.uptimeMillis()
        return result
    }

    override fun onDestroy() {
        if (regEvent() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        //取消当前Activity的协程
        cancel()
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    fun showToast(text: String) {
        mHandler.post {
            ToastUtils.show(text)
        }
    }

    fun isTodayVideoChecked(key: String): Boolean {
        return TimeUtil.isToday(MMKVUtil.get(key, 0L) as Long)
    }

}