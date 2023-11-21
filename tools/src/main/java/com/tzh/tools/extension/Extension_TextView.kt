package com.tzh.tools.extension

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * @author: Created by zfj
 * created on: 2023/5/16
 * description:TextView扩展
 */
/**
 * 清除文字的图片
 */
fun TextView.setDrawableNull() {
    setCompoundDrawables(null, null, null, null)
}

/**
 * 中划线
 * @receiver TextView
 */
fun TextView.strikeThruTextFlag() {
    paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
}

/**
 * 设置文字的左边图片
 */
fun TextView.setDrawableLeft(@DrawableRes redId: Int) {
    ContextCompat.getDrawable(context, redId)?.let {
        it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
        setCompoundDrawables(it, null, null, null)
    }
}

/**
 * 设置文字的上边图片
 */
fun TextView.setDrawableTop(@DrawableRes redId: Int) {
    ContextCompat.getDrawable(context, redId)?.let {
        it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
        setCompoundDrawables(null, it, null, null)
    }
}

/**
 * 设置文字的右边图片
 */
fun TextView.setDrawableRight(@DrawableRes redId: Int) {
    ContextCompat.getDrawable(context, redId)?.let {
        it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
        setCompoundDrawables(null, null, it, null)
    }
}

/**
 * 设置文字的下边图片
 */
fun TextView.setDrawableBottom(@DrawableRes redId: Int) {
    ContextCompat.getDrawable(context, redId)?.let {
        it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
        setCompoundDrawables(null, null, null, it)
    }
}

/**
 * 轻度加粗
 * 注意： 不要和textStyle = "bold" 混合使用
 *
 * @param stroke
 */
fun TextView.lightBold(stroke: Float = 1f) {
    paint.strokeWidth = stroke
    paint.style = Paint.Style.FILL_AND_STROKE
}

fun TextView.textStyleNormal() {
    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
}

fun TextView.textStyleBold() {
    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
}