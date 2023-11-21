package com.tzh.tools.constants

/**
 * Created by xwm on 2021/9/10
 */
object CacheKey {
    //是否是新用户
    const val IS_NEW_USER = "is_new_user"

    //是否领取过新人红包
    const val RECEIVED_NEW_USER_PACKAGE = "received_new_user_package"

    //是否同意了隐私协议
    const val IS_ACCEPT_AGREEMENT = "is_accept_agreement"

    const val POINT_INFO_DATA = "point_info_data"

    const val WIFI_GUARD_DATA = "wifi_guard_data"

    const val INCREASE_GOLD_NUM = "increase_gold_num"

    //随机金币到达每日领取上限的时间
    const val RANDOM_GOLD_REACHED_MAX_TIME = "random_gold_reached_max_time"

    //自增浮动金币到达每日领取上限的时间
    const val INCREASE_GOLD_REACHED_MAX_TIME =
        "increase_gold_reached_max_time"

    const val LAST_SIGN_IN_TIME = "last_sign_in_time"
    const val DOUBLE_POINT_SECRET = "double_point_secret"

    const val LAST_CHECK_SECURITY_AD_TIME = "last_check_security_ad_time"
    const val LAST_SPEED_TEST_AD_TIME = "last_speed_test_ad_time"
    const val LAST_SIGNAL_ENHANCE_AD_TIME = "last_signal_enhance_ad_time"
    const val LAST_WIFI_CLIENT_AD_TIME = "last_signal_wifi_client_time"
    const val LAST_WIFI_GUARD_AD_TIME = "last_wifi_guard_ad_time"
    const val LAST_HARDWARE_OPTI_AD_TIME = "last_hardware_opti_ad_time"

    /**
     * 在别的地方开启了获取定位权限，需要进行wifi列表刷新
     */
    const val NEED_FRESH_WIFI_LIST_GRANTED = "NEED_FRESH_WIFI_LIST_GRANTED"
}