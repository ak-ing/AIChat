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


    /**
     * 发送问题
     */
    fun postResponse(query: String) = viewModelScope.launch {
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
        Timber.tag("响应结果").v("$response.choices.get(0)")
        val tempJson = gson.toJson("")
        val tempGson = gson.fromJson(tempJson, GptText::class.java)
        Timber.tag("加工结果").v("$tempGson.text")
        //保存数据库
    }

}