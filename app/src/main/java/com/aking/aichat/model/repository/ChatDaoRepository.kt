package com.aking.aichat.model.repository

import androidx.room.Room
import com.aking.aichat.database.ChatDatabase
import com.aking.aichat.database.entity.ChatEntity
import com.aking.aichat.database.entity.ConversationEntity
import com.aking.aichat.database.entity.OwnerWithChats
import com.aking.aichat.utl.Constants
import com.txznet.common.AppGlobal
import com.txznet.common.model.BaseRepository

/**
 * Created by Rick on 2023-03-01  18:00.
 * Description:
 */
class ChatDaoRepository : BaseRepository() {

    private val dao by lazy {
        Room.databaseBuilder(
            AppGlobal.context,
            ChatDatabase::class.java, Constants.DATABASE_CHAT
        ).build()
    }

    suspend fun insertChat(chatEntity: ChatEntity) {
        dao.getChatDao().loadById(chatEntity.id) ?: dao.getChatDao().insertChat(chatEntity)
    }

    suspend fun deleteChat(chatEntity: ChatEntity) {
        dao.getChatDao().deleteChat(chatEntity)
    }

    suspend fun insertConversation(conversation: ConversationEntity) {
        dao.getConversationDao().insertConversation(conversation)
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


}