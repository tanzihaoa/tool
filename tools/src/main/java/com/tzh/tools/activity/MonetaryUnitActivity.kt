package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tzh.tools.R
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.fragment.MonetaryUnitFragment
import com.tzh.tools.fragment.NumberTransitionFragment
import com.tzh.tools.util.clickDelay
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar

/**
 * 金额转换
 */
class MonetaryUnitActivity : BaseLibActivity<BaseViewModel<*>>() {

    var mediator: TabLayoutMediator? = null
    val fragmentList = mutableListOf<Fragment>()
    val tabNames = mutableListOf<String>()
    private var viewPager2Adapter: ViewPager2Adapter? = null

    private var isAdResume = false
    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
        }
    }

    companion object {
        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            var intent = Intent(context, MonetaryUnitActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE, adType)
            context.startActivity(intent)
        }
    }


    @SuppressLint("SetTextI18n")
    override fun initView() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .statusBarDarkFont(true)
            .init()

        findViewById<TextView>(R.id.tv_title).text = "数字大小写转换"

        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }
        mediator?.detach()
        tabNames.add("金额转大写")
        tabNames.add("中文转数字")
        fragmentList.add(MonetaryUnitFragment())
        fragmentList.add(NumberTransitionFragment())
        viewPager2Adapter = ViewPager2Adapter()
        findViewById<ViewPager2>(R.id.view_pager_2).adapter = viewPager2Adapter
        mediator = TabLayoutMediator(
            findViewById<TabLayout>(R.id.tab),
            findViewById<ViewPager2>(R.id.view_pager_2)
        ) { tab, position ->
            tab.text = tabNames[position]
        }
        mediator?.attach()

    }


    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_monetary_unit
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

    inner class ViewPager2Adapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }
    }
}