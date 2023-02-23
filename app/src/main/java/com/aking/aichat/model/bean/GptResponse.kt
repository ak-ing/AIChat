package com.aking.aichat.model.bean

/**
 * Created by Rick at 2023/02/23 2:10
 * @Description //TODO $
 */
data class GptResponse(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)

data class Choice(
    val finish_reason: String,
    val index: Int,
    val logprobs: Any,
    val text: String
)

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)

data class GptErrorResponse(val error: Error)

data class Error(
    val code: Any,
    val message: String,
    val `param`: Any,
    val type: String
)