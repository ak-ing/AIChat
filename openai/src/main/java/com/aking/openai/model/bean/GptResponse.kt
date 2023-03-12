package com.aking.openai.model.bean

import com.aking.openai.network.MessageContext
import java.io.Serializable

/**
 * Created by Rick at 2023/02/23 2:10
 * @Description //TODO $
 */
open class GptResponse<T : Choices>(
    val choices: List<T> = arrayListOf(),
    val usage: Usage? = null,
    open val error: Error? = null,
    val created: Int = 0,
    val id: String = "",
    val model: String? = null,
    val `object`: String? = null,
    var ownerID: Int = 0
) : Serializable {
    data class Usage(
        val completion_tokens: Int, val prompt_tokens: Int, val total_tokens: Int
    )

    suspend fun onSuccess(block: suspend (GptResponse<T>) -> Unit): GptResponse<T> {
        if (this !is GptErrorResponse) block.invoke(this)
        return this
    }

    suspend fun onError(block: suspend (GptResponse<T>) -> Unit): GptResponse<T> {
        if (this is GptErrorResponse) block.invoke(this)
        return this
    }

    fun isNotEmpty() = choices.isNotEmpty()

    fun getText(): String = error?.message ?: choices[0].getContent().trimStart()

    fun getIndex(): Int = if (error != null) -1 else choices[0].index

    override fun toString(): String {
        return "GptResponse(choices=$choices, usage=$usage, error=$error, created=$created, id=$id, model=$model, `object`=$`object`)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GptResponse<*>

        if (choices != other.choices) return false
        if (usage != other.usage) return false
        if (error != other.error) return false
        if (created != other.created) return false
        if (id != other.id) return false
        if (model != other.model) return false
        if (`object` != other.`object`) return false

        return true
    }

    override fun hashCode(): Int {
        var result = choices.hashCode()
        result = 31 * result + (usage?.hashCode() ?: 0)
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + (created ?: 0)
        result = 31 * result + id.hashCode()
        result = 31 * result + (model?.hashCode() ?: 0)
        result = 31 * result + (`object`?.hashCode() ?: 0)
        return result
    }
}

class GptEmptyResponse<T : Choices> : GptResponse<T>()
data class GptErrorResponse<T : Choices>(override val error: Error?) : GptResponse<T>()
data class Error(
    val code: Any, val message: String, val `param`: Any, val type: String
)

interface Choices {
    val index: Int
    val finish_reason: String
    fun getContent(): String
}

data class Text(
    override val finish_reason: String, override val index: Int, val logprobs: Any, val text: String
) : Choices {
    override fun getContent(): String {
        return text
    }
}

data class Message(
    override val finish_reason: String, override val index: Int, val message: MessageContext
) : Choices {
    override fun getContent(): String {
        return message.content
    }
}
