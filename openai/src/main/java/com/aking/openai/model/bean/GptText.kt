package com.aking.openai.model.bean

import androidx.annotation.IntDef
import androidx.recyclerview.widget.DiffUtil
import com.aking.openai.util.currentSeconds

/**
 * Created by Rick on 2023-03-01  11:02.
 * Description:
 */
data class GptText(
    val id: String,
    val created: Int = 0,
    val model: String? = null,
    val index: Int = 0,
    val text: String,
    @ViewType val viewType: Int
) {
    @IntDef(GPT, USER, LOADING, ERROR)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ViewType

    companion object {
        const val GPT = 0
        const val USER = 1
        const val LOADING = 2
        const val ERROR = 3

        @JvmStatic
        fun createGPT(gptResponse: GptResponse<out Choices>): GptText = GptText(
            gptResponse.id,
            gptResponse.created,
            gptResponse.model,
            gptResponse.getIndex(),
            gptResponse.getText(),
            GPT
        )

        @JvmStatic
        fun createUSER(query: String): GptText = GptText(
            currentSeconds().toString(), currentSeconds().toInt(), text = query, viewType = USER
        )

        @JvmStatic
        fun createEROOR(error: String): GptText = GptText(
            currentSeconds().toString(), currentSeconds().toInt(), text = error, viewType = ERROR
        )

        val loading = GptText(
            "2", currentSeconds().toInt(), text = "思考中...", viewType = LOADING
        )
    }

}

object GptDiffCallback : DiffUtil.ItemCallback<GptText>() {
    override fun areItemsTheSame(oldItem: GptText, newItem: GptText): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GptText, newItem: GptText): Boolean =
        oldItem == newItem
}