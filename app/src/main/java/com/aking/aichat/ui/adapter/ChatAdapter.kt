package com.aking.aichat.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.aking.aichat.databinding.ItemChatGptBinding
import com.aking.aichat.model.bean.Choice
import com.aking.aichat.model.bean.GptResponse
import com.aking.aichat.utl.CommonViewHolder

/**
 * Created by Rick on 2023-02-27  14:08.
 * Description:
 */
class ChatAdapter : ListAdapter<GptResponse<Choice>, CommonViewHolder>(GptResponse.GptDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return CommonViewHolder(
            ItemChatGptBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        Log.e("TAG", "onBindViewHolder: ${getItem(position).id}", )
        holder.bind(getItem(position))
    }
}