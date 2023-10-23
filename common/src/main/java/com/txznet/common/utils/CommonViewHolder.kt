package com.txznet.common.utils

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.txznet.common.BR
import kotlin.properties.Delegates

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

/**
 * Created by Rick on 2023-08-04  16:46.<p>
 *
 * Description: 生成默认通用BR.item
 */
class BRItem<T>() : BaseObservable() {
    @get:Bindable
    var item: T by Delegates.observable(item) { _, _, _ ->
        notifyPropertyChanged(BR.item)
    }
}