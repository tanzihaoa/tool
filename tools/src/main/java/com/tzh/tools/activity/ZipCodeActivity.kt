package com.tzh.tools.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.allen.library.shape.ShapeTextView
import com.tzh.tools.R
import com.tzh.tools.ad.LibAdBridge
import com.tzh.tools.base.BaseLibActivity
import com.tzh.tools.base.BaseViewModel
import com.tzh.tools.net.apiLib
import com.tzh.tools.util.clickDelay
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

/**
 * Description:
 * @Author: LYL
 * @CreateDate: 2023/5/30 13:17
 */
class ZipCodeActivity : BaseLibActivity<BaseViewModel<*>>() {

    private var isAdResume = false
    override fun onResume() {
        super.onResume()
        if(!isAdResume){
            isAdResume = true
            LibAdBridge.instance.startInterstitial(this)
        }
    }
    companion object {
        const val Is_Bar_Dark = "isBarDark"
        fun startActivity(context: Context, @LayoutRes layoutResID: Int?,isBarDark:Boolean = false) {
            var intent = Intent(context, ZipCodeActivity::class.java)
            layoutResID?.let {
                intent.putExtra(KEY_LAYOUT, layoutResID)
                intent.putExtra(Is_Bar_Dark,isBarDark)
            }
            context.startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        if(intent.getBooleanExtra(Is_Bar_Dark,false)){
            ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init()
        }

        findViewById<TextView>(R.id.tv_title).text = "邮编查询"

        findViewById<ImageView>(R.id.iv_back).clickDelay {
            finish()
        }

        val etZipContent = findViewById<EditText>(R.id.et_zip_content)
        val tvZipQuery = findViewById<TextView>(R.id.tv_zip_query)
        val cvZipResult = findViewById<CardView>(R.id.cv_zip_result)
        val tvZipCode = findViewById<TextView>(R.id.tv_zip_code)
        val tvZipAddress = findViewById<TextView>(R.id.tv_zip_address)
        val stvZipCopy = findViewById<ShapeTextView>(R.id.stv_zip_copy)

        if(cvZipResult.isVisible){
            cvZipResult.visibility = View.GONE
        }

        tvZipQuery.clickDelay(500) {
            /*val suffixPattern = Regex("^[1-9]\\d{5}\$")
            if (!suffixPattern.matches(tvZipQuery.text.toString())) {
                showToast("请确认是否输入正确邮政编码")
                return@clickDelay
            }*/

            launch(Dispatchers.IO) {
                runCatching {
                    val params = HashMap<String, String>()
                    params["postcode"] = etZipContent.text.toString()
                    apiLib.postCodeQuery(params)
                }.onSuccess {
                    if (it.code == 200) {

                        withContext(Dispatchers.Main) {

                            if(!it.data.list.isNullOrEmpty()) {

                                cvZipResult.visibility = View.VISIBLE

                                tvZipCode.text = it.data.list[0].PostNumber

                                tvZipAddress.text =
                                        "${it.data.list[0].Province} " +
                                        "${it.data.list[0].City} " +
                                        "${it.data.list[0].District} " +
                                        it.data.list[0].Address
                            }else{
                                showToast("邮政编码查询失败")
                            }
                        }
                    } else {
                        showToast(it.msg)
                    }
                }.onFailure {
                    showToast("邮政编码查询失败")
                }
            }
        }

        stvZipCopy.clickDelay(500) {
            var str: ClipData = ClipData.newPlainText("Label", tvZipAddress.text)
            var cm: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(str)

            showToast("复制成功")
        }
    }

    override fun initDataObserver() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_zip_code
    }

    override fun getViewModel(): Class<BaseViewModel<*>> {
        return BaseViewModel::class.java
    }
}