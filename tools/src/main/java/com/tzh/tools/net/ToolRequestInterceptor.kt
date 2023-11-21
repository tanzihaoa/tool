package com.tzh.tools.net


import com.tzh.tools.Tools
import com.tzh.tools.constants.Constants
import com.tzh.tools.util.AESUtil
import com.tzh.tools.util.LogUtil
import com.google.gson.Gson
import okhttp3.*

/**
 * @author: Created by xyc
 * created on: 2022/8/1
 * description:请求拦截器
 */
class ToolRequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestOrigin: Request = chain.request()
        if ("POST" == requestOrigin.method()) {
            val formBody = requestOrigin.body()
            if (formBody is FormBody) {
                val bodyBuilder = FormBody.Builder()
                val hashParam = HashMap<String, String>()

                val url = requestOrigin.url().url().toString()

                for (i in 0 until formBody.size()) {
                    val key = formBody.encodedName(i)
                    val value = formBody.value(i)
                    if (key == "doublePointSecret" || key == "errorMsg") {
                        hashParam[key] = value
                    } else {
                        if (url.contains("report-api.csshuqu.cn")) {
                            hashParam[key] = value
                        } else {
                            val aes = com.tzh.tools.util.AESUtil.encrypt(value, Constants.AES_KEY_BILL)
                            hashParam[key] = aes
                            LogUtil.e("工具加密key----->${key}   ------->$aes")
                        }
                    }
                    bodyBuilder.add(formBody.name(i), formBody.value(i))
                }

                hashParam.apply {

                    put("token", com.tzh.tools.Tools.token)
                    put("version", com.tzh.tools.Tools.version)
                    put("channel", com.tzh.tools.Tools.channel)
                    put("projectId", com.tzh.tools.Tools.projectId)
                    put("appClient", com.tzh.tools.Tools.appClient)
                }
                val content = Gson().toJson(hashParam)
                val requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), content)
                val request = requestOrigin.newBuilder().post(requestBody).build()
                return chain.proceed(request)
            }
        }
        return chain.proceed(requestOrigin)
    }

}