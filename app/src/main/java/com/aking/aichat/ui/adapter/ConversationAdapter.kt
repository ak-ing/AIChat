package com.aking.aichat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.aking.aichat.database.entity.ConversationCallback
import com.aking.aichat.database.entity.OwnerWithChats
import com.aking.aichat.databinding.ItemConversationBinding
import com.aking.aichat.utl.CommonViewHolder

/**
 * Created by Rick on 2023-02-27  10:57.
 * Description:
 */
class ConversationAdapter(var onItemClickListener: ConversationClickListener? = null) :
    ListAdapter<OwnerWithChats, CommonViewHolder>(ConversationCallback) {

    interface ConversationClickListener {
        fun onItemClick(it: OwnerWithChats, bind: ItemConversationBinding)
        fun onDelete(it: OwnerWithChats)
        fun onMoved(it: List<OwnerWithChats>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder = CommonViewHolder(
        ItemConversationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            onItemClickListener?.onItemClick(it, holder.getBind())
        }
    }
}
