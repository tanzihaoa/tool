package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.text.format.Time
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.LibTimeUtils.changeFormatDateCN
import java.util.*

class TodayEatWhatActivity : BaseLibActivity<BaseViewModel<*>>() {

    private val eatMenuSet: MutableList<String> = mutableListOf()
    private val eatMenuLinkedHashMap: MutableMap<Int, String> = mutableMapOf()
    private var tvMenu: TextView? = null
    private var tvRandomMenu: android.widget.TextView? = null
    private var tvTimeTitle: android.widget.TextView? = null
    private var tvEatToday: android.widget.TextView? = null
    private var ivWhatEatTodayBack: ImageView? = null
    private val random = Random()
    private val list: MutableList<Int> = ArrayList()

    private var isAdResume = false

    private fun initData() {
        eatMenuSet.add("玉带虾仁")
        eatMenuSet.add("油发豆莛")
        eatMenuSet.add("红扒鱼翅")
        eatMenuSet.add("白扒通天翅")
        eatMenuSet.add("孔府一品锅")
        eatMenuSet.add("花揽桂鱼")
        eatMenuSet.add("纸包鸡")
        eatMenuSet.add("焖大虾")
        eatMenuSet.add("锅烧鸡")
        eatMenuSet.add("山东菜丸")
        eatMenuSet.add("麻婆豆腐")
        eatMenuSet.add("辣子鸡丁")
        eatMenuSet.add("东坡肘子")
        eatMenuSet.add("豆瓣鲫鱼")
        eatMenuSet.add("口袋豆腐")
        eatMenuSet.add("酸菜鱼")
        eatMenuSet.add("夫妻肺片")
        eatMenuSet.add("蚂蚁上树")
        eatMenuSet.add("叫花鸡")
        eatMenuSet.add("鱼香肉丝")
        eatMenuSet.add("咸菜焖猪肉")
        eatMenuSet.add("酿茄子")
        eatMenuSet.add("酿豆腐")
        eatMenuSet.add("梅菜扣肉")
        eatMenuSet.add("客家盐焗鸡")
        eatMenuSet.add("广式烧填鸭")
        eatMenuSet.add("烧鹅")
        eatMenuSet.add("红槽排骨")
        eatMenuSet.add("豆豉蒸排骨")
        eatMenuSet.add("煎酿三宝")
        eatMenuSet.add("卤猪")
        eatMenuSet.add("腊肉")
        eatMenuSet.add("晾肉")
        eatMenuSet.add("香肠儿")
        eatMenuSet.add("什锦苏盘儿")
        eatMenuSet.add("江米酿鸭子")
        eatMenuSet.add("罐儿野鸡")
        eatMenuSet.add("罐儿鹌鹑")
        eatMenuSet.add("烩海参")
        /**
         * 把菜单都添加到list中间去
         */
        for (i in eatMenuSet.indices) {
            eatMenuLinkedHashMap.put(i, eatMenuSet[i])
        }
        tvEatToday!!.text = if (isCurrentInTimeScope(0, 0, 12, 0)) "今天" else "明天"
        tvTimeTitle!!.text = changeFormatDateCN()
        tvRandomMenu!!.setOnClickListener { view: View? ->
            val todayMenu: Boolean = getTodayMenu()
            if (todayMenu) {
                getTodayMenu()
            }
        }
        ivWhatEatTodayBack!!.setOnClickListener { view: View? -> onBackPressed() }
    }

    fun isCurrentInTimeScope(beginHour: Int, beginMin: Int, endHour: Int, endMin: Int): Boolean {
        var result = false
        val aDayInMillis = (1000 * 60 * 60 * 24).toLong()
        val currentTimeMillis = System.currentTimeMillis()
        val now = Time()
        now.set(currentTimeMillis)
        val startTime = Time()
        startTime.set(currentTimeMillis)
        startTime.hour = beginHour
        startTime.minute = beginMin
        val endTime = Time()
        endTime.set(currentTimeMillis)
        endTime.hour = endHour
        endTime.minute = endMin
        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis)
            result = !now.before(startTime) && !now.after(endTime) // startTime <= now <= endTime
            val startTimeInThisDay = Time()
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis)
            if (!now.before(startTimeInThisDay)) {
                result = true
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime) // startTime <= now <= endTime
        }
        return result
    }


    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            var intent = Intent(context, TodayEatWhatActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    private fun getTodayMenu(): Boolean {

        //产生一个0-size的随机数
        val i = random.nextInt(eatMenuSet.size)
        //判断是否出现过
        for (integer in list) {
            if (integer == i) {
                Log.d("---------", "重复了...")
                //如果出现过，就跳出这次循环，重新调用一下
                return true
            }
        }
        val s = eatMenuLinkedHashMap[i]
        tvMenu!!.text = s
        list.add(i)
        return false
    }

    override fun initView() {
        tvMenu = findViewById(R.id.tvMenu)
        tvRandomMenu = findViewById(R.id.tvRandomMenu)
        tvTimeTitle = findViewById(R.id.tvTimeTitle)
        tvEatToday = findViewById(R.id.tvEatToday)
        ivWhatEatTodayBack = findViewById(R.id.ivWhatEatTodayBack)

        initData()
        getTodayMenu()

    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_what_eat_today
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}