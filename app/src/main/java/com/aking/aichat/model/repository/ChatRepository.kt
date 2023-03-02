package com.aking.aichat.model.repository

import com.aking.aichat.model.BaseRepository
import com.aking.aichat.model.bean.Choice
import com.aking.aichat.model.bean.GptResponse
import com.aking.aichat.network.ChatApis
import com.aking.aichat.network.RetrofitClient
import com.google.gson.JsonObject
import timber.log.Timber

/**
 * Created by Rick at 2023/02/23 0:52
 * @Description //TODO $
 */
class ChatRepository : BaseRepository() {

    private val charGPTClient = RetrofitClient.getInstance().create(ChatApis::class.java)

    suspend fun postRequest(query: String): GptResponse<Choice> {
        Timber.tag("postResponse").v(query)
        val request = getRequest(query)
        return executeHttp { charGPTClient.postRequest(request) }
    }

    /**
     * 获取请求体
     */
    private fun getRequest(query: String?) = JsonObject().apply {
//        addProperty("model", "Davinci-Codex")
        addProperty("model", "text-davinci-003")
        addProperty("prompt", "$query")
        addProperty("temperature", 0.6)
        addProperty("max_tokens", 1024)
        addProperty("top_p", 1)
        addProperty("frequency_penalty", 0.0)
        addProperty("presence_penalty", 0.0)
    }
}