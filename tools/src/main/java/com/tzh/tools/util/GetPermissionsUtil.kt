package com.tzh.tools.util

import android.Manifest
import android.content.Context
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils

object GetPermissionsUtil {

    /**
     * 判断权限(读写录音)
     *
     * @return
     */
    fun hasPer(context: Context): Boolean {
        val p = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
        val b: Boolean = XXPermissions.isGranted(context, p)
        if (!b) {
//            ToastUtils.show("请授予存储和录音权限");
//            recordPer();
        }
        return b
    }

    /**
     * 获取权限(读写录音)
     */
    fun getPer(context: Context, confirm: () -> Unit, cancle: () -> Unit) {
        val p = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
        XXPermissions.with(context)
            .permission(*p)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, all: Boolean) {
                    //成功
                    confirm()
                }

                override fun onDenied(permissions: List<String>, never: Boolean) {
                    ToastUtils.show("请授予存储和录音权限")
                    cancle()
                    return
                }
            })
    }

    /**
     * 判断权限(READ_PHONE_STATE)
     *
     * @return
     */
    fun hasPerReadPhone(context: Context): Boolean {
        val p = arrayOf(
            Manifest.permission.READ_PHONE_STATE
        )
        val b: Boolean = XXPermissions.isGranted(context, p)
        if (!b) {
//            ToastUtils.show("请授予存储和录音权限");
//            recordPer();
        }
        return b
    }

    /**
     * 获取权限(读写录音)
     */
    fun getPerReadPhone(context: Context, finish: () -> Unit) {
        val p = arrayOf(
            Manifest.permission.READ_PHONE_STATE
        )
        XXPermissions.with(context)
            .permission(*p)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, all: Boolean) {
                    //成功
                }

                override fun onDenied(permissions: List<String>, never: Boolean) {
                    ToastUtils.show("请授予存储和录音权限")
                    finish()
                    return
                }
            })
    }
}