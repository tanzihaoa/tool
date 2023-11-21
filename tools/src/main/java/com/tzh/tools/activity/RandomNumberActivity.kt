package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.adapter.RandomNumberAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.extension.dp
import com.tzh.tools.extension.getStringColor
import com.tzh.tools.util.clickDelay
import com.tzh.tools.view.GridDividerItemDecoration
import com.gyf.immersionbar.ImmersionBar
import java.util.HashMap

/**
 * 随机数生成
 * TTT
 */
class RandomNumberActivity : BaseLibActivity<BaseViewModel<*>>() {

    companion object {
        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 0) {
            val intent = Intent(context, RandomNumberActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE,adType)
            context.startActivity(intent)
        }
    }

    private var isAdResume = false
    override fun onResume() {
        super.onResume()
        if(!isAdResume){
            isAdResume = true
            if(intent.getIntExtra(GOTO_TYPE, 0)==1){
                LibAdBridge.instance.startInterstitial(this)
            }else if(intent.getIntExtra(GOTO_TYPE, 0)==2){
                LibAdBridge.instance.startRewardVideo(this)
            }
        }
    }
    //随要集合
    private var randomAdapter: RandomNumberAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mEditStart:AppCompatEditText? = null
    private var mEditEnd: AppCompatEditText? = null
    private var mEditRandomNumber: AppCompatEditText? = null
    private var mButRandomNumberYes:TextView? = null
    private var mButRandomNumberNo:TextView? = null
    private var isWeiYi = false

    override fun initView() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .statusBarDarkFont(true)
            .titleBar(findViewById(R.id.fl_top))
            .init()

        findViewById<ImageView>(R.id.but_back).setOnClickListener { finish() }

        mEditStart = findViewById(R.id.ed_random_start)
        mEditEnd = findViewById(R.id.ed_random_end)
        mEditRandomNumber = findViewById(R.id.ed_random_number)
        mButRandomNumberYes = findViewById(R.id.but_random_number_yes)
        mButRandomNumberNo = findViewById(R.id.but_random_number_no)

        if(null == randomAdapter)randomAdapter = RandomNumberAdapter()
        mRecyclerView = findViewById(R.id.random_list)
        mRecyclerView?.apply {
            layoutManager = GridLayoutManager(this@RandomNumberActivity,3)
            addItemDecoration(
                com.tzh.tools.view.GridDividerItemDecoration(
                    10.dp,
                    10.dp,
                    getStringColor("#00000000")
                )
            )
            adapter = randomAdapter
        }

        setWeiYiView()
        mButRandomNumberYes?.clickDelay {
            isWeiYi = true
            setWeiYiView()
        }
        mButRandomNumberNo?.clickDelay {
            isWeiYi = false
            setWeiYiView()
        }
        findViewById<View>(R.id.but_random_number).clickDelay {
            //生成随机数
            val startStr = mEditStart?.editableText.toString()
            val endStr = mEditEnd?.editableText.toString()
            val numberStr = mEditRandomNumber?.editableText.toString()
            if(TextUtils.isEmpty(startStr) || TextUtils.isEmpty(endStr)){
                showToast("请正确输入随机数范围")
                return@clickDelay
            }
            if(TextUtils.isEmpty(numberStr)){
                showToast("请正确输入随机数个数")
                return@clickDelay
            }
            try {
                val start = startStr.toLong()
                val end = endStr.toLong()
                val number = numberStr.toLong()
                if(0>=end || 0>=number){
                    showToast("请输入不能为零")
                    return@clickDelay
                }
                if(start <= -1){
                    showToast("请正确输入随机数范围")
                    return@clickDelay
                }
                if(start > end){
                    showToast("请正确输入随机数范围")
                    return@clickDelay
                }

                val dataList = mutableListOf<Long>()
                val mapHase = HashMap<Long,String>()
                while (number > dataList.size){
                    val random = (start..end).random()
                    if(isWeiYi){
                        if(!mapHase.containsKey(random)){
                            mapHase[random] = ""
                            dataList.add(random)
                        }
                    }else{
                        dataList.add(random)
                    }
                }
                randomAdapter?.setList(dataList)
            }catch (e:Exception){
                showToast("请正确输入数字")
            }
        }
    }



    private fun setWeiYiView(){
        if(isWeiYi){
            mButRandomNumberYes?.setTextColor(Color.parseColor("#009DFF"))
            mButRandomNumberYes?.setBackgroundResource(R.drawable.random_number_btn_bg_shape2)
            mButRandomNumberNo?.setTextColor(Color.parseColor("#000000"))
            mButRandomNumberNo?.setBackgroundResource(0)
        }else{
            mButRandomNumberNo?.setTextColor(Color.parseColor("#009DFF"))
            mButRandomNumberNo?.setBackgroundResource(R.drawable.random_number_btn_bg_shape2)
            mButRandomNumberYes?.setTextColor(Color.parseColor("#000000"))
            mButRandomNumberYes?.setBackgroundResource(0)
        }
    }


    override fun initDataObserver() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_random_number
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

}