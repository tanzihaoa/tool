package com.tzh.tools.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.adapter.TestSpeedAdapter
import com.tzh.tools.base.BaseAdActivity
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.ActivityTestSpeedBinding
import com.tzh.tools.model.TestSpeedEnum
import com.tzh.tools.model.TestSpeedModel
import com.tzh.tools.util.CommonUtil
import com.tzh.tools.util.WifiUtils
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 */
class TestSpeedLibActivity : BaseAdActivity<BaseViewModel<*>, ActivityTestSpeedBinding>() {

    private var isAdResume = false

    override fun onResume() {
        super.onResume()
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

    lateinit var type: TestSpeedEnum
    private val mList = mutableListOf<TestSpeedModel>()
    private lateinit var mAdapter: TestSpeedAdapter

    companion object {

        private const val GOTO_TYPE = "GOTO_TYPE"

        fun startActivity(context: Context, @LayoutRes layoutResID: Int?, adType: Int = 1, type: TestSpeedEnum, title: String) {
            val intent = Intent(context, TestSpeedLibActivity::class.java)
            layoutResID?.let {
                intent.putExtra(BaseLibActivity.KEY_LAYOUT, layoutResID)
            }
            intent.putExtra("type", type)
            intent.putExtra("title", title)
            intent.putExtra(GOTO_TYPE,adType)
            context.startActivity(intent)
        }

    }

    override fun initView() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()
        mDataBinding.top.findViewById<TextView>(R.id.tv_title).text = intent.getStringExtra("title")
        mDataBinding.top.findViewById<ImageView>(R.id.iv_right).apply {
            visibility = View.VISIBLE
            setImageResource(R.mipmap.ic_tips)
            clickDelay(500) {
                CommonUtil.getTipsDialog(this@TestSpeedLibActivity)
            }
        }
        mDataBinding.top.findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }

        mAdapter = TestSpeedAdapter(mList)
        mDataBinding.rcvTestSpeed.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.cl_content)
        mAdapter.setOnItemChildClickListener { _, view, pos ->
            val speedLottieView = view.findViewById<LottieAnimationView>(R.id.lottie_ping)
            speedLottieView.visibility = View.VISIBLE
            speedLottieView.repeatCount = -1
            speedLottieView.playAnimation()

            val speedTextView = view.findViewById<TextView>(R.id.tv_speed)
            speedTextView.visibility = View.INVISIBLE

            WifiUtils.getDelayedNet(mList[pos].ip, 1) {

                var delay = ""

                delay = if (it.toInt() == -1) {
                    "80+"
                } else {
                    it.toString()
                }

                mList[pos].speed = "${delay}/ms"
                speedLottieView.visibility = View.GONE
                speedTextView.visibility = View.VISIBLE
                mAdapter.notifyItemChanged(pos)
            }
        }
        mDataBinding.rcvTestSpeed.layoutManager = LinearLayoutManager(this)

        type = intent.getSerializableExtra("type") as TestSpeedEnum
        loadIconByType(type)
    }

    private fun loadIconByType(type: TestSpeedEnum) {
        launch(Dispatchers.IO) {
            mList.run {
                when (type) {
                    TestSpeedEnum.GAME -> {
                        add(TestSpeedModel(R.mipmap.game_running, "地铁跑酷", ip = "pao.uu.cc"))
                        add(TestSpeedModel(R.mipmap.game_king, "王者荣耀", ip = "pvp.qq.com"))
                        add(TestSpeedModel(R.mipmap.game_gun, "和平精英", ip = "gp.qq.com"))
                        add(TestSpeedModel(R.mipmap.game_dream, "梦幻西游", ip = "xyq.163.com"))
                        add(TestSpeedModel(R.mipmap.game_bird, "开心消消乐", ip = "xxl.happyelements.com"))
                        add(TestSpeedModel(R.mipmap.game_shark, "捕鱼大作战", ip = "tuyoogame.com"))
                        add(TestSpeedModel(R.mipmap.game_ninja, "火柴人联盟", ip = "hcrlm.biligame.com"))
                        add(TestSpeedModel(R.mipmap.game_cat, "汤姆猫跑酷", ip = "kylinmobi.net"))
                        add(TestSpeedModel(R.mipmap.game_te, "阴阳师", ip = "yys.163.com"))
                        add(TestSpeedModel(R.mipmap.game_human, "重生细胞", ip = "game.bilibili.com"))
                        add(TestSpeedModel(R.mipmap.game_san, "三国志", ip = "sgzzlb.lingxigames.com"))
                        add(TestSpeedModel(R.mipmap.game_terraria, "泰拉瑞亚", ip = "terraria.org"))
                        add(TestSpeedModel(R.mipmap.game_cf, "穿越火线", ip = "cfm.qq.com"))
                    }
                    TestSpeedEnum.NEWS -> {
                        add(TestSpeedModel(R.mipmap.news_uc, "UC浏览器", ip = "wap.uc.cn"))
                        add(TestSpeedModel(R.mipmap.news_wy, "网易新闻", ip = "news.163.com"))
                        add(TestSpeedModel(R.mipmap.news_tt, "今日头条", ip = "toutiao.com"))
                        add(TestSpeedModel(R.mipmap.news_tencent, "腾讯新闻", ip = "news.qq.com"))
                        add(TestSpeedModel(R.mipmap.news_zh, "知乎", ip = "zhihu.com"))
                        add(TestSpeedModel(R.mipmap.news_sina, "新浪新闻", ip = "news.sina.com.cn"))
                        add(TestSpeedModel(R.mipmap.news_fm, "封面新闻", ip = "thecover.cn"))
                        add(TestSpeedModel(R.mipmap.news_bd, "百度", ip = "baidu.com"))
                        add(TestSpeedModel(R.mipmap.news_ys, "央视新闻", ip = "news.cctv.com"))
                        add(TestSpeedModel(R.mipmap.news_rm, "人民日报", ip = "paper.people.com.cn"))
                        add(TestSpeedModel(R.mipmap.news_pp, "澎湃新闻", ip = "thepaper.cn"))
                        add(TestSpeedModel(R.mipmap.news_xh, "新华社", ip = "m.xinhuanet.com"))
                        add(TestSpeedModel(R.mipmap.news_bj, "北京日报", ip = "wap.bjd.com.cn"))
                        add(TestSpeedModel(R.mipmap.news_fh, "凤凰新闻", ip = "fenghuang.rednet.cn"))
                        add(TestSpeedModel(R.mipmap.news_sh, "搜狐新闻", ip = "k.sohu.com"))
                        add(TestSpeedModel(R.mipmap.news_qc, "汽车之家", ip = "autohome.com.cn"))
                        add(TestSpeedModel(R.mipmap.news_qtt, "趣头条", ip = "qutoutiao.net"))
                        add(TestSpeedModel(R.mipmap.news_yd, "一点资讯", ip = "yidianzixun.com"))
                        add(TestSpeedModel(R.mipmap.news_kkd, "快看点", ip = "mp.yuncheapp.cn"))
                        add(TestSpeedModel(R.mipmap.news_htt, "惠头条", ip = "mp.cashtoutiao.com"))
//                        add(TestSpeedModel(R.mipmap.news_kd, "聚看点", ip = ))
                        add(TestSpeedModel(R.mipmap.news_db, "豆瓣", ip = "douban.com"))
//                        add(TestSpeedModel(R.mipmap.news_dy, "大鱼新闻", ip = ""))
                        add(TestSpeedModel(R.mipmap.news_xj, "新京报", ip = "bjnews.com.cn"))
                        add(TestSpeedModel(R.mipmap.news_nd, "南方都市报", ip = "nandu.com"))
                    }
                    TestSpeedEnum.STREAM -> {
                        add(TestSpeedModel(R.mipmap.stream_dy, "抖音", ip = "douyin.com"))
                        add(TestSpeedModel(R.mipmap.stream_ks, "快手", ip = "kuaishou.com"))
                        add(TestSpeedModel(R.mipmap.stream_xhs, "小红书", ip = "xiaohongshu.com"))
                        add(TestSpeedModel(R.mipmap.stream_ws, "微视", ip = "weishi.qq.com"))
                        add(TestSpeedModel(R.mipmap.stream_ppx, "皮皮虾", ip = "pipix.com"))
                        add(TestSpeedModel(R.mipmap.stream_pop, "糗事百科", ip = "qiushibaike.com"))
                        add(TestSpeedModel(R.mipmap.stream_mp, "美拍", ip = "meipai.com"))
                        add(TestSpeedModel(R.mipmap.stream_miaop, "秒拍", ip = "miaopai.com"))
                        add(TestSpeedModel(R.mipmap.hk, "好看视频", ip = "haokan.baidu.com"))
                        add(TestSpeedModel(R.mipmap.stream_now, "NOW直播", ip = "now.qq.com"))
                        add(TestSpeedModel(R.mipmap.stream_hj, "花椒直播", ip = "huajiao.com"))
                        add(TestSpeedModel(R.mipmap.stream_up, "UP直播", ip = "upliveapp.com"))
                        add(TestSpeedModel(R.mipmap.stream_yy, "YY", ip = "yy.com"))
                        add(TestSpeedModel(R.mipmap.stream_yk, "映客直播", ip = "www.inke.cn"))
                    }
                    TestSpeedEnum.VIDEO -> {
                        add(TestSpeedModel(R.mipmap.video_aqy, "爱奇艺", ip = "iqiyi.com"))
                        add(TestSpeedModel(R.mipmap.video_tencent, "腾讯视频", ip = "v.qq.com"))
                        add(TestSpeedModel(R.mipmap.video_youku, "优酷视频", ip = "youku.com"))
                        add(TestSpeedModel(R.mipmap.video_blbl, "哔哩哔哩", ip = "app.bilibili.com"))
                        add(TestSpeedModel(R.mipmap.video_watermelon, "西瓜视频", ip = "ixigua.com"))
                        add(TestSpeedModel(R.mipmap.video_mango, "芒果TV", ip = "www.mgtv.com"))
                        add(TestSpeedModel(R.mipmap.video_bbbs, "宝宝巴士", ip = "babybus.com"))
                        add(TestSpeedModel(R.mipmap.video_huya, "虎牙", ip = "huya.com"))
                        add(TestSpeedModel(R.mipmap.video_xl, "迅雷", ip = "xunlei.com"))
                        add(TestSpeedModel(R.mipmap.video_dy, "斗鱼", ip = "douyu.com"))
                        add(TestSpeedModel(R.mipmap.video_renren, "人人视频", ip = "rr.tv"))
                        add(TestSpeedModel(R.mipmap.video_damai, "大麦", ip = "damai.com"))
                        add(TestSpeedModel(R.mipmap.video_migu, "咪咕视频", ip = "www.miguvideo.com"))
                        add(TestSpeedModel(R.mipmap.video_mdd, "埋堆堆", ip = "mddcloud.com.cn"))
                        add(TestSpeedModel(R.mipmap.video_ls, "乐视视频", ip = "le.com"))
                        add(TestSpeedModel(R.mipmap.video_bs, "百搜视频", ip = "baishi.xiaodutv.com"))
//                        add(TestSpeedModel(R.mipmap.video_bk, "播客", ip = ""))
                        add(TestSpeedModel(R.mipmap.video_pp, "PP视频", ip = "pptv.com"))
                        add(TestSpeedModel(R.mipmap.video_dd, "多多视频", ip = "rr.tv"))
                        add(TestSpeedModel(R.mipmap.video_cb, "唱吧", ip = "changba.com"))
                        add(TestSpeedModel(R.mipmap.video_td, "糖豆", ip = "tangdou.com"))
                    }
                    TestSpeedEnum.SHOPPING -> {
                        add(TestSpeedModel(R.mipmap.sp_pdd, "拼多多", ip = "mobile.pinduoduo.com"))
                        add(TestSpeedModel(R.mipmap.sp_tb, "淘宝", ip = "m.taobao.com"))
                        add(TestSpeedModel(R.mipmap.sp_jd, "京东", ip = "jd.com"))
                        add(TestSpeedModel(R.mipmap.sp_zz, "转转", ip = "zhuanzhuan.com"))
                        add(TestSpeedModel(R.mipmap.sp_wph, "唯品会", ip = "m.vip.com"))
                        add(TestSpeedModel(R.mipmap.sp_tm, "天猫", ip = "tmall.com"))
                        add(TestSpeedModel(R.mipmap.sp_tt, "淘特", ip = "uland.taobao.com"))
                        add(TestSpeedModel(R.mipmap.sp_sam, "山姆会员", ip = "sanmsclub.com"))
                        add(TestSpeedModel(R.mipmap.sp_hlj, "婚礼纪", ip = "hunliji.com"))
                        add(TestSpeedModel(R.mipmap.sp_sn, "苏宁易购", ip = "c.m.suning.com"))
                        add(TestSpeedModel(R.mipmap.sp_jddj, "京东到家", ip = "jd.com"))
                        add(TestSpeedModel(R.mipmap.sp_ymt, "洋码头", ip = "www.ymatou.com"))
                        add(TestSpeedModel(R.mipmap.sp_xy, "闲鱼", ip = "2.taobao.com"))
                        add(TestSpeedModel(R.mipmap.sp_1688, "阿里巴巴", ip = "m.1688.com"))
                        add(TestSpeedModel(R.mipmap.sp_yt, "一淘", ip = "etao.com"))
                        add(TestSpeedModel(R.mipmap.sp_sq, "省钱快报", ip = "sqkb.com"))
                        add(TestSpeedModel(R.mipmap.sp_kl, "考拉海购", ip = "kaola.com"))
                        add(TestSpeedModel(R.mipmap.sp_dw, "得物", ip = "app.dewu.com"))
                        add(TestSpeedModel(R.mipmap.sp_dd, "当当", ip = "dangdang.com"))
                        add(TestSpeedModel(R.mipmap.sp_ws, "屈臣氏", ip = "www.watsons.com.cn"))
                        add(TestSpeedModel(R.mipmap.sp_fl, "返利网", ip = "fanli.com"))
                        add(TestSpeedModel(R.mipmap.sp_ht, "海淘免税店", ip = "haitao.top"))
                        add(TestSpeedModel(R.mipmap.sp_yj, "云集", ip = "yunjiglobal.com"))
                        add(TestSpeedModel(R.mipmap.sp_zlj, "找靓机", ip = "zhaoliangji.com"))
                        add(
                            TestSpeedModel(
                                R.mipmap.sp_yjjj,
                                "宜家家居",
                                ip = "store-companion.ikea.cn"
                            )
                        )
                        add(TestSpeedModel(R.mipmap.sp_hn, "海南免税", ip = "hainandfs.com"))
                        add(TestSpeedModel(R.mipmap.sp_my, "蜜源", ip = "sz.gzmiyuan.com"))
                        add(TestSpeedModel(R.mipmap.sp_elm, "饿了么", ip = "ele.me"))
                        add(TestSpeedModel(R.mipmap.sp_mt, "美团", ip = "meituan.net"))
                        add(TestSpeedModel(R.mipmap.sp_dp, "大众点评", ip = "dianping.com"))
                        add(TestSpeedModel(R.mipmap.sp_hm, "盒马", ip = "www.freshippo.com"))
                        add(TestSpeedModel(R.mipmap.sp_bd, "贝店", ip = "beidian.com"))
                        add(TestSpeedModel(R.mipmap.sp_klg, "快乐购", ip = "happigo.com"))
                        add(TestSpeedModel(R.mipmap.sp_ymx, "亚马逊购物", ip = "amazon.com"))
                    }
                }
            }
            withContext(Dispatchers.Main) {
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_test_speed
    }
}