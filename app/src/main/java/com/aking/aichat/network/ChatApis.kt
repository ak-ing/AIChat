package com.aking.aichat.network

import com.aking.aichat.model.bean.GptResponse
import com.google.gson.JsonObject
import retrofit2.http.Body
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
    suspend fun postRequest(@Body jsonData: JsonObject): GptResponse
}