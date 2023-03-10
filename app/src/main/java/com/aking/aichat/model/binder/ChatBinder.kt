package com.aking.aichat.model.binder

import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import com.aking.aichat.model.binder.listener.ChatCallback
import com.aking.openai.database.entity.ChatEntity
import com.aking.openai.database.entity.OwnerWithChats
import com.aking.openai.model.bean.GptResponse
import com.aking.openai.model.bean.GptText
import com.aking.openai.model.bean.Message
import com.aking.openai.model.repository.ChatRepository
import com.aking.openai.model.repository.DaoRepository
import com.aking.openai.network.MessageContext
import com.txznet.common.AppGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Rick on 2023-02-06  15:51.
 * Description:
 */
class ChatBinder(private val lifecycleScope: LifecycleCoroutineScope) : Binder(), IInterface {
    //    private val mChatCallbacks = RemoteCallbackList<ChatCallback>()
    private val mChatCallbacks = mutableListOf<ChatCallback>()
    private val repository = ChatRepository()
    private val daoRepository = DaoRepository()

    /**
     * 发送问题
     */
    fun postRequestTurbo(messages: List<MessageContext>, owner: OwnerWithChats) =
        lifecycleScope.launch {
            cacheToDb(GptText.createUSER(messages.last().content), owner)
            repository.postRequestTurbo(messages).onSuccess {
                Timber.tag("postRequest onSuccess").v("$it")
            }.onError {
                Toast.makeText(AppGlobal.context, "${it.error?.message}", Toast.LENGTH_SHORT).show()
            }.also {
                dispatchReplies(it)
                cacheToDb(GptText.createGPT(it), owner)
            }
        }

    /**
     * 如果是开启新会话
     */
    fun newChatIf(owner: OwnerWithChats) = lifecycleScope.launch(Dispatchers.IO) {
        if (daoRepository.loadById(owner.conversation.id) == null) {
            daoRepository.insertConversation(owner.conversation)
            notifyConversation(owner)
        }
    }

    /**
     * 保存数据库
     */
    private fun cacheToDb(gptText: GptText, owner: OwnerWithChats) {
        lifecycleScope.launch(Dispatchers.IO) {
            val chatEntity = ChatEntity.create(gptText, owner.conversation.id)
            daoRepository.insertChat(chatEntity)
            owner.conversation.timestamp = gptText.created.toLong()   //同步最后时间戳
            owner.conversation.endMessage = gptText.text
            owner.chat.add(chatEntity)
            notifyConversation(owner)
        }
    }

    /**
     * 通知会话数据已改变
     */
    private suspend fun notifyConversation(owner: OwnerWithChats) {
        daoRepository.updateConversation(owner.conversation)
        dispatchNotify(owner)
    }

    private fun dispatchReplies(gptResponse: GptResponse<Message>) {
//        val count = mChatCallbacks.beginBroadcast()
//        for (i in 0 until count) {
//            val chatCallback = mChatCallbacks.getBroadcastItem(i)
//            chatCallback.onAIReplies(gptResponse)
//        }
//        mChatCallbacks.finishBroadcast()
        for (mChatCallback in mChatCallbacks) {
            Log.e("TAG", "dispatchReplies: $mChatCallback")
            mChatCallback?.let { it.onAIReplies(gptResponse) }
        }
    }

    private fun dispatchNotify(owner: OwnerWithChats) {
//        val count = mChatCallbacks.beginBroadcast()
//        for (i in 0 until count) {
//            val chatCallback = mChatCallbacks.getBroadcastItem(i)
//            chatCallback.onNotifyConversation(owner)
//        }
//        mChatCallbacks.finishBroadcast()
        for (mChatCallback in mChatCallbacks) {
            mChatCallback?.let { it.onNotifyConversation(owner) }
        }
    }

    fun registerCallback(callback: ChatCallback): Boolean {
        if (mChatCallbacks.contains(callback)) return true
        return mChatCallbacks.add(callback)
    }

    fun unregisterCallback(callback: ChatCallback): Boolean {
        return mChatCallbacks.remove(callback)
    }

    override fun asBinder(): IBinder = this
}