package com.aking.aichat.vm

import com.aking.openai.model.repository.ChatRepository
import com.txznet.common.vm.BaseViewModel

/**
 * Created by Rick on 2023-03-10  16:58.
 * Description:
 */
class SharedViewModel : BaseViewModel<ChatRepository>(ChatRepository()) {


}