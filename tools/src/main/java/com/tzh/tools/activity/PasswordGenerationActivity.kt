package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tzh.tools.R
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.util.clickDelay
import kotlin.random.Random

/**
 * 字符生成器页面
 */
class PasswordGenerationActivity : BaseLibActivity<BaseViewModel<*>>() {

    //数字
    val num = "0123456789"
    //小写字母
    val smallLetter = "abcdefghijklmnopqrstuvwxyz"
    //大写字母
    val bigLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    //特殊符号
    val specialCharacter = "!@#¥%&*()"

    companion object {
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?) {
            val intent = Intent(context, PasswordGenerationActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
            }
            context.startActivity(intent)
        }
    }

    /**
     * 默认字符长度
     */
    var length = 8

    @SuppressLint("CutPasteId")
    override fun initView() {
        val tvNum = findViewById<View>(R.id.tv_num)//数字
        val tvSmallLetter = findViewById<View>(R.id.tv_small_letter)//小写字母
        val tvBigLetter = findViewById<View>(R.id.tv_big_letter)//大写字母
        val tvSpecialCharacter = findViewById<View>(R.id.tv_special_character)//特殊符号
        //默认选中数字 大小写字母
        tvNum.isSelected = true
        tvSmallLetter.isSelected = true
        tvBigLetter.isSelected = true
        //数字选中事件
        tvNum.setOnClickListener {
            tvNum.isSelected = !tvNum.isSelected
        }
        //小写字母选中事件
        tvSmallLetter.setOnClickListener {
            tvSmallLetter.isSelected = !tvSmallLetter.isSelected
        }
        //大写字母选中事件
        tvBigLetter.setOnClickListener {
            tvBigLetter.isSelected = !tvBigLetter.isSelected
        }
        //特殊符号选中事件
        tvSpecialCharacter.setOnClickListener {
            tvSpecialCharacter.isSelected = !tvSpecialCharacter.isSelected
        }


        findViewById<TextView>(R.id.tv_title).text = "字符生成器"
        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }

        //复制字符
        findViewById<View>(R.id.iv_copy).clickDelay {
            val pwd = findViewById<TextView>(R.id.tv_psw).text.toString()
            if(TextUtils.isEmpty(pwd)){
                Toast.makeText(this,"请先生成字符",Toast.LENGTH_SHORT).show()
            }else{
                val str: ClipData = ClipData.newPlainText("Label", pwd)
                val cm: ClipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(str)
                showToast("复制成功")
            }
        }

        //生成字符
        findViewById<View>(R.id.tv_create).clickDelay {
            var passwordText = ""
            //如果选中了数字
            if(tvNum.isSelected){
                passwordText += num
            }
            //如果选中了小写字母
            if(tvSmallLetter.isSelected){
                passwordText += smallLetter
            }
            //如果选中了大写字母
            if(tvBigLetter.isSelected){
                passwordText += bigLetter
            }
            //如果选中了特殊符号
            if(tvSpecialCharacter.isSelected){
                passwordText += specialCharacter
            }
            //如果没选
            if(TextUtils.isEmpty(passwordText)){
                showToast("请选择字符组合类型")
                return@clickDelay
            }

            //字符
            var password = ""
            //循环取
            for(index in 1..length){
                val num : Int = Random.nextInt(passwordText.length - 1)
                password += passwordText.substring(num,num+1)
            }

            findViewById<TextView>(R.id.tv_psw).text = password

        }

        /**
         * 字符长度监听
         */
        findViewById<SeekBar>(R.id.seek_bar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar : SeekBar?, progress : Int, p2: Boolean) {
                length = progress
                findViewById<TextView>(R.id.tv_length).text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

    }

    override fun initDataObserver() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_password_generation
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }

}