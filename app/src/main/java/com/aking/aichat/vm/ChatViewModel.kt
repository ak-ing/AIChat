package com.aking.aichat.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aking.aichat.database.entity.ChatEntity
import com.aking.aichat.database.entity.ConversationEntity
import com.aking.aichat.model.bean.GptText
import com.aking.aichat.model.repository.ChatDaoRepository
import com.aking.aichat.model.repository.ChatRepository
import com.txznet.common.vm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Rick at 2023/02/23 1:00
 * @Description //TODO $
 */
class ChatViewModel(private val conversation: ConversationEntity) :
    BaseViewModel<ChatRepository>(ChatRepository()) {

    private val daoRepository by lazy { ChatDaoRepository() }

    private val _chatListLD = MutableLiveData<MutableList<GptText>>(mutableListOf())
    val chatListLD: LiveData<MutableList<GptText>> get() = _chatListLD

    /**
     * 发送问题
     */
    fun postRequest(query: String) = viewModelScope.launch {
        handlerResult(GptText.createUSER(query))
        repository.postRequest(query).onSuccess {
            Timber.tag("postRequest").v("$it")
            Timber.tag("postRequest").v("${it.type}")
            handlerResult(GptText.createGPT(it))
            viewModelScope.launch(Dispatchers.IO) {
                val all = daoRepository.getAll()
                Timber.tag("all").w(all.toString())
            }
        }.onError {

        }
    }

    private fun handlerResult(gptText: GptText) {
        Timber.tag("postRequest").v("$gptText")
        chatListLD.value?.toMutableList()?.let {
            it.add(gptText)
            _chatListLD.value = it
        }
        //保存数据库
        viewModelScope.launch(Dispatchers.IO) {
            daoRepository.insertChat(ChatEntity.create(gptText, conversation))
        }
    }

}