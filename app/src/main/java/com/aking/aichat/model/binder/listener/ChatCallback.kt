package com.aking.aichat.model.binder.listener

import android.os.IInterface
import com.aking.openai.database.entity.OwnerWithChats
import com.aking.openai.model.bean.GptResponse
import com.aking.openai.model.bean.Message

/**
 * Created by Rick at 2023/3/10 23:36.
 * @Description:
 */
interface ChatCallback : IInterface {
    fun onAIReplies(response: GptResponse<Message>) {}
    fun onNotifyConversation(owner: OwnerWithChats) {}
    override fun asBinder() = null
}