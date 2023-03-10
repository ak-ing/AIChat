package com.aking.aichat.vm.event

import com.aking.openai.model.bean.Choices
import com.aking.openai.model.bean.GptResponse

/**
 * Created by Rick on 2023-03-10  17:15.
 * Description:
 */
fun interface Result<T : Choices> {
    fun onResult(dataResult: GptResponse<T>)
}