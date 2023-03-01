package com.aking.aichat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Rick on 2023-03-01  17:24.
 * Description:
 */
@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val avatarRes: Int,
    val timestamp: Long
) : Serializable
