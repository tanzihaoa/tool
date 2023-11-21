package com.tzh.tools.customview.dashboard

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.tzh.tools.R
import com.tzh.tools.util.LogUtil

/**
 * @author: Created by xyc
 * created on: 2023/03/23
 * description: 自定义View弧形
 */
@SuppressLint("SetTextI18n")
class ArcProgressView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    companion object {
        private const val START_ANGEL = 235
        private const val END_ANGEL = 465
    }

    private var view: View? = null
    private val Float.sp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            context.resources.displayMetrics
        )

    private fun getResDrawable(resId: Int): Drawable {
        return context.resources.getDrawable(resId)
    }


    /**
     * 当前旋转的角度
     */
    private var currentAngel = -1F

    private var desc = ""

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressView)
        desc = typedArray.getString(R.styleable.ArcProgressView_desc) ?: ""
        val backgroundBg = typedArray.getDrawable(R.styleable.ArcProgressView_background_bg)
            ?: getResDrawable(R.drawable.icon_test_net_bg)
        val indicatorBg = typedArray.getDrawable(R.styleable.ArcProgressView_indicator_bg)
            ?: getResDrawable(R.drawable.icon_test_net_indicator)
        val startAngle = typedArray.getFloat(R.styleable.ArcProgressView_startAngle, 0F)

        view = LayoutInflater.from(context).inflate(R.layout.custom_view_arc_progress, this, true)

        view?.findViewById<AppCompatImageView>(R.id.ivBg)?.background = backgroundBg
        view?.findViewById<AppCompatImageView>(R.id.ivIndicator)?.background = indicatorBg
        setAngle(startAngle)
        typedArray.recycle()
    }

    /**
     * 设置速度
     * @param speed Int 角度
     */
    fun setAngle(speed: Float) {
        //图片刻度只有100
        val speedTemp: Float = if (speed <= 0) {
            0F
        } else if (speed >= 100) {
            100F
        } else {
            speed
        }
        //计算需要旋转的角度
        val allAngel: Int = END_ANGEL - START_ANGEL
        val everyItem: Float = allAngel / 10F
//        speedTemp = 0.2
//        ---------
//        x = everyItem
        val angel: Float = when (speedTemp) {
            in (0.0..0.2) -> {
                everyItem * 0 + (speedTemp - 0F) * everyItem / (0.2F - 0.0F)
            }
            in (0.2..0.5) -> {
                everyItem * 1 + (speedTemp - 0.2F) * everyItem / (0.5F - 0.2F)
            }
            in (0.5..1.0) -> {
                everyItem * 2 + (speedTemp - 0.5F) * everyItem / (1.0F - 0.5F)
            }
            in (1.0..2.0) -> {
                everyItem * 3 + (speedTemp - 1.0F) * everyItem / (2.0F - 1.0F)
            }
            in (2.0..5.0) -> {
                everyItem * 4 + (speedTemp - 2.0F) * everyItem / (5.0F - 2.0F)
            }
            in (5.0..10.0) -> {
                everyItem * 5 + (speedTemp - 5.0F) * everyItem / (10.0F - 5.0F)
            }
            in (10.0..20.0) -> {
                everyItem * 6 + (speedTemp - 10.0F) * everyItem / (20.0F - 10.0F)
            }
            in (20.0..40.0) -> {
                everyItem * 7 + (speedTemp - 20.0F) * everyItem / (40.0F - 20.0F)
            }
            in (40.0..60.0) -> {
                everyItem * 8 + (speedTemp - 40.0F) * everyItem / (60.0F - 40.0F)
            }
            else -> {
                everyItem * 9 + (speedTemp - 60.0F) * everyItem / (100.0F - 60.0F)
            }
        }
        //如果有动画立即停止
        view?.findViewById<View>(R.id.ivIndicator)?.animation?.cancel()
        //计算真正旋转的角度
        val start: Float = currentAngel
        val end: Float = START_ANGEL + angel
        LogUtil.d("start = $start , end = $end")
        val animation =
            ObjectAnimator.ofFloat(findViewById(R.id.ivIndicator), "rotation", start, end)
        animation.duration = if (currentAngel == -1F) 0 else 500
        animation.addUpdateListener {
            currentAngel = it.animatedValue as Float
        }
        animation.start()
    }

}