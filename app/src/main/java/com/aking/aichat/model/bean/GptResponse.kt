package com.aking.aichat.model.bean

import androidx.core.util.Consumer
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
