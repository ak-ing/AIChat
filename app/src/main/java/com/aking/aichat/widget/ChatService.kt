package com.aking.aichat.widget

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.aking.aichat.R
import com.aking.aichat.database.entity.ChatEntity
import com.aking.aichat.database.entity.OwnerWithChats
import com.aking.aichat.model.bean.GptResponse
import com.aking.aichat.model.bean.GptText
import com.aking.aichat.model.bean.Message
import com.aking.aichat.model.repository.ChatRepository
import com.aking.aichat.model.repository.DaoRepository
import com.aking.aichat.network.MessageContext
import com.txznet.common.AppGlobal
import com.txznet.common.utils.CLASS_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Rick on 2023-03-09  14:55.
 * Description:
 */
class ChatService : LifecycleService() {

    companion object {
        private const val CHANNEL_ID_STRING = "SimpleService"
        private const val CHANNEL_ID = 0x11
    }

    private val repository = ChatRepository()
    private val daoRepository = DaoRepository()

    override fun onCreate() {
        super.onCreate()
        startServiceForeground()
        Timber.tag(CLASS_TAG).d("[onCreate]")
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        Timber.tag(CLASS_TAG).d("[onBind]")
        return null
    }


    /**
     * 发送问题
     */
    suspend fun postRequest(messages: List<MessageContext>, owner: OwnerWithChats): GptResponse<Message> {
        cacheToDb(GptText.createUSER(messages.last().content), owner)
        return repository.postRequestTurbo(messages).onSuccess {
            Timber.tag("postRequest onSuccess").v("$it")
            cacheToDb(GptText.createGPT(it), owner)
        }.onError {
            Toast.makeText(AppGlobal.context, "${it.error?.message}", Toast.LENGTH_SHORT).show()
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
            daoRepository.updateConversation(owner.conversation)
        }
    }


    private fun startServiceForeground() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                CHANNEL_ID_STRING, getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_STRING)
                .setSmallIcon(IconCompat.createWithResource(this, R.drawable.ic_face))
                .build()
            startForeground(CHANNEL_ID, notification)
        }
    }

}