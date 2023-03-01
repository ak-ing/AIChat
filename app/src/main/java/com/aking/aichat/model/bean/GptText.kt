package com.aking.aichat.model.bean

import androidx.annotation.IntDef
import androidx.recyclerview.widget.DiffUtil
import com.txznet.common.utils.currentSeconds

/**
 * Created by Rick on 2023-03-01  11:02.
 * Description:
 */
data class GptText(
    val id: String,
    val created: Int? = 0,
    val model: String? = null,
    val index: Int = 0,
    val text: String,
    @ViewType
    val viewType: Int
) {
    @IntDef(GPT, USER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ViewType

    companion object {
        const val GPT = 0
        const val USER = 1

        @JvmStatic
        fun createGPT(gptResponse: GptResponse<Choice>): GptText = GptText(
            gptResponse.id,
            gptResponse.created,
            gptResponse.model,
            gptResponse.choices[0].index,
            if (gptResponse.isNotEmpty()) gptResponse.choices[0].text.trimStart() else "",
            GPT
        )

        @JvmStatic
        fun createUSER(query: String): GptText = GptText(
            query.hashCode().toString(),
            currentSeconds().toInt(),
            text = query,
            viewType = USER
        )
    }

}

object GptDiffCallback : DiffUtil.ItemCallback<GptText>() {
    override fun areItemsTheSame(oldItem: GptText, newItem: GptText): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: GptText, newItem: GptText): Boolean = oldItem == newItem
}