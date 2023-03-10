package com.aking.openai.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aking.openai.database.entity.ChatEntity
import com.aking.openai.database.entity.ConversationEntity
import com.aking.openai.database.room.ChatDao
import com.aking.openai.database.room.ConversationDao
import com.aking.openai.database.room.OwnerWithChatsDao
import com.aking.openai.util.Constants

/**
 * Created by Rick on 2023-03-01  17:08.
 * Description: 数据库
 */
@Database(
    entities = [ConversationEntity::class, ChatEntity::class], version = 1, exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {

    companion object {
        private lateinit var db: ChatDatabase

        fun init(context: Context) {
            db = Room.databaseBuilder(context, ChatDatabase::class.java, Constants.DATABASE_CHAT)
                .build()
        }

        @JvmStatic
        val INSTANCE get() = db
    }

    abstract fun getConversationDao(): ConversationDao
    abstract fun getChatDao(): ChatDao
    abstract fun getOwnerWithChats(): OwnerWithChatsDao
}