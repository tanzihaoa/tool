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
import com.tzh.tools.base.BaseRepository
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.vm.CalendarFragmentViewModel
import com.hjq.toast.ToastUtils


class NumberTransitionFragment : BaseFragment<BaseViewModel<*>>() {

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
        fun newInstance() = NumberTransitionFragment().apply {}
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
                    textView?.text = ch2Num(s.toString()).toString()
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
        return R.layout.fragment_number_transition
    }

    /**
     * 中文数字转阿拉伯数字
     * (长度不能超过long最大值)
     * @param chNum 中文数字
     * @return 阿拉伯数字
     */
    fun ch2Num(chNum: String?): Long {
        var chNum = chNum
        val numLen = intArrayOf(16, 8, 4, 3, 2, 1) //对应下面单位后面多少个零
        val dw = arrayOf("兆", "亿", "万", "千", "百", "十") //中文单位
        val dw1 = arrayOf("兆", "亿", "萬", "仟", "佰", "拾") //中文单位另一版
        val sz = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十") //中文数字
        val sz1 = arrayOf("〇", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾") //中文数字另一版
        if (chNum == null) return 0 //空对象返回0
        for (i in sz.indices) { //统一文字版本
            if (i < dw.size) chNum = chNum!!.replace(dw1[i].toRegex(), dw[i])
            chNum = chNum!!.replace(sz1[i].toRegex(), sz[i])
        }
        chNum = chNum!!.replace("(百.)\\b".toRegex(), "$1十") //正则替换为了匹配中文类似二百五这样的词
        if (chNum.length == 1) {
            for (i in sz.indices) {
                if (chNum == sz[i]) return i.toLong()
            }
            return 0 //中文数字没有这个字
        }
        chNum = strReverse(chNum) //调转输入的字符串
        for (i in dw.indices) {
            if (chNum?.contains(dw[i]) == true) {
                val part = chNum?.split(dw[i].toRegex(), limit = 2)?.toTypedArray() //把字符串分割2部分
                val num1 = ch2Num(strReverse(part!![1]))
                val num2 = ch2Num(strReverse(part!![0]))
                return ((if (num1 == 0L) 1 else num1) * Math.pow(
                    10.0,
                    numLen[i].toDouble()
                ) + num2).toLong()
            }
        }
        val c = chNum?.toCharArray()
        var sum: Long = 0
        for (i in c?.indices!!) { //一个个解析数字
            val tem = c[i].toString() //根据索引转成对应数字
            sum += (ch2Num(tem) * Math.pow(10.0, i.toDouble())).toLong() //根据位置给定数字
        }
        return sum
    }

    //字符串掉转
    private fun strReverse(str: String): String? {
        return StringBuilder(str).reverse().toString()
    }

    /**
     * @param num 人民币阿拉伯数字
     * @return 中文大写人民币
     */
    fun upperRMB(num: String): String? {
        val dw = charArrayOf(
            '圆', '拾', '佰', '仟', '萬', '拾', '佰', '仟', '亿', '拾', '佰',
            '仟', '萬', '拾', '佰', '仟', '兆', '拾', '佰', '仟', '萬', '拾', '佰',
            '仟', '亿', '拾', '佰', '仟', '萬', '拾', '佰', '仟'
        ) //单位
        val sz = charArrayOf('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖') //中文数字
        val s = num.toCharArray() //数字转成字符数组
        val sb = java.lang.StringBuilder() //创建字符串生成器
        var index = 0
        for (i in s.indices.reversed()) {
            sb.append("" + dw[index++] + sz[s[i].code - 48]) //倒着插入中文数字和单位
        }
        var str = sb.reverse().toString() //字符串反转
        var lastStr: String
        do { //语法调整
            lastStr = str
            str = str.replace("零[零拾佰仟]".toRegex(), "零")
            str = str.replace("零([萬亿兆])".toRegex(), "$1零")
            str = str.replace("亿萬".toRegex(), "亿")
            str = str.replace("兆[萬万]".toRegex(), "兆")
            str = str.replace("零圆".toRegex(), "圆")
        } while (lastStr != str)
        return str
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