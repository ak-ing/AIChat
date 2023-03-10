package com.aking.aichat.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aking.aichat.model.binder.ChatManager
import com.aking.aichat.model.binder.listener.ChatCallback
import com.aking.openai.database.entity.OwnerWithChats
import com.aking.openai.model.repository.DaoRepository
import com.txznet.common.vm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Created by Rick on 2023-03-03  11:48.
 * Description:
 */
class HomeViewModel : BaseViewModel<DaoRepository>(DaoRepository()), ChatCallback {

    private val _conversation = MutableLiveData<List<OwnerWithChats>>(listOf())
    val conversationLd: LiveData<List<OwnerWithChats>> get() = _conversation

    init {
        ChatManager.instant.registerCallback(this)
        viewModelScope.launch(Dispatchers.IO) {
            _conversation.postValue(repository.getAll())
        }
    }

    override fun onNotifyConversation(owner: OwnerWithChats) {
        Timber.v("onNotifyConversation $owner")
        _conversation.value?.toMutableList()?.let { data ->
            data.removeIf { it.conversation.id == owner.conversation.id }
            data.add(0, owner)
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
        ChatManager.instant.unregisterCallback(this)
        super.onCleared()
    }
}