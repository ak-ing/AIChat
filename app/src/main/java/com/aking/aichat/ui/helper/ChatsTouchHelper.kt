package com.aking.aichat.ui.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aking.aichat.ui.adapter.ChatAdapter
import com.aking.aichat.utl.VibratorManager

/**
 * Created by Rick on 2023-03-08  14:47.
 * Description:
 */
class ChatsTouchHelper(private val adapter: ChatAdapter) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemClickListener?.onDelete(adapter.currentList[viewHolder.adapterPosition])
        VibratorManager.vibrator()
    }
}