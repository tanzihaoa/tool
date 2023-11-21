package com.tzh.tools.net

/**
 * Created by xwm on 2020/5/22
 */
sealed class ToolsResult<out T> {

    data class Success<out T>(val data: T) : ToolsResult<T>()
    data class Failure(val errorMsg: String) : ToolsResult<Nothing>()
    data class Error(val t: Throwable) : ToolsResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data = $data]"
            is Failure -> "Failure[errorMsg = $errorMsg]"
            is Error -> "Error[throwable = $t]"
        }
    }
}