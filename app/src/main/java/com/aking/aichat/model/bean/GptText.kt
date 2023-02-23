package com.aking.aichat.model.bean

/**
 * Created by Rick at 2023/02/23 2:10
 * @Description //TODO $
 */
data class GptText(
    val finish_reason: String,
    val index: Int,
    val logprobs: Any,
    val text: String
)