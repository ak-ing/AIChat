package com.aking.aichat.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aking.aichat.database.entity.ChatEntity
import com.aking.aichat.model.repository.ChatRepository
import com.google.gson.Gson
import com.txznet.common.vm.BaseViewModel

/**
 * Created by Rick at 2023/02/23 1:00
 * @Description //TODO $
 */
class MainViewModel : BaseViewModel<ChatRepository>(ChatRepository()) {
    private val gson = Gson()

    private val _contentList = MutableLiveData<List<ChatEntity>>()
    val contentLd: LiveData<List<ChatEntity>> get() = _contentList

    init {
    }

    /**
     * 发送问题
     */
//    fun postResponse(query: String) = viewModelScope.launch {
//        repository.postRequest(query).onSuccess {
//            Timber.tag("postResponse").v("$it")
//            val tempJson = gson.toJson(it.choices[0])
//            val tempGson = gson.fromJson(tempJson, GptText::class.java)
//            Timber.tag("postResponse").v("${tempGson.text}")
//            //保存数据库
//        }.onError {
//
//        }
//    }

}