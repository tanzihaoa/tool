package com.tzh.tools.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.view.WheelView
import com.tzh.tools.R
import com.tzh.tools.util.MMKVUtil
import com.tzh.tools.util.clickDelay


class SelectBirthdayDialogFragment : DialogFragment() {

    private fun originDayList(): ArrayList<String> {
        return arrayListOf(
            "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24", "25",
            "26", "27", "28", "29", "30", "31"
        )
    }

    private fun originMonthList(): ArrayList<String> {
        return arrayListOf(
            "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12"
        )
    }

    private var onOk: (() -> Unit)? = null
    fun setonOk(onOk: (() -> Unit)) {
        this.onOk = onOk
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.ActivityTheme)
        val inflate = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_fragment_select_birthday, null)

        inflate.findViewById<TextView>(R.id.tv_cancels).clickDelay { dialog.dismiss() }

        val month = inflate.findViewById<WheelView>(R.id.wv_month)
        val days = inflate.findViewById<WheelView>(R.id.wv_day)

        inflate.findViewById<TextView>(R.id.tv_submit).clickDelay {
            MMKVUtil.save(
                "birthday",
                "${originMonthList()[month.currentItem]}-${originDayList()[days.currentItem]}"
            )
            onOk?.invoke()
            dismiss()
        }

        month.setLabel("月")
        days.setLabel("日")

        month.setItemsVisibleCount(3)
        days.setItemsVisibleCount(3)

        days.adapter = ArrayWheelAdapter(originDayList())
        month.adapter = ArrayWheelAdapter(originMonthList())

        days.setTextColorCenter(Color.parseColor("#FF4735"))
        month.setTextColorCenter(Color.parseColor("#FF4735"))
        days.setDividerColor(Color.parseColor("#9CA7BE"))
        month.setDividerColor(Color.parseColor("#9CA7BE"))
        days.setDividerType(WheelView.DividerType.WRAP)
        month.setDividerType(WheelView.DividerType.WRAP)
        dialog.setContentView(inflate)

        dialog.window?.setBackgroundDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.shape_permission_tips, null)
        )
        dialog.window?.setDimAmount(0.8f)

        val attributes = dialog.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = LayoutParams.WRAP_CONTENT
        attributes?.height = LayoutParams.WRAP_CONTENT
        attributes?.windowAnimations = R.style.MyDialogAnimationCenter
        dialog.window?.attributes = attributes

        return dialog
    }

}