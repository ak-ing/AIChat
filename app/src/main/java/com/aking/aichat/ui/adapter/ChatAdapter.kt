package com.aking.aichat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.aking.aichat.databinding.ItemChatGptBinding
import com.aking.aichat.databinding.ItemChatUserBinding
import com.aking.aichat.model.bean.GptDiffCallback
import com.aking.aichat.model.bean.GptText
import com.aking.aichat.utl.CommonViewHolder

/**
 * Created by Rick on 2023-02-27  14:08.
 * Description:
 */
class ChatAdapter : ListAdapter<GptText, CommonViewHolder>(GptDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val bind = if (viewType == GptText.GPT) {
            ItemChatGptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return CommonViewHolder(bind)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }
}