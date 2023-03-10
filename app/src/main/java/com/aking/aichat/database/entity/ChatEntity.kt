package com.aking.aichat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aking.aichat.model.bean.GptText
import java.io.Serializable

/**
 * Created by Rick at 2023/02/23 1:43
 * @Description  聊天记录，属于会话
 */
@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val id: String,
    val conversationId: Int = 0,
    val created: Int = 0,
    val model: String? = null,
    val index: Int,
    val text: String,
    @GptText.ViewType
    val viewType: Int
) : Serializable {
    companion object {
        @JvmStatic
        fun create(gptText: GptText, conversationID: Int): ChatEntity {
            gptText.also {
                return ChatEntity(
                    it.id,
                    conversationID,
                    it.created,
                    it.model,
                    it.index,
                    it.text,
                    it.viewType
                )
            }
        }

        @JvmStatic
        fun ChatEntity.toGptText() = GptText(id, created, model, index, text, viewType)
    }
}