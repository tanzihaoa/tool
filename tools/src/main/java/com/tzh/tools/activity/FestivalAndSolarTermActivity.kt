package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.allen.library.shape.ShapeFrameLayout
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.extension.getStringColor
import com.tzh.tools.fragment.HolidaysFragment
import com.tzh.tools.fragment.PublicVacationFragment
import com.tzh.tools.fragment.SolarTermFragment
import com.tzh.tools.vm.FestivalAndSolarTermViewModel

/**
 * @author: Created by xyc
 * created on: 2022/7/19
 * description:节日与节气
 */
class FestivalAndSolarTermActivity : BaseLibActivity<FestivalAndSolarTermViewModel>() {

    private var isAdResume = false

    private lateinit var ivBack: AppCompatImageView
    private lateinit var tvPublicVacation: AppCompatTextView
    private lateinit var tvSolarTerm: AppCompatTextView
    private lateinit var tvHolidays: AppCompatTextView
    private lateinit var vIndicator: ShapeFrameLayout
    private lateinit var viewPager2: ViewPager2

    // Tab集合
    private val tabs = listOf("公众节日", "节气", "节假日")

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            val intent = Intent(context, FestivalAndSolarTermActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }

        fun startActivity(context: Context, showAd: Boolean, @LayoutRes layoutResID: Int?) {
            val intent = Intent(context, FestivalAndSolarTermActivity::class.java)
            intent.putExtra("showAd", showAd)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_festival_and_solar_term
    }

    override fun getViewModel(): Class<FestivalAndSolarTermViewModel> {
        return FestivalAndSolarTermViewModel::class.java
    }

    override fun initView() {
        ivBack = findViewById(R.id.iv_back)
        tvPublicVacation = findViewById(R.id.tv_public_vacation)
        tvSolarTerm = findViewById(R.id.tv_solar_term)
        tvHolidays = findViewById(R.id.tv_holidays)
        vIndicator = findViewById(R.id.v_indicator)
        viewPager2 = findViewById(R.id.view_pager_2)

        ivBack.setOnClickListener {
            onBackPressed()
        }

        tvPublicVacation.setOnClickListener {
            val index = 0
            updateTabText(index)
            viewPager2.currentItem = index
        }

        tvSolarTerm.setOnClickListener {
            val index = 1
            updateTabText(index)
            viewPager2.currentItem = index
        }

        tvHolidays.setOnClickListener {
            val index = 2
            updateTabText(index)
            viewPager2.currentItem = index
        }

        viewPager2.offscreenPageLimit = tabs.size
        viewPager2.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return tabs.size
            }

            override fun createFragment(position: Int): Fragment {
                if (1 == position) return SolarTermFragment.newInstance()
                if (2 == position) return HolidaysFragment.newInstance()
                return PublicVacationFragment.newInstance()
            }
        }
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun initDataObserver() {}

    override fun onResume() {
        super.onResume()
        if (intent.getBooleanExtra("showAd", true)) {
            if (!isAdResume) {
                isAdResume = true
                LibAdBridge.instance.startInterstitial(this)
            }
        }
    }

    override fun onDestroy() {
        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback)
        viewPager2.adapter = null
        super.onDestroy()
    }

    /**
     * 更新Tab字体颜色
     * @param position Int
     */
    private fun updateTabText(position: Int) {
        tvPublicVacation.setTextColor(getStringColor("#666666"))
        tvSolarTerm.setTextColor(getStringColor("#666666"))
        tvHolidays.setTextColor(getStringColor("#666666"))
        when (position) {
            0 -> {
                tvPublicVacation.setTextColor(getStringColor("#F30213"))
            }
            1 -> {
                tvSolarTerm.setTextColor(getStringColor("#F30213"))
            }
            2 -> {
                tvHolidays.setTextColor(getStringColor("#F30213"))
            }
        }
    }

    /**
     * ViewPager2页面监听器
     */
    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val layoutParams = vIndicator.layoutParams as? ConstraintLayout.LayoutParams
            when (position) {
                0 -> {
                    layoutParams?.startToStart = R.id.tv_public_vacation
                    layoutParams?.endToEnd = R.id.tv_public_vacation
                }
                1 -> {
                    layoutParams?.startToStart = R.id.tv_solar_term
                    layoutParams?.endToEnd = R.id.tv_solar_term
                }
                2 -> {
                    layoutParams?.startToStart = R.id.tv_holidays
                    layoutParams?.endToEnd = R.id.tv_holidays
                }
            }
            vIndicator.layoutParams = layoutParams
            updateTabText(position)
        }
    }
}