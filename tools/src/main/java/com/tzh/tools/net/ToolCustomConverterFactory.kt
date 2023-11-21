package com.tzh.tools.net

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by xwm on 2021/7/16
 */
class ToolCustomConverterFactory private constructor(val gson: Gson) : Converter.Factory() {

    companion object {
        fun create(): ToolCustomConverterFactory {
            return ToolCustomConverterFactory(Gson())
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return ToolResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return ToolRequestBodyConverter(gson, adapter)
    }
}