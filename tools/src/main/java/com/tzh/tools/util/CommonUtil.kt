package com.tzh.tools.util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tzh.tools.R
import com.tzh.tools.model.PermissionTypeEnum
import com.didichuxing.doraemonkit.util.ScreenUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by xwm on 2021/7/19
 */
object CommonUtil {


    fun getCPUAbi(): String {
        val os_cpuabi = BufferedReader(
            InputStreamReader(
                Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream()
            )
        ).readLine()
        return if (os_cpuabi.contains("x86")) {
            "x86"
        } else if (os_cpuabi.contains("armeabi-v7a")) {
            "armeabi-v7a"
        } else if (os_cpuabi.contains("arm64-v8a")) {
            "arm64-v8a"
        } else {
            "armeabi"
        }
    }

    fun getSystemStartupTime(): String {
        val time = SystemClock.elapsedRealtime()
        val currentTime = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val d1 = Date(currentTime - time)
        return format.format(d1);
    }

    fun showNoiseTipsDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_noise_tips, null)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.transparent
                )
            )
        )
        dialog.window?.setDimAmount(0f)
        dialog.setContentView(inflate)
        dialog.show()

        val attributes = dialog.window?.attributes
        attributes?.gravity = Gravity.TOP
        attributes?.width = ScreenUtils.getScreenWidth() - 60
        attributes?.windowAnimations = R.style.MyDialogAnimationCenter
        dialog.window?.attributes = attributes
        return dialog
    }

    fun getNeverNoiseDialog(context: Context, onGranted: () -> Unit, onDenied: () -> Unit) {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_never_noise_agranted, null)
        inflate.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            onGranted.invoke()
            dialog.dismiss()
        }
        inflate.findViewById<TextView>(R.id.tv_denied).setOnClickListener {
            onDenied.invoke()
            dialog.dismiss()
        }
        dialog.setContentView(inflate)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun showPermissionTipsDialog(context: Context, permissionTypeEnum: PermissionTypeEnum): Dialog {
        when (permissionTypeEnum) {
            PermissionTypeEnum.PHONE_STATUS -> {
                val dialog = Dialog(context)
                val inflate =
                    LayoutInflater.from(context).inflate(R.layout.dialog_permission_tips, null)
                dialog.window?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.transparent
                        )
                    )
                )
                dialog.window?.setDimAmount(0f)
                dialog.setContentView(inflate)
                dialog.show()

                val attributes = dialog.window?.attributes
                attributes?.gravity = Gravity.TOP
                attributes?.width = ScreenUtils.getScreenWidth() - 60
                attributes?.windowAnimations = R.style.MyDialogAnimationCenter
                dialog.window?.attributes = attributes
                return dialog
            }
            else -> {
                val dialog = Dialog(context)
                val inflate =
                    LayoutInflater.from(context).inflate(R.layout.dialog_permission_tips, null)
                inflate.findViewById<TextView>(R.id.tv_permission_name).text = "位置信息权限"
                inflate.findViewById<TextView>(R.id.tv_permission_description).text = "检测WiFi信息权限"
                dialog.window?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.transparent
                        )
                    )
                )
                dialog.window?.setDimAmount(0f)
                dialog.setContentView(inflate)
                dialog.show()

                val attributes = dialog.window?.attributes
                attributes?.gravity = Gravity.TOP
                attributes?.width = ScreenUtils.getScreenWidth() - 60
                attributes?.windowAnimations = R.style.MyDialogAnimationCenter
                dialog.window?.attributes = attributes
                return dialog
            }
        }


    }

    fun getNeverDialog(context: Context, onGranted: () -> Unit, onDenied: () -> Unit) {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_never_agranted, null)
        inflate.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            onGranted.invoke()
            dialog.dismiss()
        }
        inflate.findViewById<TextView>(R.id.tv_denied).setOnClickListener {
            onDenied.invoke()
            dialog.dismiss()
        }
        dialog.setContentView(inflate)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun getTipsDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_tips, null)
        inflate.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(inflate)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
    }

}