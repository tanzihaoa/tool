package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.adapter.ChemistryAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.extension.dp
import com.tzh.tools.extension.getStringColor
import com.tzh.tools.model.FormulaPhysicsBean
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

/**
 * Description:化学公式查询
 * @Author: LYL
 * @CreateDate: 2023/7/26 11:08
 */
class ChemistryFormulaQueryActivity : BaseLibActivity<BaseViewModel<*>>() {

    private val chemistryAdapter by lazy { ChemistryAdapter() }

    private lateinit var recycleView: RecyclerView

    private var isAdResume = false

    companion object {
        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            val intent = Intent(context, ChemistryFormulaQueryActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE, adType)
            context.startActivity(intent)
        }

        fun startActivity(
            context: Context,
            @LayoutRes layoutResID: Int?,
            adType: Int = 1,
            statusBarDarkFont: Boolean,
        ) {
            val intent = Intent(context, ChemistryFormulaQueryActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE, adType)
            intent.putExtra(KEY_DARK, statusBarDarkFont)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .statusBarDarkFont(darkFront)
            .init()

        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }

        recycleView = findViewById(R.id.rv_chemistry)
        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context)
                    .size(12.dp)
                    .color(getStringColor("#F5F5F5"))
                    .build()
            )
            adapter = chemistryAdapter
        }
        chemistryAdapter.setList(formulaData)

    }

    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_chemistry_formula_query
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

    private val formulaData: ArrayList<FormulaPhysicsBean>
        get() = arrayListOf(
            FormulaPhysicsBean(
                "铁与硫酸铜反应：",
                "Fe+CuSO₄=FeSO₄+Cu",
                "备注：铁的活动性比铜强，铁与硫酸铜反应生成硫酸亚铁和铜"
            ),
            FormulaPhysicsBean(
                "氧化铁与硫酸反应：",
                "3H₂SO₄+Fe₂0₃=Fe₂(SO₄)₃+3H₂0",
                "备注：氧化铁和硫酸反应的实质是氢离子和金属氧化物反应形成金属阳离子和水"
            ),
        )
}