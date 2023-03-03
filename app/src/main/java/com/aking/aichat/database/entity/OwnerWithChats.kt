package com.aking.aichat.database.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

/**
 * Created by Rick on 2023-03-01  17:33.
 * Description: 会话
 */
data class OwnerWithChats(
    @Embedded var conversation: ConversationEntity,
    @Relation(parentColumn = "id", entityColumn = "conversationId")
    val chat: List<ChatEntity>
) : Serializable

object ConversationCallback : DiffUtil.ItemCallback<OwnerWithChats>() {
    override fun areItemsTheSame(oldItem: OwnerWithChats, newItem: OwnerWithChats): Boolean =
        oldItem.conversation.id == newItem.conversation.id

    override fun areContentsTheSame(oldItem: OwnerWithChats, newItem: OwnerWithChats): Boolean =
        oldItem == newItem
}