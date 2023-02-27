package com.aking.aichat.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aking.aichat.model.bean.Choice
import com.aking.aichat.model.bean.GptResponse
import com.aking.aichat.model.repository.ChatRepository
import com.google.gson.Gson
import com.txznet.common.vm.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Rick at 2023/02/23 1:00
 * @Description //TODO $
 */
class ChatViewModel : BaseViewModel<ChatRepository>(ChatRepository()) {
    private val gson = Gson()

    private val _chatListLD = MutableLiveData<MutableList<GptResponse<Choice>>>(mutableListOf())
    val chatListLD: LiveData<MutableList<GptResponse<Choice>>> get() = _chatListLD

    /**
     * 发送问题
     */
    fun postRequest(query: String) = viewModelScope.launch {
        repository.postRequest(query).onSuccess {
            Timber.tag("postRequest").v("$it")
            handlerResult(it)
        }.onError {

        }
    }

    private fun handlerResult(result: GptResponse<Choice>) {
        val tempJson = gson.toJson(result.choices[0])
        Timber.tag("postRequest").v(tempJson)
        chatListLD.value?.toMutableList()?.let {
            it.add(result)
            _chatListLD.value = it
        }
        //保存数据库
    }

}