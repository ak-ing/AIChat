package com.aking.aichat.database.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Rick on 2023-03-01  17:33.
 * Description:
 */
data class OwnerWithChats(
    @Embedded val conversationEntity: ConversationEntity,
    @Relation(parentColumn = "id", entityColumn = "conversationId")
    val chats: List<ChatEntity>
)