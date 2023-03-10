package com.aking.aichat.model.repository

import com.aking.aichat.model.binder.ChatManager
import com.aking.aichat.model.binder.listener.ChatCallback
import com.aking.openai.database.entity.OwnerWithChats
import com.aking.openai.model.bean.GptText
import com.aking.openai.network.MessageContext
import com.txznet.common.model.BaseRepository

/**
 * Created by Rick at 2023/3/11 0:59.
 * @Description:
 */
class DataRepository : BaseRepository() {
    private lateinit var mChatCallback: ChatCallback

    fun setChatListener(chatCallback: ChatCallback) {
        mChatCallback = chatCallback
        ChatManager.instant.registerCallback(mChatCallback)
    }

    fun postRequestTurbo(messages: List<MessageContext>, owner: OwnerWithChats) {
        ChatManager.instant.postRequestTurbo(messages, owner)
    }

    /**
     * 构建上下文
     */
    fun buildContext(
        query: String, chats: List<GptText>, count: Int = chats.size
    ): List<MessageContext> {
        val messageContext = mutableListOf<MessageContext>()
        messageContext.add(
            MessageContext(
                "system", "You are a helpful assistant."
            )
        )
        chats.forEachIndexed { index, chat ->
            if (index == count) return@forEachIndexed
            val role = if (chat.viewType == GptText.USER) "user" else "assistant"
            messageContext.add(MessageContext(role, chat.text))
        }
        messageContext.add(MessageContext("user", query))
        return messageContext
    }

    override fun close() {
        super.close()
        ChatManager.instant.unregisterCallback(mChatCallback)
    }
}