package com.tzh.tools.activity

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseAdActivity
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityNoiseCheckBinding
import com.tzh.tools.util.CommonUtil
import com.tzh.tools.util.GetPermissionsUtil
import com.didichuxing.doraemonkit.util.IntentUtils
import com.permissionx.guolindev.PermissionX
import com.zlw.main.recorderlib.RecordManager
import java.io.File

/**
 * 噪音检测
 */
class NoiseCheckLibActivity : BaseAdActivity<BaseViewModel<*>, ActivityNoiseCheckBinding>() {
    private var type = 0
    private var isAdResume = false
    //录音文件路径
    private var mRecordingFile: File? = null

    // 间隔
    private val MIN_CLICK_DELAY_TIME = 500

    private var lastClickTime: Long = 0

    override fun onResume() {
        super.onResume()
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            if (intent.getIntExtra(GOTO_TYPE, 0) == 1) {
                LibAdBridge.instance.startInterstitial(this)
            } else if (intent.getIntExtra(GOTO_TYPE, 0) == 2) {
                LibAdBridge.instance.startRewardVideo(this)
            }

            isAdResume = true
            openPer()
            RecordManager.getInstance().start()
        }

    }

    companion object {

        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            val intent = Intent(context, NoiseCheckLibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(BaseLibActivity.KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE, adType)
            context.startActivity(intent)
        }
    }


    override fun initView() {
        type = intent.getIntExtra(GOTO_TYPE, 0)
        mDataBinding.top.findViewById<TextView>(R.id.tv_title).text = "噪音检测"
        mDataBinding.top.findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }
        mDataBinding.top.findViewById<TextView>(R.id.tv_title).setTextColor(Color.WHITE)
        mDataBinding.top.findViewById<ImageView>(R.id.iv_back)
            .setImageResource(R.mipmap.ic_left_in_white)

        //录音初始化
        RecordManager.getInstance()
            .init(applicationContext as Application?, true)

        recordInit()
    }

    private fun recordInit() {
        mDataBinding.ncDashboard.setPercent(0)
        mDataBinding.ncDashboard.setStartColor(Color.parseColor("#FF00CEFF"))
        mDataBinding.ncDashboard.setEndColor(Color.parseColor("#FFFFB800"))
        mDataBinding.ncDb.text = "0"
        val path = externalCacheDir
        RecordManager.getInstance().changeRecordDir(path.toString())
        RecordManager.getInstance().setRecordResultListener { file: File? ->
            recoreResult(file!!)
        }
        RecordManager.getInstance().setRecordSoundSizeListener { soundSize ->
            val time = System.currentTimeMillis()
            if (time - lastClickTime >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = time
//                LogUtil.d(">>>soundSize:$soundSize")
                if (soundSize in 0..160) {
                    mDataBinding.ncDashboard.setPercent(soundSize)
                    mDataBinding.ncDb.text = "$soundSize"
                    setDBText(soundSize)
                } else {
                    mDataBinding.ncDashboard.setPercent(0)
                    mDataBinding.ncDb.text = "0"
                }

            }
        }
//        RecordManager.getInstance().start()
    }

    //设置文本颜色
    private fun setDBText(soundSize: Int) {
        when (soundSize) {
            in 0..20 -> {
                mDataBinding.ncDb020.setTextColor(this.resources.getColor(R.color.white))
                mDataBinding.ncDb260.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb680.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb8120.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb12160.setTextColor(this.resources.getColor(R.color.c_99fff))
            }

            in 20..60 -> {
                mDataBinding.ncDb020.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb260.setTextColor(this.resources.getColor(R.color.white))
                mDataBinding.ncDb680.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb8120.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb12160.setTextColor(this.resources.getColor(R.color.c_99fff))
            }

            in 60..80 -> {
                mDataBinding.ncDb020.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb260.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb680.setTextColor(this.resources.getColor(R.color.white))
                mDataBinding.ncDb8120.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb12160.setTextColor(this.resources.getColor(R.color.c_99fff))
            }

            in 80..120 -> {
                mDataBinding.ncDb020.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb260.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb680.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb8120.setTextColor(this.resources.getColor(R.color.white))
                mDataBinding.ncDb12160.setTextColor(this.resources.getColor(R.color.c_99fff))
            }

            in 120..160 -> {
                mDataBinding.ncDb020.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb260.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb680.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb8120.setTextColor(this.resources.getColor(R.color.c_99fff))
                mDataBinding.ncDb12160.setTextColor(this.resources.getColor(R.color.white))
            }
        }
    }

    /**
     * 录音结果监听
     *
     * @param file
     */
    private fun recoreResult(file: File) {
        mRecordingFile = file
    }

    override fun onPause() {
        super.onPause()
        RecordManager.getInstance().stop()
    }


    fun openPer() {
        if (!GetPermissionsUtil.hasPer(this)) {
            val dialog = CommonUtil.showNoiseTipsDialog(this)
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        RecordManager.getInstance().start()
                        dialog.dismiss()
                    } else {
                        if (deniedList.size > 0) {
                            dialog.dismiss()
                            CommonUtil.getNeverNoiseDialog(this@NoiseCheckLibActivity,
                                onGranted = {
                                    startActivity(
                                        IntentUtils.getLaunchAppDetailsSettingsIntent(
                                            packageName
                                        )
                                    )
                                },
                                onDenied = { finish() })
                        } else {
                            finish()
                        }
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        deleteFile(mRecordingFile)
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_noise_check
    }

}