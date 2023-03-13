package com.aking.aichat.model.binder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.aking.aichat.MainActivity
import com.aking.aichat.R
import com.aking.aichat.model.binder.listener.ChatCallback
import com.aking.aichat.utl.Constants.CONVERSATION_ID
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
    private val mApplication = AppGlobal.context
    var isForeground = false
    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(mApplication)
    }

    /**
     * 发送问题
     */
    fun postRequestTurbo(messages: List<MessageContext>, owner: OwnerWithChats) =
        lifecycleScope.launch(Dispatchers.IO) {
            cacheToDb(GptText.createUSER(messages.last().content), owner)
            cacheToDb(GptText.loading, owner)
            val response = repository.postRequestTurbo(messages)
            response.ownerID = owner.conversation.id
            deleteChat(GptText.loading, owner)
            response.onSuccess {
                Timber.tag("postRequest onSuccess").v("$it")
                cacheToDb(GptText.createGPT(it), owner)
                dispatchReplies(response)
            }.onError {
                Timber.tag("postRequest onError").v("$it")
                cacheToDb(GptText.createEROOR("${it.error?.message}"), owner)
                dispatchReplies(response)
            }
            if (!isForeground) {
                sendNotification(response, owner)
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
    private suspend fun cacheToDb(gptText: GptText, owner: OwnerWithChats) {
        val chatEntity = ChatEntity.create(gptText, owner.conversation.id)
        daoRepository.insertChat(chatEntity)
        owner.conversation.timestamp = gptText.created.toLong()   //同步最后时间戳
        owner.conversation.endMessage = gptText.text
        owner.chat.add(chatEntity)
        notifyConversation(owner.deepCopy())
    }

    private suspend fun deleteChat(gptText: GptText, owner: OwnerWithChats) {
        val chatEntity = ChatEntity.create(gptText, owner.conversation.id)
        daoRepository.deleteChat(chatEntity)
        owner.chat.remove(chatEntity)
        notifyConversation(owner.deepCopy())
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
            Timber.tag("TAG").e("dispatchReplies: %s", mChatCallback)
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

    private fun sendNotification(response: GptResponse<Message>, owner: OwnerWithChats) {
        val notification = buildNotification(response, owner).build()
        notificationManager.notify(owner.conversation.id, notification)
    }

    private fun buildNotification(
        response: GptResponse<Message>, owner: OwnerWithChats
    ): NotificationCompat.Builder {
        val intent = Intent(mApplication, MainActivity::class.java)
        intent.putExtra(CONVERSATION_ID, owner.conversation.id)
        val target = PendingIntent.getActivity(
            mApplication, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        setNotification()

        val icon = IconCompat.createWithResource(mApplication, R.drawable.ic_face)
        return NotificationCompat.Builder(mApplication, CHANNEL_ID_STRING)
            .setDefaults(NotificationCompat.DEFAULT_ALL).setSmallIcon(icon)
            .setContentTitle(owner.conversation.name).setContentText(response.getText())
            .setContentIntent(target).setAutoCancel(true)
    }

    private fun setNotification() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID_STRING) == null) {
            val channel = NotificationChannelCompat.Builder(
                CHANNEL_ID_STRING, NotificationManager.IMPORTANCE_HIGH
            ).setVibrationEnabled(true).setLightsEnabled(true)
                .setName(mApplication.getString(R.string.app_name))
            notificationManager.createNotificationChannel(channel.build())
        }
    }

    override fun asBinder(): IBinder = this

    companion object {
        private const val CHANNEL_ID_STRING = "AIMessageChannel"
    }
}