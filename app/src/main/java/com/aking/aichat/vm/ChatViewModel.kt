package com.aking.aichat.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aking.aichat.model.binder.ChatManager
import com.aking.aichat.model.binder.listener.ChatCallback
import com.aking.aichat.model.repository.DataRepository
import com.aking.openai.database.entity.ChatEntity.Companion.toGptText
import com.aking.openai.database.entity.OwnerWithChats
import com.aking.openai.model.bean.GptResponse
import com.aking.openai.model.bean.GptText
import com.aking.openai.model.bean.GptText.Companion.ERROR
import com.aking.openai.model.bean.GptText.Companion.GPT
import com.aking.openai.model.bean.GptText.Companion.USER
import com.aking.openai.model.bean.GptText.Companion.loading
import com.aking.openai.model.bean.Message
import com.txznet.common.vm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Rick at 2023/02/23 1:00
 * @Description //TODO $
 */
class ChatViewModel(private val ownerWithChats: OwnerWithChats) :
    BaseViewModel<DataRepository>(DataRepository()), ChatCallback {

    private val _chatListLD = MutableLiveData<List<GptText>>(mutableListOf())
    val chatListLD: LiveData<List<GptText>> get() = _chatListLD

    init {
        _chatListLD.value = ownerWithChats.chat.map { it.toGptText() }
        repository.setChatListener(ownerWithChats.conversation.id, this)
        ChatManager.instant.newChatIf(ownerWithChats)
    }

    /**
     * AI回复
     */
    override fun onAIReplies(response: GptResponse<Message>) {
        viewModelScope.launch(Dispatchers.Main) {
            response.onSuccess {
                handlerMessage(GptText.createGPT(it))
            }.onError {
                handlerMessage(GptText.createEROOR("${it.error?.message}"))
            }
        }
    }

    /**
     * 发送问题
     */
    fun postRequest(query: String) {
        Timber.tag("postRequest").v(query)
        val messageContext =
            repository.buildContext(query, chatListLD.value?.toMutableList() ?: emptyList())
        viewModelScope.launch {
            handlerMessage(GptText.createUSER(query))
            repository.postRequestTurbo(messageContext, ownerWithChats)
        }
    }

    /**
     * 处理聊天消息
     */
    private suspend fun handlerMessage(gptText: GptText) {
        chatListLD.value?.toMutableList()?.let {
            when (gptText.viewType) {
                USER -> {
                    it.add(gptText)
                    _chatListLD.value = it
                    delay(500)
                    it.toMutableList().run {
                        add(loading)
                        _chatListLD.value = this
                    }
                }
                GPT, ERROR -> {
                    it.remove(loading)
                    it.add(gptText)
                    _chatListLD.value = it
                }
            }
        }
    }

}