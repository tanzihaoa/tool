package com.tzh.tools.extension

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import com.tzh.tools.Tools

/**
 * @author: Created by zfj
 * created on: 2023/5/16
 * description:
 */

/**
 * 获取Color
 */
fun getResColor(@ColorRes colorId: Int) = com.tzh.tools.Tools.app.resources.getColor(colorId)

/**
 * 获取Color
 */
fun getStringColor(colorString: String?, defaultColor: Int = Color.WHITE): Int {
    return try {
        Color.parseColor(colorString)
    } catch (e: Exception) {
        return defaultColor
    }
}

/**
 * 获取Dimen
 */
fun getDimen(@DimenRes dimenId: Int) = com.tzh.tools.Tools.app.resources.getDimension(dimenId)

/**
 * 获取Drawable
 */
@SuppressLint("UseCompatLoadingForDrawables")
fun getDrawable(@DrawableRes resId: Int) = com.tzh.tools.Tools.app.resources.getDrawable(resId)