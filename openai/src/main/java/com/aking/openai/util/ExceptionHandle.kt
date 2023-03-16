package com.aking.openai.util

import com.aking.openai.model.bean.Error
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.text.ParseException


/**
 *  author : Asher@txzing.com
 *  date : 2022/6/22 10:44 上午
 *  description :  异常统一处理 ({@link })
 */

object ExceptionHandle {

    fun handleException(e: Throwable): Error {
        Timber.e("ExceptionHandle -->" + e.message.toString())
        return when (e) {
            is HttpException ->
                //协议出错
                Error(ERROR.HTTP_ERROR, e)
            is JSONException, is ParseException, is MalformedJsonException ->
                //简析错误
                Error(ERROR.PARSE_ERROR, e)
            is ConnectException ->
                //网络错误
                Error(ERROR.NETWORK_ERROR, e)
            is javax.net.ssl.SSLException ->
                //证书出错
                Error(ERROR.SSL_ERROR, e)
            is java.net.SocketTimeoutException ->
                //socket连接超时
                Error(ERROR.TIMEOUT_ERROR, e)

            is java.net.UnknownHostException ->
                //未知主机错误
                Error(ERROR.TIMEOUT_ERROR, e)
            else -> {
                //未知
                Error(ERROR.UNKNOWN, e)
            }
        }
    }
}


enum class ERROR(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, "未知错误"),

    /**
     * 解析错误
     */
    PARSE_ERROR(1001, "解析错误"),

    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, "网络错误"),

    /**
     * 协议出错
     */
    HTTP_ERROR(1003, "协议出错"),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, "证书出错"),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, "连接超时");

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}