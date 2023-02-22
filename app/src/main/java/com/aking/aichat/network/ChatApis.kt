package com.aking.aichat.network

import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Rick at 2023/02/23 1:21
 * @Description OpenAi ChatGPT接口
 */
interface ChatApis {
    @Headers(
        "Content-Type:application/json",
        "Authorization:Bearer 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"
    )
    @POST("v1/completions")
    suspend fun postRequest()
}