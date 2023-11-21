package com.tzh.tools.net



import android.annotation.SuppressLint
import com.tzh.tools.BuildConfig
import com.tzh.tools.Tools
import com.tzh.tools.util.LogUtil
import com.tzh.tools.util.NetworkUtil
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author: Created by xyc
 * created on: 2022/8/17
 * description:Http相关操作
 */
val apiLib by lazy {
    val url = if(com.tzh.tools.BuildConfig.DEBUG){
        "http://account-api.xiaochijiaoyu.cn"
    }else{
        "https://account-api-cdn.csshuqu.cn/"
    }
    RetrofitFactory.instance.create(url, ToolsApiService::class.java)
}

suspend fun <T> execute(block: suspend () -> BaseResponse<T>): ToolsResult<T>? {
    var result: ToolsResult<T>? = null
    runCatching {
        block()
    }.onSuccess { response ->
        result = if (response.code == 200) {
            ToolsResult.Success(response.data)
        } else {
            ToolsResult.Failure(response.msg)
        }
    }.onFailure {
        result = ToolsResult.Error(it)
    }
    return result
}

class RetrofitFactory private constructor() {
    private lateinit var retrofit: Retrofit
    private var builder: Retrofit.Builder

    init {
        val file = File(com.tzh.tools.Tools.app.externalCacheDir, "ok-cache")
        val cache = Cache(file, 1024 * 1024 * 30L)

        // 创建OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(
                HttpLoggingInterceptor { message -> LogUtil.e(TAG, message) }.setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )
            .addInterceptor(CacheInterceptor())
            .addInterceptor(ToolRequestInterceptor())
            .addNetworkInterceptor(CacheNetworkInterceptor())
            .cache(cache)
            .build()

        // 创建Retrofit实例
        builder = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(ToolCustomConverterFactory.create())

//        retrofit = builder.build()
    }

    class CacheNetworkInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            //无缓存，进行缓存
            val response = chain.proceed(chain.request())
            return response.newBuilder()
                .removeHeader("Pragma")
                //对请求进行最大60秒的缓存
                .addHeader("Cache-Control", "max-age=60")
                .build()
        }
    }

    class CacheInterceptor : Interceptor {
        @SuppressLint("MissingPermission")
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response
            val request: Request
            if (NetworkUtil.isConnected()) {
                //有网络，检查10秒内的缓存
                request = chain.request()
                    .newBuilder()
                    .cacheControl(
                        CacheControl.Builder()
                            .maxAge(10, TimeUnit.SECONDS)
                            .build()
                    )
                    .build()
            } else {
                //无网络，检查30天内的缓存，即使是过期的缓存
                request = chain.request()
                    .newBuilder()
                    .cacheControl(
                        CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(30, TimeUnit.DAYS)
                            .build()
                    )
                    .build()
            }
            response = chain.proceed(request)
            return response.newBuilder().build()
        }
    }

    /**
     * 根据API接口类生成API实体
     *
     * @param clazz 传入的API接口类
     * @return API实体类
     */
    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    /**
     * 根据API接口类生成API实体
     *
     * @param baseUrl baseUrl
     * @param clazz   传入的API接口类
     * @return API实体类
     */
    fun <T> create(baseUrl: String, clazz: Class<T>): T {
        return builder.baseUrl(baseUrl).build().create(clazz)
    }

    private object Holder {
        val retrofitFactory = RetrofitFactory()
    }

    companion object {
        private const val TAG = "RetrofitFactory"
        const val HTTP_CODE_REQUEST_SUCCESS = 200
        private const val TIME_OUT: Long = 10L

        val instance: RetrofitFactory = Holder.retrofitFactory

    }
}
