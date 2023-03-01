package com.aking.aichat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aking.aichat.database.entity.ChatEntity
import com.aking.aichat.database.entity.ConversationEntity
import com.aking.aichat.database.room.ChatDao
import com.aking.aichat.database.room.ConversationDao
import com.aking.aichat.database.room.OwnerWithChatsDao

/**
 * Created by Rick on 2023-03-01  17:08.
 * Description: 数据库
 */
@Database(entities = [ConversationEntity::class, ChatEntity::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun getConversationDao(): ConversationDao
    abstract fun getChatDao(): ChatDao
    abstract fun getOwnerWithChats(): OwnerWithChatsDao
}