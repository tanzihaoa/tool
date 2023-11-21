package com.tzh.tools.dialog

import android.app.Activity
import android.app.Dialog
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tzh.tools.R
import com.tzh.tools.adapter.CurrencyAdapter
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.databinding.DialogLoanTypeBinding
import com.tzh.tools.databinding.DialogMoneyBinding
import com.tzh.tools.util.clickDelay
import com.wx.wheelview.adapter.ArrayWheelAdapter
import com.wx.wheelview.widget.WheelView

object DialogUtils {

    fun showLoanTypeDialog(activity: Activity,list: List<String>,title:String,onConfirm:(select:String)->Unit={}) {
        val dialog = Dialog(activity, R.style.NewADDialogStyle)
        val dataBinding = DialogLoanTypeBinding.inflate(activity.layoutInflater)
        dataBinding.apply {
            wheelView.setWheelAdapter(ArrayWheelAdapter(activity))
            wheelView.skin= WheelView.Skin.Holo
            wheelView.setWheelData(list)
            tvDialogTitle.text=title
            val style = WheelView.WheelViewStyle()
            style.selectedTextSize = 14
            style.textSize = 13
            style.holoBorderColor = activity.resources.getColor(R.color.color_F0F0F0)
            style.textColor= activity.resources.getColor(R.color.color_BEC4D1)
            style.selectedTextColor= activity.resources.getColor(R.color.color_FF4735)
            style.textAlpha = 0.4f
            wheelView.style = style
            tvDialogCancle.clickDelay {
                dialog.dismiss()
            }
            tvDialogConfirm.clickDelay {
                onConfirm.invoke(list[wheelView.currentPosition])
                dialog.dismiss()
            }
        }

        dialog.setContentView(dataBinding.root)
        dialog.setOnShowListener {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
    fun showMoneyDialog(activity: BaseLibActivity<*>,
                        mAdapter: CurrencyAdapter,
                        click:(icon:Int,name:String,Sname:String)->Unit): Dialog {
        val dialog = Dialog(activity, R.style.DialogStyle)
        val dataBinding= DialogMoneyBinding.inflate(activity.layoutInflater)

        dataBinding.apply {
            dmCancle.setOnClickListener{
                dialog.dismiss()
            }
            dmRecy.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            dmRecy.adapter=mAdapter
        }
        mAdapter.setOnItemClickListener {_,_,position->
            click.invoke(
                mAdapter.data[position].icon,
                mAdapter.data[position].name,
                mAdapter.data[position].sName)
            dialog.dismiss()
        }

        dialog.setContentView(dataBinding.root)
        dialog.setOnShowListener {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }
}