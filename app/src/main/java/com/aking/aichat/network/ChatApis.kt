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