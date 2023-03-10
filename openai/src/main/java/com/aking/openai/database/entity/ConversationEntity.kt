package com.aking.openai.database.entity

import androidx.annotation.DrawableRes
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
    @DrawableRes
    val avatarRes: Int,
    var timestamp: Long,
    var endMessage: String = ""
) : Serializable
