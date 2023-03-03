package com.aking.aichat.vm

import com.aking.aichat.model.repository.ChatRepository
import com.txznet.common.vm.BaseViewModel

/**
 * Created by Rick at 2023/02/23 1:00
 * @Description //TODO $
 */
class MainViewModel : BaseViewModel<ChatRepository>(ChatRepository()) {

}