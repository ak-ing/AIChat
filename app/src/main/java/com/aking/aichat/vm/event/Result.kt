package com.aking.aichat.vm.event

import com.aking.aichat.model.bean.Choices
import com.aking.aichat.model.bean.GptResponse

/**
 * Created by Rick on 2023-03-10  17:15.
 * Description:
 */
fun interface Result<T : Choices> {
    fun onResult(dataResult: GptResponse<T>)
}