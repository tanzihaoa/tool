package com.tzh.tools.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tzh.tools.R
import com.tzh.tools.base.BaseRepository

import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.model.MoneyModel
import com.tzh.tools.model.RateBean
import com.tzh.tools.net.ToolsResult
import com.tzh.tools.net.apiLib
import com.tzh.tools.net.execute
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch


class CurrencyActivityViewModel : BaseViewModel<BaseRepository<*>>() {

    val mList = mutableListOf<MoneyModel>()
    val exchangeRateLiveData: MutableLiveData<RateBean> = MutableLiveData()

    fun getRate(fromCode: String, toCode: String) {
        launch({
            val params = hashMapOf<String, String>()
            params["fromCode"] = fromCode
            params["toCode"] = toCode
            execute {
                apiLib.getRate(params)
            }
        }, {
            if (it is ToolsResult.Success) {
                val data = it.data
                exchangeRateLiveData.value = data
            }else if(it is ToolsResult.Error || it is ToolsResult.Failure){
                ToastUtils.show("网络错误，请重试")
            }
        })
    }

    fun getCurrencyList() {
        viewModelScope.launch{
            mList.run {
                add(MoneyModel(R.drawable.m_cny, "人民币", "CNY"))
                add(MoneyModel(R.drawable.m_usd, "美元", "USD"))
                add(MoneyModel(R.drawable.m_jpy, "日元", "JPY"))
                add(MoneyModel(R.drawable.m_eur, "欧元", "EUR"))
                add(MoneyModel(R.drawable.m_gbp, "英镑", "GBP"))
                add(MoneyModel(R.drawable.m_krw, "韩元", "KRW"))
                add(MoneyModel(R.drawable.m_hkd, "港元", "HKD"))
                add(MoneyModel(R.drawable.m_aud, "澳元", "AUD"))
                add(MoneyModel(R.drawable.m_cad, "加元", "CAD"))
                add(MoneyModel(R.drawable.m_aed, "阿联酋拉姆", "AED"))
                add(MoneyModel(R.drawable.m_mop, "澳门元", "MOP"))
                add(MoneyModel(R.drawable.m_dzd, "阿尔吉尼亚第纳尔", "DZD"))
                add(MoneyModel(R.drawable.m_omr, "阿曼里亚尔", "OMR"))
                add(MoneyModel(R.drawable.m_egp, "埃及镑", "EGP"))
                add(MoneyModel(R.drawable.m_ars, "阿根提比索", "ARS"))
                add(MoneyModel(R.drawable.m_byr, "白俄罗斯卢布", "BYR"))
                add(MoneyModel(R.drawable.m_brl, "巴西雷亚尔", "BRL"))
                add(MoneyModel(R.drawable.m_pln, "波兰兹罗提", "PLN"))
                add(MoneyModel(R.drawable.m_bhd, "巴林第纳尔", "BHD"))
                add(MoneyModel(R.drawable.m_bgn, "保加利亚列弗", "BGN"))
                add(MoneyModel(R.drawable.m_isk, "冰岛克朗", "ISK"))
                add(MoneyModel(R.drawable.m_dkk, "丹麦克朗", "DKK"))
                add(MoneyModel(R.drawable.m_rub, "俄罗斯卢布", "RUB"))
//                add(MoneyModel(R.drawable.m_fjd, "斐济元", "FJD"))
                add(MoneyModel(R.drawable.m_php, "菲律宾比索", "PHP"))
                add(MoneyModel(R.drawable.m_cop, "哥伦比亚比索", "COP"))
//                add(MoneyModel(R.drawable.m_crc, "哥斯达黎加科朗", "CRC"))
//                add(MoneyModel(R.drawable.m_htg, "海地古徳", "HTG"))
                add(MoneyModel(R.drawable.m_kzt, "哈萨克斯坦坚戈", "KZT"))
                add(MoneyModel(R.drawable.m_czk, "捷克克朗", "CZK"))
//                add(MoneyModel(R.drawable.m_khr, "柬埔寨瑞尔", "KHR"))
//                add(MoneyModel(R.drawable.m_gnf, "几内亚法郎", "GNF"))
                add(MoneyModel(R.drawable.m_hrk, "克罗地亚库纳", "HRK"))
                add(MoneyModel(R.drawable.m_qar, "卡塔尔里亚尔", "QAR"))
                add(MoneyModel(R.drawable.m_kwd, "科威特第纳尔", "QAR"))
                add(MoneyModel(R.drawable.m_kwd, "肯尼亚先令", "KWD"))
                add(MoneyModel(R.drawable.m_lak, "老挝基普", "LAK"))
                add(MoneyModel(R.drawable.m_ron, "罗马尼亚列伊", "RON"))
                add(MoneyModel(R.drawable.m_lbp, "黎巴嫩镑", "LBP"))
//                add(MoneyModel(R.drawable.m_rwf, "卢旺达法郎", "RWF"))
//                add(MoneyModel(R.drawable.m_chn, "离岸人民币", "CNH"))
                add(MoneyModel(R.drawable.m_buk, "缅甸元", "BUK"))
                add(MoneyModel(R.drawable.m_myr, "马来西亚林吉特", "MYR"))
                add(MoneyModel(R.drawable.m_mad, "摩洛哥道拉姆", "MAD"))
                add(MoneyModel(R.drawable.m_mxd, "墨西哥元", "MXN"))
                add(MoneyModel(R.drawable.m_nok, "挪威克朗", "NOK"))
                add(MoneyModel(R.drawable.m_zar, "南非兰特", "ZAR"))
                add(MoneyModel(R.drawable.m_chf, "瑞士法郎", "CHF"))
                add(MoneyModel(R.drawable.m_sek, "瑞典克朗", "SEK"))
                add(MoneyModel(R.drawable.m_sar, "沙特里亚尔", "SAR"))
                add(MoneyModel(R.drawable.m_lkr, "斯里兰卡卢比", "LKR"))
//                add(MoneyModel(R.drawable.m_rsd, "塞尔维亚第纳尔", "RSD"))
                add(MoneyModel(R.drawable.m_thb, "泰铢", "THB"))
                add(MoneyModel(R.drawable.m_tzs, "坦桑尼亚先令", "TZS"))
//                add(MoneyModel(R.drawable.m_bnd, "文莱元", "BND"))
//                add(MoneyModel(R.drawable.m_ugx, "乌干达先令", "UGX"))
                add(MoneyModel(R.drawable.m_uyu, "乌拉圭比索", "UYU"))
//                add(MoneyModel(R.drawable.m_zmk, "新的赞比亚克瓦查", "ZMK"))
//                add(MoneyModel(R.drawable.m_syp, "叙利亚镑", "SYP"))
                add(MoneyModel(R.drawable.m_nzd, "新西兰元", "NZD"))
                add(MoneyModel(R.drawable.m_try, "新土耳其里拉", "TRY"))
                add(MoneyModel(R.drawable.m_sgd, "新加坡元", "SGD"))
                add(MoneyModel(R.drawable.m_twd, "新台币", "TWD"))
                add(MoneyModel(R.drawable.m_huf, "匈牙利福林", "HUF"))
                add(MoneyModel(R.drawable.m_jod, "约旦第纳尔", "JOD"))
//                add(MoneyModel(R.drawable.m_iqd, "伊拉克第纳尔", "IQD"))
                add(MoneyModel(R.drawable.m_vnd, "越南盾", "VND"))
                add(MoneyModel(R.drawable.m_ils, "以色列新锡克尔", "ILS"))
                add(MoneyModel(R.drawable.m_inr, "印度卢比", "INR"))
                add(MoneyModel(R.drawable.m_idr, "印尼卢比", "IDR"))
                add(MoneyModel(R.drawable.m_clp, "智利比索", "CLP"))
            }
        }
    }
}
