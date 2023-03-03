package com.aking.aichat.database.room

import androidx.room.*
import com.aking.aichat.database.entity.ChatEntity

/**
 * Created by Rick on 2023-03-01  17:48.
 * Description:
 */
@Dao
interface ChatDao {
    //新增实体
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chatEntity: ChatEntity)

    //新增多个实体
    @Insert
    fun insertChat(chatEntitys: List<ChatEntity>)

    //更新数据
    @Update
    fun updateChat(chatEntity: ChatEntity)

    //删除数据
    @Delete
    fun deleteChat(chatEntity: ChatEntity)

    //编写自己的 SQL 查询(query)方法
    @Transaction
    @Query("SELECT * FROM chats ORDER BY created")
    fun getAll(): List<ChatEntity>


    //将简单参数传递给查询
    @Transaction
    @Query("SELECT * FROM chats WHERE id == :id")
    fun loadById(id: String): ChatEntity?
}