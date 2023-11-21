package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.adapter.MathsAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.extension.dp
import com.tzh.tools.extension.getStringColor
import com.tzh.tools.model.FormulaMathsBean
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

/**
 * Description:数学公式查询
 * @Author: LYL
 * @CreateDate: 2023/7/13 16:38
 */
class MathsFormulaQueryActivity : BaseLibActivity<BaseViewModel<*>>(){

    private val mathsAdapter by lazy { MathsAdapter() }

    private lateinit var recycleView: RecyclerView

    private var isAdResume = false

    companion object {
        //0:无广告   1:插屏   2:激励视频
        private const val GOTO_TYPE = "GOTO_TYPE"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1) {
            val intent = Intent(context, MathsFormulaQueryActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            intent.putExtra(GOTO_TYPE,adType)
            context.startActivity(intent)
        }

        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1,statusBarDarkFont: Boolean) {
            val intent = Intent(context, MathsFormulaQueryActivity::class.java)
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

        recycleView = findViewById(R.id.rv_maths)
        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context)
                    .size(12.dp)
                    .color(getStringColor("#F5F5F5"))
                    .build()
            )
            adapter = mathsAdapter
        }
        mathsAdapter.setList(formulaData)
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_maths_formula_query
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

    private val sins = listOf("sin²A+cos²A=1","tanA·cotA=1","tanA=sinA/cosA","cotA=cosA/sinA","sinA=cos（90°-A）","cotA=tan(90°-A)")

    private val  formulaData: ArrayList<FormulaMathsBean>

        get() = arrayListOf(
            FormulaMathsBean("三角函数",sins,"备注：sinA为正弦角，cosA为余弦角，tanA为正切角，cotA为余切角"),
            FormulaMathsBean("圆周长",listOf("C=2πR"),"备注：π为圆周率，R为圆的半径"),
            FormulaMathsBean("圆面积",listOf("S=πR²"),"备注：π为圆周率，R为圆的半径"),
            FormulaMathsBean("弧长",listOf("L=nπr/180=α*r"),"备注：n是圆心角度数（角度制)，r是半径，L是圆心角弧长，α是圆心角度数（弧度制)"),
            FormulaMathsBean("扇形面积",listOf("S=nπr²/360=rL/2"),"备注：n是圆心角度数(角度制），r是半径，L是圆心角弧长"),
            FormulaMathsBean("平行四边形面积",listOf("S=ah"),"备注：h为高，a为底，S为平行四边形面积"),
            FormulaMathsBean("菱形面积", listOf("S=1/2ab"),"备注：a，b为两条对角线的长度")
        )

}