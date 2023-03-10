package com.aking.openai.model.repository

import com.aking.openai.database.ChatDatabase
import com.aking.openai.database.entity.ChatEntity
import com.aking.openai.database.entity.ConversationEntity
import com.aking.openai.database.entity.OwnerWithChats
import java.io.Closeable

/**
 * Created by Rick on 2023-03-01  18:00.
 * Description:
 */
class DaoRepository : Closeable {

    private val dao = ChatDatabase.INSTANCE

    suspend fun insertChat(chatEntity: ChatEntity) {
        dao.getChatDao().loadById(chatEntity.id) ?: dao.getChatDao().insertChat(chatEntity)
    }

    suspend fun deleteChat(chatEntity: ChatEntity) {
        dao.getChatDao().deleteChat(chatEntity)
    }

    suspend fun deleteChats(chats: List<ChatEntity>) {
        dao.getChatDao().deleteChats(chats)
    }

    suspend fun insertConversation(conversationEntity: ConversationEntity) {
        dao.getConversationDao().insertConversation(conversationEntity)
    }

    suspend fun updateConversation(conversationEntity: ConversationEntity) {
        dao.getConversationDao().updateConversation(conversationEntity)
    }

    suspend fun deleteConversation(conversation: ConversationEntity) {
        dao.getConversationDao().deleteConversation(conversation)
    }

    suspend fun getAll(): List<OwnerWithChats> {
        return dao.getOwnerWithChats().getAll()
    }

    suspend fun loadById(id: Int): OwnerWithChats? {
        return dao.getOwnerWithChats().loadById(id)
    }

    override fun close() {

    }


}