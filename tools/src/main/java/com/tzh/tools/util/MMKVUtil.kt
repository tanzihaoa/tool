package com.tzh.tools.util

import com.tencent.mmkv.MMKV

object MMKVUtil {

    private val mmkv by lazy {
        MMKV.defaultMMKV()
    }

    fun save(key: String, value: Any) {
        when (value) {
            is String -> {
                mmkv.encode(key, value)
            }
            is Int -> {
                mmkv.encode(key, value)
            }
            is Boolean -> {
                mmkv.encode(key, value)
            }
            is Long -> {
                mmkv.encode(key, value)
            }
        }
    }

    fun get(key: String, defaultValue: Any = ""): Any? {
        return when (defaultValue) {
            is String -> {
                mmkv.decodeString(key, defaultValue)
            }
            is Int -> {
                mmkv.decodeInt(key, defaultValue)
            }
            is Boolean -> {
                mmkv.decodeBool(key, defaultValue)
            }
            is Long -> {
                mmkv.decodeLong(key, defaultValue)
            }
            else -> null
        }
    }

    fun delete(key: String) {
        mmkv.remove(key)
    }
}


