package com.aking.aichat.model.bean

import androidx.core.util.Consumer
import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

/**
 * Created by Rick at 2023/02/23 2:10
 * @Description //TODO $
 */
open class GptResponse<T>(
    val choices: List<T> = arrayListOf(),
    val usage: Usage? = null,
    open val error: Error? = null,
    val created: Int? = null,
    val id: String? = null,
    val model: String? = null,
    val `object`: String? = null
) : Serializable {
    val isSuccess = false

    data class Usage(
        val completion_tokens: Int,
        val prompt_tokens: Int,
        val total_tokens: Int
    )

    fun onSuccess(block: Consumer<GptResponse<T>>): GptResponse<T> {
        if (this !is GptErrorResponse) block.accept(this)
        return this
    }

    fun onError(block: Consumer<GptResponse<T>>): GptResponse<T> {
        if (this is GptErrorResponse) block.accept(this)
        return this
    }

    fun isNotEmpty() = isSuccess

    override fun toString(): String {
        return "GptResponse(choices=$choices, usage=$usage, error=$error, created=$created, id=$id, model=$model, `object`=$`object`, isSuccess=$isSuccess)"
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
        if (isSuccess != other.isSuccess) return false

        return true
    }

    object GptDiffCallback : DiffUtil.ItemCallback<GptResponse<Choice>>() {
        override fun areItemsTheSame(oldItem: GptResponse<Choice>, newItem: GptResponse<Choice>): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GptResponse<Choice>, newItem: GptResponse<Choice>): Boolean =
            oldItem == newItem
    }
}

class GptEmptyResponse<T> : GptResponse<T>()
data class GptErrorResponse<T>(override val error: Error?) : GptResponse<T>()

data class Choice(
    val finish_reason: String,
    val index: Int,
    val logprobs: Any,
    val text: String
)

data class Error(
    val code: Any,
    val message: String,
    val `param`: Any,
    val type: String
)
