package com.aking.aichat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import com.aking.aichat.database.entity.ConversationCallback
import com.aking.aichat.database.entity.ConversationEntity
import com.aking.aichat.database.entity.OwnerWithChats
import com.aking.aichat.databinding.ItemConversationBinding
import com.aking.aichat.ui.page.HomeFragmentDirections
import com.aking.aichat.utl.CommonViewHolder
import com.aking.aichat.utl.Constants
import com.aking.aichat.utl.generateRandomName
import com.txznet.common.utils.currentSeconds

/**
 * Created by Rick on 2023-02-27  10:57.
 * Description:
 */
class ConversationAdapter(private val navController: NavController) :
    ListAdapter<OwnerWithChats, CommonViewHolder>(ConversationCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder = CommonViewHolder(
        ItemConversationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationChat(it)
            navController.navigate(action)
        }
    }
}
