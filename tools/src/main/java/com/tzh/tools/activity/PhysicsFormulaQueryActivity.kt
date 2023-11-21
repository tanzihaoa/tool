package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.adapter.FormulaAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.extension.dp
import com.tzh.tools.extension.getStringColor
import com.tzh.tools.model.FormulaPhysicsBean
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

/**
 * Description:物理公式查询
 * @Author: LYL
 * @CreateDate: 2023/7/13 15:38
 */
class PhysicsFormulaQueryActivity : BaseLibActivity<BaseViewModel<*>>(){

    private val formulaAdapter by lazy { FormulaAdapter() }

    private lateinit var recycleView: RecyclerView

    private var isAdResume = false

    companion object {
        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            val intent = Intent(context, PhysicsFormulaQueryActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE,adType)
            context.startActivity(intent)
        }
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1,statusBarDarkFont: Boolean) {
            val intent = Intent(context, PhysicsFormulaQueryActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE,adType)
            intent.putExtra(KEY_DARK, statusBarDarkFont)
            context.startActivity(intent)
        }
    }

    override fun initView() {

        ImmersionBar.with(this)
            .statusBarDarkFont(darkFront)
            .init()

        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }

        recycleView = findViewById(R.id.rv_physics)
        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context)
                    .size(12.dp)
                    .color(getStringColor("#F5F5F5"))
                    .build()
            )
            adapter = formulaAdapter
        }
        formulaAdapter.setList(formulaData)
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_physics_formula_query
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

    override fun onResume() {
        super.onResume()
        if (!isAdResume) {
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }

    private val  formulaData: ArrayList<FormulaPhysicsBean>
        get() = arrayListOf(
            FormulaPhysicsBean("重力","G=mg","备注：m为物体重量，g为比例系数，大小约为9.8N/kg"),
            FormulaPhysicsBean("密度","ρ=m/v","备注：m为物体重量，v为物体体积"),
            FormulaPhysicsBean("压强","P=F/S","备注：F代表垂直作用力（压力)，S代表受力面积"),
            FormulaPhysicsBean("液体压强","P=ρgh","备注：ρ表示液体的密度，g约等于9.8N/kg是物体重力与质量的比值（且在数值上等于重力加速度)(有时为了进行简便计算或粗略计算，g可以取10N/kg)，h表示液面下某处到自由液面(与大气接触的液面)的竖直距离"),
            FormulaPhysicsBean("速度","v=S/t","备注：S为路程，t为时间"),
        )
}