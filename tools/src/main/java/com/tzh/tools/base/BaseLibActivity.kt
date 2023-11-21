package com.tzh.tools.base

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2018-10-26
 */
abstract class BaseLibActivity<VM : BaseViewModel<*>> : AppCompatActivity(),
    CoroutineScope by MainScope() {
    //    protected val adBridge by lazy { SQAdBridge(this) }
    var darkFront = false

    companion object {
        const val KEY_LAYOUT = "layoutResID"
        const val KEY_DARK = "darkID"
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val configuration: Configuration = res.configuration
        if (configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f
            res.updateConfiguration(configuration, res.displayMetrics)
        }
        return res
    }


    override fun onResume() {
        super.onResume()
    }

    protected lateinit var mViewModel: VM

    private var mStartActivityTag: String? = null
    private var mStartActivityTime: Long = 0


    protected val mHandler by lazy {
        Handler(Looper.getMainLooper()) {
            return@Handler handleMessage(it)
        }
    }


    protected open fun init() {
        initVar()
        initView()
    }

    /**
     * 初始化变量
     */
    protected open fun initVar() {}

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    protected open fun handleMessage(msg: Message): Boolean {
        return false
    }

    /**
     * 加载数据
     */
    protected open fun loadData() {}

    /**
     * 初始化LiveData数据观察者
     */
    protected abstract fun initDataObserver()

    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModel(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImmersionBar.with(this)
            .transparentStatusBar()
            .autoStatusBarDarkModeEnable(true, 0.2f)
            .init()

        val layoutId = intent.getIntExtra(KEY_LAYOUT, 0)
        darkFront = intent.getBooleanExtra(KEY_DARK, false)

        setContentView(if (layoutId != 0) layoutId else getLayoutId())

        mViewModel = ViewModelProvider(this)[getViewModel()]

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

//    fun checkWifiLocPermission(): Boolean {
//        return NetworkUtil.isWifiData() && LocationUtil.isGpsEnabled() &&
//                PermissionX.isGranted(baseContext, Manifest.permission.ACCESS_FINE_LOCATION)
//    }
//
//    interface RequestPermissionCallback {
//        fun onAllGrant()
//    }
//
//    /**
//     * 请求定位权限
//     * @param forceRequest 是否强制请求：禁止获取权限后跳转设置
//     * @param callback 请求成功的回调
//     */
//    fun requestLocPermission(forceRequest: Boolean = false, callback: RequestPermissionCallback?) {
//        PermissionX.init(this)
//            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
//            .request { allGranted, _, _ ->
//                // 如果没有请求权限则进行权限请求
//                if (!allGranted) {
//                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
//                            this, Manifest.permission.ACCESS_FINE_LOCATION
//                        ) && forceRequest
//                    ) {
//                        AppUtil.toSelfSettingActivity(baseContext)
//                    } else {
//                        requestLocPermission(forceRequest, callback)
//                    }
//                    return@request
//                }
//
//                // 如果授予了权限,需要通知 wifiFragment 刷新wifi列表
//                callback?.onAllGrant()
//                MMKVUtil.save(CacheKey.NEED_FRESH_WIFI_LIST_GRANTED, true)
//            }
//    }

    fun showToast(text: String) {
        ToastUtils.show(text)
    }


}