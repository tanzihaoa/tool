package com.tzh.tools.extension

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import com.tzh.tools.Tools

/**
 * @author: Created by zfj
 * created on: 2023/5/16
 * description:
 */
fun getApplicationDisplayMetrics(): DisplayMetrics {
    return com.tzh.tools.Tools.app.resources.displayMetrics
}

val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Int.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

val Float.sp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, getApplicationDisplayMetrics())

val Int.sp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), getApplicationDisplayMetrics()).toInt()

fun dp2px(dpValue: Float): Float = dpValue.dp
fun dp2px(dpValue: Int): Int = dpValue.dp

fun sp2px(spValue: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getApplicationDisplayMetrics())
fun sp2px(spValue: Int) = sp2px(spValue.toFloat()).toInt()

fun screenWidth(): Int = Resources.getSystem().displayMetrics.widthPixels
fun screenHeight(): Int = Resources.getSystem().displayMetrics.heightPixels