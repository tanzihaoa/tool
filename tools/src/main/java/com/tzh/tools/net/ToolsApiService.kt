package com.tzh.tools.net

import com.tzh.tools.model.*
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ToolsApiService {
    // 手机号归属地查询
    @FormUrlEncoded
    @POST("https://wifi-api-cdn.csshuqu.cn/tools/getMobileInfo")
    suspend fun getMobileInfo(@FieldMap params: HashMap<String, String>): BaseResponse<PhoneNumberModel>

    // IP地址查询
    @FormUrlEncoded
    @POST("https://wifi-api-cdn.csshuqu.cn/tools/ipGetCity")
    suspend fun ipGetCity(@FieldMap params: HashMap<String, String>): BaseResponse<IpModel>

    @FormUrlEncoded
    @POST("https://report-api.csshuqu.cn/tools/postcodeQuery")
    suspend fun postCodeQuery(@FieldMap params: HashMap<String, String>): BaseResponse<ZipCodeModel>


    @FormUrlEncoded
    @POST("https://weather-api-cdn.csshuqu.cn/juhe/getLimitCity")
    suspend fun getLimitCity(@FieldMap params: HashMap<String, String>): BaseResponse<LimitCityResult>

    @FormUrlEncoded
    @POST("https://weather-api-cdn.csshuqu.cn/juhe/getLimitCityInfo")
    suspend fun getLimitCityInfo(@FieldMap params: HashMap<String, String>): BaseResponse<TrafficRestrictionResult>

    /**
     * 获取两个货币汇率
     * @param params HashMap<String, String>
     * @return BaseResponse<Any>
     */
    @FormUrlEncoded
    @POST("https://account-api-cdn.csshuqu.cn/tools/getMoneyExchangeRate")
    suspend fun getRate(@FieldMap params: HashMap<String, String>): BaseResponse<RateBean>

    /**
     * 货币列表
     */
    @FormUrlEncoded
    @POST("http://account-api.xiaochijiaoyu.cn/tools/getMoneyTypeList")
    suspend fun getRateList(@FieldMap params: HashMap<String, String>): BaseResponse<RateListBean>


    @FormUrlEncoded
    @POST("https://report-api.csshuqu.cn/tools/todayOilPrice")
    suspend fun todayOilPrice(@FieldMap params: HashMap<String, String>): BaseResponse<GasPriceBean>

    @FormUrlEncoded
    @POST("https://report-api.csshuqu.cn/tools/charConvert")
    suspend fun charConvert(@FieldMap params: HashMap<String, String>): BaseResponse<TranslateBean>

    @FormUrlEncoded
    @POST("https://report-api.csshuqu.cn/tools/birthdayPassword")
    suspend fun birthdayPassword(@FieldMap params: HashMap<String, String>): BaseResponse<BirthdayPasswordBean>

    @FormUrlEncoded
    @POST("https://weather-api-cdn.csshuqu.cn/juhe/todayInHistory")
    suspend fun todayInHistory(@FieldMap params: HashMap<String, String>): BaseResponse<ArrayList<TodayInHistoryBean>>

    @FormUrlEncoded
    @POST("https://weather-api-cdn.csshuqu.cn/calendar/latelyFestival")
    suspend fun latelyFestival(@FieldMap params: HashMap<String, String>): BaseResponse<LatelyFestivalResult>

    @FormUrlEncoded
    @POST("https://weather-api-cdn.csshuqu.cn/juhe/getYearHoliday")
    suspend fun getYearHoliday(@FieldMap params: HashMap<String, String>): BaseResponse<YearHolidayResult>

    @FormUrlEncoded
    @POST("https://report-api.csshuqu.cn/tools/randJoke")
    suspend fun getJoke(@FieldMap params: HashMap<String, String>): BaseResponse<JokeResult>
}