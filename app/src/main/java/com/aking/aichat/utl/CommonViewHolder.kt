package com.aking.aichat.utl

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.aking.aichat.BR

/**
 * Created by Rick on 2023-01-03  18:07.
 * Description: 通用ViewHolder
 */
class CommonViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    fun <T> bind(item: T, itemClick: ((T) -> Unit)? = null) {
        binding.run {
            setVariable(BR.item, item)
            executePendingBindings()

            itemClick?.let {
                root.setOnClickListener { _ ->
                    it.invoke(item)
                }
            }
        }
    }

    /**
     * 获取ViewDataBinding
     */
    inline fun <reified T : ViewDataBinding> getBind(): T {
        return binding as T
    }

}