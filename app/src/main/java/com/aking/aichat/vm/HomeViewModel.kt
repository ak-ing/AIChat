package com.aking.aichat.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aking.aichat.database.entity.OwnerWithChats
import com.aking.aichat.model.repository.DaoRepository
import com.txznet.common.vm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


/**
 * Created by Rick on 2023-03-03  11:48.
 * Description:
 */
class HomeViewModel : BaseViewModel<DaoRepository>(DaoRepository()) {

    private val _conversation = MutableLiveData<List<OwnerWithChats>>(listOf())
    val conversationLd: LiveData<List<OwnerWithChats>> get() = _conversation

    init {
        EventBus.getDefault().register(this)
        viewModelScope.launch(Dispatchers.IO) {
            _conversation.postValue(repository.getAll())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: OwnerWithChats) {
        Timber.v("onMessageEvent $event")
        _conversation.value?.toMutableList()?.let { data ->
            data.removeIf { it.conversation.id == event.conversation.id }
            data.add(0, event)
            _conversation.value = data
        }
    }

    /**
     * 删除会话
     */
    fun deleteConversation(it: OwnerWithChats) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteChats(it.chat)
        repository.deleteConversation(it.conversation)
        _conversation.postValue(_conversation.value?.toMutableList()?.apply { remove(it) })
    }

    /**
     * 刷新数据源
     */
    fun submitList(list: List<OwnerWithChats>) {
        _conversation.value = list
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
        super.onCleared()
    }
}