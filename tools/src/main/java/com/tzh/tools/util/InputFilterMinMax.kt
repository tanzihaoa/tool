package com.tzh.tools.util

import android.text.InputFilter
import android.text.Spanned


class InputFilterMinMax : InputFilter {
    private var min: Float
    private var max: Float

    constructor(min: Float, max: Float) {
        this.min = min
        this.max = max
    }

//    constructor(min: Int, max: Int) {
//        this.min = min
//        this.max = max
//    }

//    constructor(min: String?, max: String?) {
//        this.min = java.lang.Float.valueOf(min)
//        this.max = java.lang.Float.valueOf(max)
//    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            //限制小数点位数
            if (source == "." && dest.toString().isEmpty()) {
                return "0."
            }
            if (dest.toString().contains(".")) {
                val index = dest.toString().indexOf(".")
                val mlength = dest.toString().substring(index).length
                if (mlength == 5) {
                    return ""
                }
            }
            //限制大小
            val input = java.lang.Float.valueOf(dest.toString() + source.toString())
            if (isInRange(min, max, input)) {
                return null
            }
        } catch (nfe: Exception) {
        }
        return ""
    }

    private fun isInRange(a: Float, b: Float, c: Float): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}