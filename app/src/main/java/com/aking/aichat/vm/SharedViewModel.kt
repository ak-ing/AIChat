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
 * Created by Rick on 2023-03-10  16:58.
 * Description:
 */
class SharedViewModel : BaseViewModel<ChatRepository>(ChatRepository()) {


}