package com.aking.aichat.model

import com.aking.aichat.BuildConfig
import com.aking.aichat.model.bean.Error
import com.aking.aichat.model.bean.GptEmptyResponse
import com.aking.aichat.model.bean.GptErrorResponse
import com.aking.aichat.model.bean.GptResponse
import com.txznet.common.model.BaseRepository

/**
 * Created by Rick on 2023-02-25  15:20.
 * Description:
 */
open class BaseRepository : BaseRepository() {

    suspend fun <T> executeHttp(block: suspend () -> GptResponse<T>): GptResponse<T> {
        runCatching { block.invoke() }
            .onSuccess { return handleHttpOk(it) }
            .onFailure { return handleHttpError(it) }
        return GptEmptyResponse()
    }

    /**
     * 返回200，但是还要判断isSuccess
     */
    private fun <T> handleHttpOk(data: GptResponse<T>): GptResponse<T> {
        return if (data.error == null) {
            getHttpSuccessResponse(data)
        } else {
            GptErrorResponse(data.error)
        }
    }

    /**
     * 非后台返回错误，捕获到的异常
     */
    private fun <T> handleHttpError(e: Throwable): GptErrorResponse<T> {
        if (BuildConfig.DEBUG) e.printStackTrace()
        return GptErrorResponse(Error(0, "${e.message}", e, ""))
    }

    /**
     * 成功和数据为空的处理
     */
    private fun <T> getHttpSuccessResponse(response: GptResponse<T>): GptResponse<T> {
        val data = response.choices
        return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
            GptEmptyResponse()
        } else {
            response
        }
    }
}