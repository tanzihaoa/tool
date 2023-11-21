package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.clickDelay
import com.didichuxing.doraemonkit.util.LunarUtils
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.gyf.immersionbar.ImmersionBar
import java.util.Calendar
import java.util.Date
import kotlin.math.max

/**
 *
 * 时长计算
 * TTTT
 */
class DurationCalculationActivity : BaseLibActivity<BaseViewModel<*>>() {
    companion object {
        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 0) {
            var intent = Intent(context, DurationCalculationActivity::class.java)
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


    //选择日期
    private var mTvelectDuration:TextView? = null
    //周岁
    private var mOneFullYear:TextView? = null
    //虚岁
    private var mNominalAge:TextView? = null
    //生肖
    private var mAnimalSign:TextView? = null
    //年
    private var mDurationYear:TextView? = null
    //月
    private var mDurationMonth:TextView? = null
    //天
    private var mDurationDay:TextView? = null

    private var mDatePicker: DatePicker? = null
    private var mDateEntity: DateEntity = DateEntity.today()
    //生日
    private val mCalendar = Calendar.getInstance()
    //当前日期
    private val mCalendarDate = Calendar.getInstance()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .statusBarDarkFont(true)
            .titleBar(findViewById(R.id.fl_top))
            .init()
        findViewById<ImageView>(R.id.but_back).setOnClickListener { finish() }
       findViewById<View>(R.id.but_select_duration).clickDelay {
           //选择日期
           if (mDatePicker == null) {
               mDatePicker = DatePicker(this)
           }
           mDatePicker?.wheelLayout?.let {
               it.setDateMode(DateMode.YEAR_MONTH_DAY)
               it.setDateLabel("年", "月", "日")
               it.setRange(
                   DateEntity.target(1920, 1, 1),
                   DateEntity.target(mCalendarDate.get(Calendar.YEAR), mCalendarDate.get(Calendar.MONTH)+1, mCalendarDate.get(Calendar.DAY_OF_MONTH)),
                   mDateEntity
               )
               it.setCurtainEnabled(false)
           }
           mDatePicker?.setOnDatePickedListener { year, month, day ->
               mTvelectDuration?.text = "${year}年${month}月${day}日"
               mDateEntity.day = day
               mDateEntity.month = month
               mDateEntity.year = year
               mCalendar.set(year, month, day)

               //计算周岁
               mOneFullYear?.text = getCurrentAge().toString()
               //生肖
               mAnimalSign?.text = getZodiacByYear(year)
               //虚岁
               mNominalAge?.text = getCurrentNominalAge(year,month,day).toString()
               //年
               val yearAge = max(mCalendarDate.get(Calendar.YEAR) - year,0)
               mDurationYear?.text = yearAge.toString()
               //月
               mDurationMonth?.text = max(((yearAge*12) + (mCalendarDate.get(Calendar.MONTH)+1)),0).toString()
               //天
               mDurationDay?.text = differentDays(mCalendar.time,mCalendarDate.time).toString()


           }
           mDatePicker?.show()
       }
        mTvelectDuration = findViewById(R.id.tv_select_duration)
        mOneFullYear = findViewById(R.id.tv_one_full_year)
        mNominalAge = findViewById(R.id.tv_nominal_age)
        mAnimalSign = findViewById(R.id.tv_animal_sign)
        mDurationYear = findViewById(R.id.tv_duration_year)
        mDurationMonth = findViewById(R.id.tv_duration_month)
        mDurationDay = findViewById(R.id.tv_duration_day)
    }


    /**
     * 两个日期相差天数
     */
    fun differentDays(beforeDate: Date, afterDate: Date): Int {
        var result = 0

        //将Date类型转换为Calendar类型
        val beforeCalendar = Calendar.getInstance()
        beforeCalendar.time = beforeDate
        val afterCalendar = Calendar.getInstance()
        afterCalendar.time = afterDate

        //获取日期的DayOfYear（这一天在是这一年的第多少天）
        val beforeDayOfYear = beforeCalendar.get(Calendar.DAY_OF_YEAR)
        val afterDayOfYear = afterCalendar.get(Calendar.DAY_OF_YEAR)

        //获取日期的年份
        val beforeYear = beforeCalendar.get(Calendar.YEAR)
        val afterYear = afterCalendar.get(Calendar.YEAR)

        if (beforeYear == afterYear) {
            //同一年
            result = afterDayOfYear - beforeDayOfYear
        } else {
            //不同一年
            var timeDistance = 0
            for (i in beforeYear until afterYear) {
                timeDistance += if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    //闰年
                    366
                } else {
                    //不是闰年
                    365
                }
            }
            result = timeDistance + (afterDayOfYear - beforeDayOfYear)
        }

        return result
    }


    /**
     * 根据生日计算当前虚岁数
     */
    private fun getCurrentNominalAge(year:Int,month:Int,monthDay:Int):Int {
        // 当前时间
        val curr = Calendar.getInstance()
        val curr666 = LunarUtils.solar2Lunar(LunarUtils.Solar(curr.get(Calendar.YEAR),curr.get(Calendar.MONTH)+1,curr.get(Calendar.DAY_OF_MONTH)))
        curr.set(curr666.lunarYear,curr666.lunarMonth,curr666.lunarDay)

        // 生日
        val curr777 = LunarUtils.solar2Lunar(LunarUtils.Solar(year,month,monthDay))
        val born = Calendar.getInstance()
        born.set(curr777.lunarYear,curr777.lunarMonth,curr777.lunarDay)

//        LogUtil.e("当前农历-11-->${curr666.lunarYear}")
//        LogUtil.e("当前农历-11-->${curr666.lunarMonth}")
//        LogUtil.e("当前农历-11-->${curr666.lunarDay}")
//
//        LogUtil.e("当前农历-22-->${curr777.lunarYear}")
//        LogUtil.e("当前农历-22-->${curr777.lunarMonth}")
//        LogUtil.e("当前农历-22-->${curr777.lunarDay}")

        // 年龄 = 当前年 - 出生年
        var age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR)
        if (age <= 0) return 0
        // 如果当前月份小于出生月份: age-1
        // 如果当前月份等于出生月份, 且当前日小于出生日: age-1
        val currMonth = curr.get(Calendar.MONTH);
        val currDay = curr.get(Calendar.DAY_OF_MONTH);
        val bornMonth = curr.get(Calendar.MONTH);
        val bornDay = curr.get(Calendar.DAY_OF_MONTH);
        if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
            age--
        }
        return if(age < 0)  0 else age
    }
    /**
     * 根据生日计算当前周岁数
     */
    private fun getCurrentAge():Int {
        // 年龄 = 当前年 - 出生年
        var age = mCalendarDate.get(Calendar.YEAR) - mCalendar.get(Calendar.YEAR);
        if (age <= 0) return 0
        // 如果当前月份小于出生月份: age-1
        // 如果当前月份等于出生月份, 且当前日小于出生日: age-1
        val currMonth = mCalendarDate.get(Calendar.MONTH);
        val currDay = mCalendarDate.get(Calendar.DAY_OF_MONTH);
        val bornMonth = mCalendarDate.get(Calendar.MONTH);
        val bornDay = mCalendarDate.get(Calendar.DAY_OF_MONTH);
        if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
            age--
        }
        return if(age < 0)  0 else age
    }

    /**
     * 根据年份计算生肖
     *
     * @param year 2021
     * @return
     */
    private fun getZodiacByYear(year:Int):String {
        if (year < 1900)return "未知"
        val start = 1900
        val years = arrayOf(
            "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"
        )
        return years[(year - start) % years.size]
    }

    override fun initDataObserver() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_duration_calculation
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

}