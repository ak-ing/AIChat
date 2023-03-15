package com.aking.aichat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.aking.aichat.databinding.MessageBubbleErrorBinding
import com.aking.aichat.databinding.MessageBubbleLoadingBinding
import com.aking.aichat.databinding.MessageBubbleOtherBinding
import com.aking.aichat.databinding.MessageBubbleSelfBinding
import com.aking.aichat.utl.CommonViewHolder
import com.aking.openai.model.bean.GptDiffCallback
import com.aking.openai.model.bean.GptText

/**
 * Created by Rick on 2023-02-27  14:08.
 * Description:
 */
class ChatAdapter : ListAdapter<GptText, CommonViewHolder>(GptDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val bind = when (viewType) {
            GptText.GPT -> MessageBubbleOtherBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            GptText.USER -> MessageBubbleSelfBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            GptText.LOADING -> MessageBubbleLoadingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            else -> MessageBubbleErrorBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        }
        return CommonViewHolder(bind)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.adapterPosition
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }
}