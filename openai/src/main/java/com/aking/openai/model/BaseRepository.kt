package com.aking.openai.model

import com.aking.openai.BuildConfig
import com.aking.openai.model.bean.Choices
import com.aking.openai.model.bean.GptEmptyResponse
import com.aking.openai.model.bean.GptErrorResponse
import com.aking.openai.model.bean.GptResponse
import com.aking.openai.util.ExceptionHandle
import timber.log.Timber
import java.io.Closeable

/**
 * Created by Rick on 2023-02-25  15:20.
 * Description:
 */
open class BaseRepository : Closeable {

    suspend fun <T : Choices> executeHttp(block: suspend () -> GptResponse<T>): GptResponse<T> {
        runCatching { block.invoke() }
            .onSuccess { return handleHttpOk(it) }
            .onFailure { return handleHttpError(it) }
        return GptEmptyResponse()
    }

    /**
     * 返回200，但是还要判断isSuccess
     */
    private fun <T : Choices> handleHttpOk(data: GptResponse<T>): GptResponse<T> {
        return if (data.error == null) {
            getHttpSuccessResponse(data)
        } else {
            GptErrorResponse(data.error)
        }
    }

    /**
     * 非后台返回错误，捕获到的异常
     */
    private fun <T : Choices> handleHttpError(e: Throwable): GptErrorResponse<T> {
        if (BuildConfig.DEBUG) e.printStackTrace()
        return GptErrorResponse(ExceptionHandle.handleException(e))
    }

    /**
     * 成功和数据为空的处理
     */
    private fun <T : Choices> getHttpSuccessResponse(response: GptResponse<T>): GptResponse<T> {
        val data = response.choices
        return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
            GptEmptyResponse()
        } else {
            response
        }
    }

    override fun close() {
        Timber.tag(this.javaClass.name).v("close")
    }
}