package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tzh.tools.R
import com.tzh.tools.adapter.TraditionColorAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.model.TraditionColorBean
import com.tzh.tools.util.clickDelay

class ChineseTraditionColorActivity : BaseLibActivity<BaseViewModel<*>>() {

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            val intent = Intent(context, ChineseTraditionColorActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    private val mAdapter by lazy {
        TraditionColorAdapter(getColorList())
    }

    override fun initView() {
        findViewById<TextView>(R.id.tv_title).text = "中国传统颜色"
        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }
        findViewById<RecyclerView>(R.id.recycle_view).adapter = mAdapter
    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_chinese_tradition_color
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

    /**
     * 获取颜色集合
     */
    private fun getColorList() : MutableList<TraditionColorBean>{
        val list = mutableListOf<TraditionColorBean>()
        list.add(TraditionColorBean("暗玉紫","#5C2221"))
        list.add(TraditionColorBean("牡丹粉红","#EEA2A5"))
        list.add(TraditionColorBean("栗紫","#58191B"))
        list.add(TraditionColorBean("香叶红","#EE7C80"))
        list.add(TraditionColorBean("葡萄酱紫","#5A1315"))
        list.add(TraditionColorBean("艳红","#EB5A64"))
        list.add(TraditionColorBean("玉红","#C04850"))
        list.add(TraditionColorBean("茶花红","#EE3F4D"))
        list.add(TraditionColorBean("高粱红","#C02C38"))
        list.add(TraditionColorBean("满江红","#A7535A"))
        list.add(TraditionColorBean("鼠鼻红","#E3B4B8"))
        list.add(TraditionColorBean("合欢红","#F0A1A8"))
        list.add(TraditionColorBean("春梅红","#F1939C"))
        list.add(TraditionColorBean("苋菜红","#A61B29"))
        list.add(TraditionColorBean("烟红","#894E54"))
        list.add(TraditionColorBean("莓红","#C45A65"))
        list.add(TraditionColorBean("鹅冠红","#D11A2D"))
        list.add(TraditionColorBean("枫叶红","#C21F30"))
        list.add(TraditionColorBean("唐菖蒲红","#DE1C31"))
        list.add(TraditionColorBean("枣红","#7C1823"))
        list.add(TraditionColorBean("猪肝紫","#541E24"))
        list.add(TraditionColorBean("葡萄紫","#4C1F24"))
        list.add(TraditionColorBean("暗紫苑红","#82202B"))
        list.add(TraditionColorBean("殷红","#82111F"))
        list.add(TraditionColorBean("草茉莉红","#EF475D"))
        list.add(TraditionColorBean("酱紫","#4D1018"))
        list.add(TraditionColorBean("山茶红","#ED556A"))
        list.add(TraditionColorBean("锌灰","#7A7374"))
        list.add(TraditionColorBean("海棠红","#F03752"))
        list.add(TraditionColorBean("蓟粉红","#E6D2D5"))
        list.add(TraditionColorBean("石蕊红","#F0C9CF"))
        list.add(TraditionColorBean("淡曙红","#EE2746"))
        list.add(TraditionColorBean("菜头紫","#951C48"))
        list.add(TraditionColorBean("葡萄酒红","#62102E"))
        list.add(TraditionColorBean("淡青紫","#E0C8D1"))
        list.add(TraditionColorBean("菠根红","#D13C74"))
        list.add(TraditionColorBean("海象紫","#4B1E2F"))
        list.add(TraditionColorBean("兔眼红","#EC4E8A"))
        list.add(TraditionColorBean("嫩菱红","#DE3F7C"))
        list.add(TraditionColorBean("洋葱紫","#A8456B"))
        list.add(TraditionColorBean("吊钟花红","#CE5E8A"))
        list.add(TraditionColorBean("绀紫","#461629"))
        list.add(TraditionColorBean("紫荆红","#EE2C79"))
        list.add(TraditionColorBean("扁豆花红","#EF498B"))
        list.add(TraditionColorBean("马鞭草紫","#EDE3E7"))
        list.add(TraditionColorBean("藏花红","#EC2D7A"))
        list.add(TraditionColorBean("斑鸠灰","#482936"))
        list.add(TraditionColorBean("古铜紫","#440E25"))
        list.add(TraditionColorBean("丹紫红","#D2568C"))
        list.add(TraditionColorBean("丁香淡紫","#E9D7DF"))
        list.add(TraditionColorBean("古鼎灰","#36292F"))
        list.add(TraditionColorBean("菱锰红","#D276A3"))
        list.add(TraditionColorBean("樱草紫","#C06F98"))
        list.add(TraditionColorBean("玫瑰紫","#BA2F7B"))
        list.add(TraditionColorBean("苋菜紫","#9B1E64"))
        list.add(TraditionColorBean("紫灰","#5D3F51"))
        list.add(TraditionColorBean("龙睛鱼紫","#4E2A40"))
        list.add(TraditionColorBean("青蛤壳紫","#BC84A8"))
        list.add(TraditionColorBean("萝兰紫","#C08EAF"))
        list.add(TraditionColorBean("荸荠紫","#411C35"))
        list.add(TraditionColorBean("豆蔻紫","#AD6598"))
        list.add(TraditionColorBean("扁豆紫","#A35C8F"))
        list.add(TraditionColorBean("牵牛紫","#681752"))
        list.add(TraditionColorBean("芓紫","#894276"))
        list.add(TraditionColorBean("葛巾紫","#7E2065"))
        list.add(TraditionColorBean("青莲","#8B2671"))
        list.add(TraditionColorBean("芥花紫","#983680"))
        list.add(TraditionColorBean("凤信紫","#C8ADC4"))
        list.add(TraditionColorBean("深牵牛紫","#1C0D1A"))
        list.add(TraditionColorBean("魏紫","#7E1671"))
        list.add(TraditionColorBean("乌梅紫","#1E131D"))
        list.add(TraditionColorBean("桔梗紫","#813C85"))
        list.add(TraditionColorBean("牛角灰","#2D2E36"))
        list.add(TraditionColorBean("鱼尾灰","#5E616D"))
        list.add(TraditionColorBean("瓦罐灰","#47484C"))
        list.add(TraditionColorBean("钢蓝","#0F1423"))
        list.add(TraditionColorBean("燕颔蓝","#131824"))
        list.add(TraditionColorBean("鲸鱼灰","#475164"))
        list.add(TraditionColorBean("青灰","#2B333E"))
        list.add(TraditionColorBean("鸽蓝","#1C2938"))
        list.add(TraditionColorBean("暗蓝","#101F30"))
        list.add(TraditionColorBean("钢青","#142334"))
        list.add(TraditionColorBean("海涛蓝","#15559A"))
        list.add(TraditionColorBean("飞燕草蓝","#0F59A4"))
        list.add(TraditionColorBean("靛青","#1661AB"))
        list.add(TraditionColorBean("安安蓝","#3170A7"))
        list.add(TraditionColorBean("海军蓝","#346C9C"))
        list.add(TraditionColorBean("景泰蓝","#2775B6"))
        list.add(TraditionColorBean("品蓝","#2B73AF"))
        list.add(TraditionColorBean("尼罗蓝","#2474B5"))
        list.add(TraditionColorBean("蝶翅蓝","#4E7CA1"))
        list.add(TraditionColorBean("云水蓝","#BACCD9"))
        list.add(TraditionColorBean("蓝绿","#12A182"))
        list.add(TraditionColorBean("竹绿","#1BA784"))
        list.add(TraditionColorBean("亚丁绿","#428675"))
        list.add(TraditionColorBean("月影白","#C0C4C3"))
        list.add(TraditionColorBean("海王绿","#248067"))
        list.add(TraditionColorBean("深海绿","#1A3B32"))
        list.add(TraditionColorBean("绿灰","#314A43"))
        list.add(TraditionColorBean("青矾绿","#2C9678"))
        list.add(TraditionColorBean("苍绿","#223E36"))
        list.add(TraditionColorBean("飞泉绿","#497568"))
        list.add(TraditionColorBean("莽丛绿","#141E1B"))
        list.add(TraditionColorBean("梧枝绿","#69A794"))
        list.add(TraditionColorBean("铜绿","#2BAE85"))
        list.add(TraditionColorBean("草原远绿","#9ABEAF"))
        list.add(TraditionColorBean("蛙绿","#45B787"))
        list.add(TraditionColorBean("粉绿","#83CBAC"))
        list.add(TraditionColorBean("蝶黄","#E2D849"))
        list.add(TraditionColorBean("橄榄绿","#5E5314"))
        list.add(TraditionColorBean("淡灰绿","#AD9E5F"))
        list.add(TraditionColorBean("佛手黄","#FED71A"))
        list.add(TraditionColorBean("香蕉黄","#ffe135"))
        list.add(TraditionColorBean("油菜花黄","#FBDA41"))
        list.add(TraditionColorBean("秋葵黄","#EED045"))
        list.add(TraditionColorBean("柚黄","#F1CA17"))
        list.add(TraditionColorBean("草黄","#D2B42C"))
        list.add(TraditionColorBean("硫华黄","#F2CE2B"))
        list.add(TraditionColorBean("姜黄","#E2C027"))
        list.add(TraditionColorBean("金瓜黄","#FCD217"))
        list.add(TraditionColorBean("麦秆黄","#F8DF70"))
        list.add(TraditionColorBean("蒿黄","#DFC243"))
        list.add(TraditionColorBean("茉莉黄","#F8DF72"))
        list.add(TraditionColorBean("藤黄","#FFD111"))
        list.add(TraditionColorBean("芒果黄","#DDC871"))
        list.add(TraditionColorBean("古铜褐","#5C3719"))
        list.add(TraditionColorBean("麂棕","#DE7622"))
        list.add(TraditionColorBean("玫瑰粉","#F8B37F"))
        list.add(TraditionColorBean("美人焦橙","#FA7E23"))
        list.add(TraditionColorBean("米色","#F9E9CD"))
        list.add(TraditionColorBean("蛛网灰","#B7A091"))
        list.add(TraditionColorBean("淡咖啡","#945833"))
        list.add(TraditionColorBean("海螺橙","#F0945D"))
        list.add(TraditionColorBean("岩石棕","#964D22"))
        list.add(TraditionColorBean("芒果棕","#954416"))
        list.add(TraditionColorBean("金莲花橙","#F86B1D"))
        list.add(TraditionColorBean("玫瑰灰","#4B2E2B"))
        list.add(TraditionColorBean("淡菽红","#ED4845"))
        list.add(TraditionColorBean("枸枢红","#ED3333"))
        list.add(TraditionColorBean("貂紫","#5D3131"))
        return list
    }
}