package com.tzh.tools.fragment

import android.content.ClipboardManager
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.tzh.tools.R
import com.tzh.tools.base.BaseFragment
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.vm.CalendarFragmentViewModel
import com.hjq.toast.ToastUtils
import java.math.BigDecimal


class MonetaryUnitFragment : BaseFragment<BaseViewModel<*>>() {


    private val DFT_SCALE = 2

    /** 大写数字  */
    private val NUMBERS = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")

    /** 整数部分的单位  */
    private val IUNIT = arrayOf(
        "元",
        "拾",
        "佰",
        "仟",
        "万",
        "拾",
        "佰",
        "仟",
        "亿",
        "拾",
        "佰",
        "仟",
        "万",
        "拾",
        "佰",
        "仟"
    )

    /** 小数部分的单位  */
    private val DUNIT = arrayOf("角", "分", "厘")

    companion object {

//        @JvmStatic
//        fun newInstance(@LayoutRes layoutResID: Int?) =
//            MonetaryUnitFragment().apply {
//                arguments = Bundle().apply {
//                    layoutResID?.let {
//                        putInt(KEY_LAYOUT, layoutResID)
//                    }
//                }
//            }

        @JvmStatic
        fun newInstance() = MonetaryUnitFragment().apply {}
    }

    override fun initView() {
        val editText = view?.findViewById<EditText>(R.id.edittext)
        val textView = view?.findViewById<TextView>(R.id.tv_result)
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isEmpty()) {
                        textView?.text = ""
                        return@let
                    }
                    textView?.text = toChinese(s.toString())
                }
            }

        })

        view?.findViewById<View>(R.id.tv_empty)?.setOnClickListener {
            editText?.setText("")
            textView?.text = ""
        }
        view?.findViewById<View>(R.id.tv_copy)?.setOnClickListener {
            if (textView?.text.isNullOrEmpty()){
                ToastUtils.show("请输入内容")
                return@setOnClickListener
            }
            context?.let { it1 -> copyToClipboard(it1, textView?.text.toString()) };
        }
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_monet
    }

    /**
     * 得到大写金额。
     */
    fun toChinese(str: String): String? {
        var str = str
        str = str.replace(",".toRegex(), "") // 去掉","
        var integerStr: String // 整数部分数字
        val decimalStr: String // 小数部分数字

        // 初始化：分离整数部分和小数部分
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."))
            decimalStr = str.substring(str.indexOf(".") + 1)
        } else if (str.indexOf(".") == 0) {
            integerStr = ""
            decimalStr = str.substring(1)
        } else {
            integerStr = str
            decimalStr = ""
        }
        // integerStr去掉首0，不必去掉decimalStr的尾0(超出部分舍去)
        if (integerStr != "") {
            integerStr = java.lang.Long.toString(integerStr.toLong())
            if (integerStr == "0") {
                integerStr = ""
            }
        }
        // overflow超出处理能力，直接返回
        if (integerStr.length > IUNIT.size) {
            println("$str:超出处理能力")
            return str
        }
        val integers: IntArray = toArray(integerStr)!! // 整数部分数字
        val isMust5: Boolean = isMust5(integerStr) // 设置万单位
        val decimals: IntArray = toArray(decimalStr)!! // 小数部分数字
        return (getChineseInteger(integers, isMust5)
                + getChineseDecimal(decimals))
    }

    /**
     * 整数部分和小数部分转换为数组，从高位至低位
     */
    private fun toArray(number: String): IntArray? {
        val array = IntArray(number.length)
        for (i in 0 until number.length) {
            array[i] = number.substring(i, i + 1).toInt()
        }
        return array
    }

    /**
     * 得到中文金额的整数部分。
     */
    private fun getChineseInteger(integers: IntArray, isMust5: Boolean): String? {
        val chineseInteger = StringBuffer("")
        val length = integers.size
        for (i in 0 until length) {
            // 0出现在关键位置：1234(万)5678(亿)9012(万)3456(元)
            // 特殊情况：10(拾元、壹拾元、壹拾万元、拾万元)
            var key = ""
            if (integers[i] == 0) {
                if (length - i == 13) // 万(亿)(必填)
                    key = IUNIT[4] else if (length - i == 9) // 亿(必填)
                    key = IUNIT[8] else if (length - i == 5 && isMust5) // 万(不必填)
                    key = IUNIT[4] else if (length - i == 1) // 元(必填)
                    key = IUNIT[0]
                // 0遇非0时补零，不包含最后一位
                if (length - i > 1 && integers[i + 1] != 0) key += NUMBERS[0]
            }
            chineseInteger.append(if (integers[i] == 0) key else NUMBERS[integers[i]] + IUNIT[length - i - 1])
        }
        return chineseInteger.toString()
    }

    /**
     * 得到中文金额的小数部分。
     */
    private fun getChineseDecimal(decimals: IntArray): String? {
        val chineseDecimal = StringBuffer("")
        for (i in decimals.indices) {
            // 舍去3位小数之后的
            if (i == 3) break
            chineseDecimal.append(if (decimals[i] == 0) "" else NUMBERS[decimals[i]] + DUNIT[i])
        }
        return chineseDecimal.toString()
    }

    /**
     * 判断第5位数字的单位"万"是否应加。
     */
    private fun isMust5(integerStr: String): Boolean {
        val length = integerStr.length
        return if (length > 4) {
            var subInteger = ""
            subInteger = if (length > 8) { // TODO 12-9-17
                // 取得从低位数，第5到第8位的字串
                integerStr.substring(length - 8, length - 4)
            } else {
                integerStr.substring(0, length - 4)
            }
            subInteger.toInt() > 0
        } else {
            false
        }
    }

    /**
     * BigDecimal 相乘,四舍五入保留0位
     *
     * @param a
     * @param b
     * @return a*b
     */
    fun mutiply(a: String?, b: String?, roundingMode: Int): BigDecimal? {
        val bd = BigDecimal(a)
        return bd.multiply(BigDecimal(b)).setScale(DFT_SCALE, roundingMode)
    }

    /**
     * BigDecimal 相除,四舍五入保留两位
     *
     * @param a
     * @param b
     * @return a/b
     */
    fun div(a: String?, b: String?, roundingMode: Int): BigDecimal? {
        val decimal1 = BigDecimal(a)
        val decimal2 = BigDecimal(b)
        return decimal1.divide(decimal2, DFT_SCALE, roundingMode)
    }

    /**
     * BigDecimal 相加,四舍五入保留两位
     *
     * @param a
     * @param b
     * @return a+b
     */
    fun sum(a: String?, b: String?, roundingMode: Int): BigDecimal? {
        val decimal1 = BigDecimal(a)
        val decimal2 = BigDecimal(b)
        // DecimalFormat format = new DecimalFormat("#0.00");
        return decimal1.add(decimal2).setScale(DFT_SCALE, roundingMode)
    }

    /**
     * BigDecimal 相减,四舍五入保留两位
     *
     * @param a
     * @param b
     * @return a+b
     */
    fun sub(a: String?, b: String?, roundingMode: Int): BigDecimal? {
        val decimal1 = BigDecimal(a)
        val decimal2 = BigDecimal(b)
        // DecimalFormat format = new DecimalFormat("#0.00");
        return decimal1.subtract(decimal2).setScale(DFT_SCALE, roundingMode)
    }

    /**
     * 100.00 为10000
     *
     * @param a
     * @return
     */
    fun format(a: String?, roundingMode: Int): BigDecimal? {
        return BigDecimal(a).multiply(BigDecimal(100)).setScale(
            0,
            roundingMode
        )
    }

    // 将文本复制到剪切板
    fun copyToClipboard(context: Context, content: String?) {
        // 从 API11 开始 android 推荐使用 android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的 android.text.ClipboardManager，虽然提示 deprecated，但不影响使用。
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 将文本内容放到系统剪贴板里。
        cm.text = content
        Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show()
    }


}