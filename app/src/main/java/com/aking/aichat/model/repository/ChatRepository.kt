package com.aking.aichat.model.repository

import com.aking.aichat.model.bean.GptResponse
import com.aking.aichat.network.ChatApis
import com.aking.aichat.network.RetrofitClient
import com.google.gson.JsonObject
import com.txznet.common.model.BaseRepository

/**
 * Created by Rick at 2023/02/23 0:52
 * @Description //TODO $
 */
class ChatRepository : BaseRepository() {

    private val charGPTClient = RetrofitClient.getInstance().create(ChatApis::class.java)

    suspend fun postResponse(jsonData: JsonObject): GptResponse = charGPTClient.postRequest(jsonData)

}