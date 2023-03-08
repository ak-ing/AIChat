package com.aking.aichat.model.repository

import com.aking.aichat.model.BaseRepository
import com.aking.aichat.model.bean.GptResponse
import com.aking.aichat.model.bean.GptText
import com.aking.aichat.model.bean.Message
import com.aking.aichat.model.bean.Text
import com.aking.aichat.network.ChatApis
import com.aking.aichat.network.MessageContext
import com.aking.aichat.network.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import timber.log.Timber

/**
 * Created by Rick at 2023/02/23 0:52
 * @Description //TODO $
 */
class ChatRepository : BaseRepository() {
    private val gson = Gson()
    private val charGPTClient = RetrofitClient.getInstance().create(ChatApis::class.java)

    suspend fun postRequest(query: String): GptResponse<Text> {
        Timber.tag("postResponse").v(query)
        val request = getRequest(query)
        return executeHttp { charGPTClient.postRequest(request) }
    }

    suspend fun postRequestTurbo(query: List<MessageContext>): GptResponse<Message> {
        val request = getRequestTurbo(query)
        return executeHttp { charGPTClient.postRequestTurbo(request) }
    }

    /**
     * 获取请求体
     */
    private fun getRequest(query: String?) = JsonObject().apply {
        addProperty("model", "text-davinci-003")
        addProperty("prompt", "$query")
        addProperty("temperature", 0.6)
        addProperty("max_tokens", 1024)
        addProperty("top_p", 1)
        addProperty("frequency_penalty", 0.0)
        addProperty("presence_penalty", 0.0)
    }

    /**
     * 获取请求体
     */
    private fun getRequestTurbo(query: List<MessageContext>) = JsonObject().apply {
        addProperty("model", "gpt-3.5-turbo")
        add("messages", gson.toJsonTree(query))
        addProperty("temperature", 0.6)
        addProperty("max_tokens", 1024)
        addProperty("top_p", 1)
        addProperty("frequency_penalty", 0.0)
        addProperty("presence_penalty", 0.0)
//        messages=[
//            {"role": "system", "content": "You are a helpful assistant."},
//            {"role": "user", "content": "Who won the world series in 2020?"},
//            {"role": "assistant", "content": "The Los Angeles Dodgers won the World Series in 2020."},
//            {"role": "user", "content": "Where was it played?"}
//        ]
    }

    /**
     * 构建上下文
     */
    fun buildContext(query: String, chats: List<GptText>, count: Int = chats.size): List<MessageContext> {
        val messageContext = mutableListOf<MessageContext>()
        messageContext.add(MessageContext("system", "You are a helpful assistant."))
        chats.forEachIndexed { index, chat ->
            if (index == count) return@forEachIndexed
            val role = if (chat.viewType == GptText.USER) "user" else "assistant"
            messageContext.add(MessageContext(role, chat.text))
        }
        messageContext.add(MessageContext("user", query))
        return messageContext
    }
}