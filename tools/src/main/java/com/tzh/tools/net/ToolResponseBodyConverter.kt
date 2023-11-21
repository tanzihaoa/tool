package com.tzh.tools.net

import android.text.TextUtils
import com.tzh.tools.constants.Constants
import com.tzh.tools.util.AESUtil
import com.tzh.tools.util.LogUtil
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.io.StringReader

/**
 * Created by xwm on 2021/7/16
 */
class ToolResponseBodyConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) :
    Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        var json = value.string()
        try {
            val aesJson = com.tzh.tools.util.AESUtil.decrypt(json, Constants.AES_KEY_BILL)
            if(!TextUtils.isEmpty(aesJson))json = aesJson
            LogUtil.e("工具类请求----->$json")
        } catch (ee:Exception){
            ee.printStackTrace()
        }
        val jsonReader = gson.newJsonReader(StringReader(json))
        return try {
            adapter.read(jsonReader)
        } finally {
            value.close()
            jsonReader.close()
        }
    }
}