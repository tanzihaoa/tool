package com.tzh.tools.base

import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.ViewDataBinding

abstract class BaseAdActivity<VM : BaseViewModel<*>, DB : ViewDataBinding>: BaseActivity<VM, DB>() {
//    #AEB6C3
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        isSecondActivity(true)
        super.onCreate(savedInstanceState)
    }


    override fun showInsertAd() {
        super.showInsertAd()
    }

    open fun fromBack(){

    }



}