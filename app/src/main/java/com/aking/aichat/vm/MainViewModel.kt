package com.aking.aichat.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aking.aichat.database.entity.ContentEntity
import com.aking.aichat.model.bean.GptText
import com.aking.aichat.model.repository.ChatRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.txznet.common.vm.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Rick at 2023/02/23 1:00
 * @Description //TODO $
 */
class MainViewModel : BaseViewModel<ChatRepository>(ChatRepository()) {
    private val gson = Gson()

    private val _contentList = MutableLiveData<List<ContentEntity>>()
    val contentLd: LiveData<List<ContentEntity>> get() = _contentList

    init {
//        postResponse("hello! who are you?")
    }

    /**
     * 发送问题
     */
    fun postResponse(query: String) = viewModelScope.launch {
        Timber.tag("postResponse").v(query)
        val jsonObject = JsonObject().apply {
            addProperty("model", "text-davinci-003")
            addProperty("prompt", query)
            addProperty("temperature", 0)
            addProperty("max_tokens", 500)
            addProperty("top_p", 1)
            addProperty("frequency_penalty", 0.0)
            addProperty("presence_penalty", 0.0)
        }
        val response = repository.postResponse(jsonObject)
        Timber.tag("postResponse").v("${response.choices[0]}")
        val tempJson = gson.toJson(response.choices[0])
        val tempGson = gson.fromJson(tempJson, GptText::class.java)
        Timber.tag("postResponse").v("${tempGson.text}")
        //保存数据库
    }

}