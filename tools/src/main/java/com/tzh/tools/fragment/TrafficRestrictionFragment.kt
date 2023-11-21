package com.tzh.tools.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.tzh.tools.R
import com.tzh.tools.activity.RestrictionRulesLibActivity
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.databinding.FragmentTrafficRestrictionBinding
import com.tzh.tools.model.TrafficRestrictionResult
import com.tzh.tools.net.apiLib
import com.tzh.tools.util.MMKVUtil
import com.tzh.tools.util.clickDelay

import com.google.gson.Gson
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.HashMap

/**
 * 限行
 */
class TrafficRestrictionFragment : BaseFragment<BaseViewModel<*>>() {

    private val trafficRestrictionResult: MutableLiveData<TrafficRestrictionResult> =
        MutableLiveData()

    companion object {
        private const val LIMIT_CITY_LIST = "limit_city_list"
        private const val select_limit_city = "select_limit_city"
        private const val TITLE = "title"

        fun newInstance(title: String? = null, @LayoutRes layoutResID: Int?) =
            TrafficRestrictionFragment().apply {
                arguments = Bundle().apply {
                    title?.let {
                        putString(TITLE, title)
                    }
                    layoutResID?.let {
                        putInt(KEY_LAYOUT, layoutResID)
                    }
                }
            }
    }

    var city: String? = null
    var title: String? = null

    override fun initView() {
        view?.findViewById<View>(R.id.cl_add_city)?.clickDelay {
            showSetCity(requireContext())
        }

        title = arguments?.getString(TITLE)

        view?.findViewById<View>(R.id.tv_set_city)?.clickDelay {
            showSetCity(requireContext())
//            val intent = Intent(context, RestrictionRulesActivity::class.java)
//            intent.putExtra(RestrictionRulesActivity.KEY_DATA, data)
//            requireContext().startActivity(intent)
        }
    }

    private var oneData = true
    override fun onResume() {
        super.onResume()
        if (oneData) {
            oneData = false

            city = MMKVUtil.get(select_limit_city, "") as String
            city?.let {
                getTrafficRestrictionList(it)
            }

            val dataString = MMKVUtil.get(LIMIT_CITY_LIST, "") as String
            if (TextUtils.isEmpty(dataString)) {
                startLimitCity(false)
            }
        }
    }

    private fun trafficRestriction(
        data: TrafficRestrictionResult
    ) {
        view?.setOnClickListener {
            if (data.limits.size >= 2) {
                RestrictionRulesLibActivity.startActivity(requireContext(), data, null)
            }
        }

        view?.findViewById<TextView>(R.id.tv_title)?.text = if (title == null) {
            "车辆限行"
        } else {
            title
        }

        if (data.limits.size >= 2) {
            view?.findViewById<View>(R.id.cl_traffic_deTail)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.cl_add_city)?.visibility = View.INVISIBLE

            val data1 = data.limits[0]
            val data2 = data.limits[1]
            view?.findViewById<TextView>(R.id.tv_data1)?.text = data1.date
            view?.findViewById<TextView>(R.id.tv_data2)?.text = data2.date

            val tvData2Num1 = view?.findViewById<TextView>(R.id.tv_data2_num1)


            val stringBuffer1 = StringBuffer()
            for (item in data1.plates) {
                stringBuffer1.append(",")
                stringBuffer1.append(item)
            }
            if (stringBuffer1.isEmpty()) {
                view?.findViewById<TextView>(R.id.tv_data1_num1)?.text = "不限行"
            } else {
                view?.findViewById<TextView>(R.id.tv_data1_num1)?.text =
                    stringBuffer1.substring(1).toString()
            }


            val stringBuffer2 = StringBuffer()
            for (item in data2.plates) {
                stringBuffer2.append(",")
                stringBuffer2.append(item)
            }

            if (stringBuffer2.isEmpty()) {
                tvData2Num1?.text = "不限行"
            } else {
                tvData2Num1?.text = stringBuffer2.substring(1).toString()
            }
            val tvTitle = view?.findViewById<TextView>(R.id.tv_title)
            val tvSelectCity = view?.findViewById<TextView>(R.id.tv_set_city)
            if (title == null) {
                tvTitle?.text = "车辆限行"
            } else {
                tvTitle?.text = title
            }
            tvSelectCity?.text = ""

        } else {
            val clTrafficDeTail = view?.findViewById<View>(R.id.cl_traffic_deTail)
            val clAddCity = view?.findViewById<View>(R.id.cl_add_city)
            val tvTitle = view?.findViewById<TextView>(R.id.tv_title)
            val tvSelectCity = view?.findViewById<TextView>(R.id.tv_set_city)
            clTrafficDeTail?.visibility = View.INVISIBLE
            clAddCity?.visibility = View.VISIBLE

            if (data.city.isNotEmpty()) {
//                mDataBinding.tvTitle.text = "车辆限行尾号  ${data.city}"
                if (title == null) {
                    tvTitle?.text = "车辆限行"
                } else {
                    tvTitle?.text = title
                }
                tvSelectCity?.text = "此城市无限号"

            } else {
                tvTitle?.text = "有车一族请关注限行信息"
                tvSelectCity?.text = "请选择城市"
            }


        }


    }


    private fun showSetCity(context: Context) {

        val dataString = MMKVUtil.get(LIMIT_CITY_LIST, "") as String
        if (!TextUtils.isEmpty(dataString)) {
            try {
                val list = Gson().fromJson(dataString, Array<String>::class.java)
                if (list != null && list.isNotEmpty()) {
                    showCityPicker(context, list.toMutableList())
                    return
                }
            } catch (_: Exception) {
            }
        }
        startLimitCity(true)
    }

    private fun startLimitCity(isDialog: Boolean) {
        launch {
            runCatching {
                apiLib.getLimitCity(HashMap())
            }.onSuccess {
                if (it.code == 200) {
                    if (it.data.list.isNotEmpty()) {
                        val cityList = mutableListOf<String>()
                        for (item in it.data.list) {
                            cityList.add(item.name)
                        }
                        val stringData = Gson().toJson(cityList)
                        MMKVUtil.save(LIMIT_CITY_LIST, stringData)
                        if (isDialog) showCityPicker(requireContext(), cityList)
                    }
                }
            }
        }
    }

    private var mPvOptions: OptionsPickerView<String>? = null
    private fun showCityPicker(context: Context, list: List<String>) {
        if (null != mPvOptions) {
            if (mPvOptions!!.isShowing) {
                return
            }
        }
        mPvOptions =
            OptionsPickerBuilder(context) { options1, _, _, v ->
                val sex = list.getOrNull(options1) ?: ""
                city = sex
                MMKVUtil.save(select_limit_city, sex)
                getTrafficRestrictionList(sex)
            }
                .setTitleText("选择城市")
                .setTitleBgColor(Color.parseColor("#FFFFFF"))
                .setTitleColor(context.resources.getColor(R.color.colorTheme))
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.parseColor("#999999"))
                .setTitleSize(18)
                .setSubCalSize(17)
                .setContentTextSize(18)
                .setTextColorCenter(context.resources.getColor(R.color.colorTheme))
                .setItemVisibleCount(5)
                .setLineSpacingMultiplier(3f)
                .build()
        mPvOptions?.setPicker(list)
        city?.let {
            mPvOptions?.setSelectOptions(list.indexOf(it))

        }
        mPvOptions?.show()
    }

    /**
     * 获取限行信息
     */
    private fun getTrafficRestrictionList(city: String) {
        launch {
            runCatching {
                val params = HashMap<String, String>()
                params["cityName"] = city
                apiLib.getLimitCityInfo(params)
            }.onSuccess {
                val data = it.data

                if (it.code == 200 && data != null) {
                    it.data.city = city
                    trafficRestrictionResult.postValue(data)
                } else {
                    if(it.code == 200){
                        withContext(Dispatchers.Main) {
                            ToastUtils.show("该城市暂无限行信息")
                        }
                    }else {
                        if (!TextUtils.isEmpty(it.msg)) {
                            withContext(Dispatchers.Main) {
                                ToastUtils.show(it.msg)
                            }
                        } else {
                            val result = TrafficRestrictionResult()
                            result.city = city
                            trafficRestrictionResult.postValue(result)
                        }
                    }
                }
            }
        }
//        launch({
//            val params = HashMap<String, String>()
//            params["cityName"] = city
//            execute { api.getLimitCityInfo(params) }
//
//        }, {
//            if (it is Result.Success) {
//                it.data.city = city
//                trafficRestrictionResult.postValue(it.data)
//            } else {
//                val result = TrafficRestrictionResult()
//                result.city = city
//                trafficRestrictionResult.postValue(result)
//            }
//
//        })
    }

    override fun initDataObserver() {
        trafficRestrictionResult.observe(this) {
            view?.findViewById<View>(R.id.cl_add_city)?.visibility = View.INVISIBLE
            view?.findViewById<View>(R.id.cl_traffic_deTail)?.visibility = View.VISIBLE

            view?.findViewById<View>(R.id.tv_positioning)?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.tv_positioning)?.text = it.city

            trafficRestriction(it)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_traffic_restriction
    }
}