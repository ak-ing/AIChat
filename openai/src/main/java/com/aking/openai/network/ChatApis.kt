package com.aking.openai.network

import com.aking.openai.model.bean.GptResponse
import com.aking.openai.model.bean.Message
import com.aking.openai.model.bean.Text
import com.aking.openai.util.Constants
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Rick at 2023/02/23 1:21
 * @Description OpenAi ChatGPT接口
 */
interface ChatApis {
    @Headers("Content-Type:application/json")
    @POST("v1/completions")
    suspend fun postRequest(
        @Body jsonData: JsonObject,
        @Header("Authorization") apikey: String = "Bearer " + Constants.getAPIKey()
    ): GptResponse<Text>

    @Headers("Content-Type:application/json")
    @POST("/v1/chat/completions")
    suspend fun postRequestTurbo(
        @Body jsonData: JsonObject,
        @Header("Authorization") apikey: String = "Bearer " + Constants.getAPIKey()
    ): GptResponse<Message>

}