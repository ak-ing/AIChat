package com.aking.aichat.ui.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aking.aichat.ui.adapter.ConversationAdapter

/**
 * Created by Rick on 2023-03-08  14:47.
 * Description:
 */
class ConversationTouchHelper(private val adapter: ConversationAdapter) :
    ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        val list = adapter.currentList.toMutableList()
        val pre = list.removeAt(fromPos)
        list.add(toPos, pre)
        adapter.onItemClickListener?.onMoved(list)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemClickListener?.onDelete(adapter.currentList[viewHolder.adapterPosition])
    }
}