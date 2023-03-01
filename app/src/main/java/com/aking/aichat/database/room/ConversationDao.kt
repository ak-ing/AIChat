package com.aking.aichat.database.room

import androidx.room.*
import com.aking.aichat.database.entity.ConversationEntity

/**
 * Created by Rick on 2023-03-01  17:48.
 * Description:
 */
@Dao
interface ConversationDao {
    //新增实体
    @Insert
    fun insertConversation(conversation: ConversationEntity)

    //新增多个实体
    @Insert
    fun insertConversation(conversations: List<ConversationEntity>)

    //更新数据
    @Update
    fun updateConversation(conversation: ConversationEntity)

    //删除数据
    @Delete
    fun deleteConversation(conversation: ConversationEntity)

    //编写自己的 SQL 查询(query)方法
    @Transaction
    @Query("SELECT * FROM conversations ORDER BY timestamp")
    fun getAll(): List<ConversationEntity>


    //将简单参数传递给查询
    @Query("SELECT * FROM conversations WHERE id == :id")
    fun loadById(id: Int): ConversationEntity?
}