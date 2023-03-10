package com.aking.aichat.vm

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aking.aichat.database.entity.ChatEntity
import com.aking.aichat.database.entity.ChatEntity.Companion.toGptText
import com.aking.aichat.database.entity.OwnerWithChats
import com.aking.aichat.model.bean.GptText
import com.aking.aichat.model.repository.ChatRepository
import com.aking.aichat.model.repository.DaoRepository
import com.txznet.common.AppGlobal
import com.txznet.common.vm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * Created by Rick at 2023/02/23 1:00
 * @Description //TODO $
 */
class ChatViewModel(private val ownerWithChats: OwnerWithChats) :
    BaseViewModel<ChatRepository>(ChatRepository()) {

    private val daoRepository = DaoRepository()

    private val _chatListLD = MutableLiveData<List<GptText>>(mutableListOf())
    val chatListLD: LiveData<List<GptText>> get() = _chatListLD

    init {
        addCloseable(daoRepository)
        _chatListLD.value = ownerWithChats.chat.map { it.toGptText() }
        initConversation()
    }

    /**
     * 发送问题
     */
    fun postRequest(query: String) = viewModelScope.launch {
        Timber.tag("postRequest").v(query)
        val messageContext = repository.buildContext(query, chatListLD.value ?: emptyList())
        handlerMessage(GptText.createUSER(query))
        repository.postRequestTurbo(messageContext).onSuccess {
            Timber.tag("postRequest").v("$it")
            handlerMessage(GptText.createGPT(it))
        }.onError {
            Toast.makeText(AppGlobal.context, "${it.error?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 处理聊天消息
     */
    private fun handlerMessage(gptText: GptText) {
        chatListLD.value?.toMutableList()?.let {
            it.add(gptText)
            _chatListLD.value = it
        }
        //保存数据库
        viewModelScope.launch(Dispatchers.IO) {
            val chatEntity = ChatEntity.create(gptText, ownerWithChats.conversation.id)
            daoRepository.insertChat(chatEntity)
            ownerWithChats.conversation.timestamp = gptText.created.toLong()   //同步最后时间戳
            ownerWithChats.conversation.endMessage = gptText.text
            ownerWithChats.chat.add(chatEntity)
            notifyConversation()
        }
    }

    /**
     * 通知会话数据已改变
     */
    private suspend fun notifyConversation() {
        daoRepository.updateConversation(ownerWithChats.conversation)
        EventBus.getDefault().postSticky(ownerWithChats)
    }

    private fun initConversation() = viewModelScope.launch(Dispatchers.IO) {
        if (daoRepository.loadById(ownerWithChats.conversation.id) == null) {
            daoRepository.insertConversation(ownerWithChats.conversation)
            EventBus.getDefault().postSticky(ownerWithChats)
        }
    }

}