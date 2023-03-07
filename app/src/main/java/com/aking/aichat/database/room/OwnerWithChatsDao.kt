package com.aking.aichat.database.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.aking.aichat.database.entity.OwnerWithChats

/**
 * Created by Rick on 2023-03-01  17:10.
 * Description: 聊天会话记录操作
 */
@Dao
interface OwnerWithChatsDao {

    //编写自己的 SQL 查询(query)方法
    @Transaction
    @Query("SELECT * FROM conversations ORDER BY timestamp")
    fun getAll(): List<OwnerWithChats>


    //将简单参数传递给查询
    @Transaction
    @Query("SELECT * FROM conversations WHERE id == :id")
    fun loadById(id: Int): OwnerWithChats?
}