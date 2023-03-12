package com.aking.aichat.model.binder

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aking.aichat.MainActivity
import com.aking.aichat.R
import com.aking.aichat.model.binder.base.BaseConnectManager
import com.aking.aichat.model.binder.listener.ChatCallback
import com.aking.openai.database.entity.OwnerWithChats
import com.aking.openai.model.bean.GptResponse
import com.aking.openai.model.bean.Message
import com.aking.openai.network.MessageContext
import com.txznet.common.utils.CLASS_TAG
import com.txznet.common.utils.logV
import com.txznet.sdk.util.RemoteHelper

/**
 * Created by Rick on 2023-02-03  17:10.
 * Description: 示例
 */
class ChatManager : BaseConnectManager<ChatBinder>() {
    private val TAG = CLASS_TAG

    companion object {
        private const val SERVICE_PACKAGE = "com.aking.aichat"
        private const val SERVICE_CLASSNAME = "com.aking.aichat.model.binder.ChatService"
        private const val CHANNEL_ID_STRING = "AIMessageChannel"

        val instant by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ChatManager() }
    }

    private val mCallbacks = mutableMapOf<Int, ChatCallback>()
    private val notificationManager: NotificationManager by lazy {
        mApplication.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
    }

    //Dispatch Callbacks
    private val mSampleDispatchCallback: ChatCallback = object : ChatCallback {
        override fun onAIReplies(response: GptResponse<Message>) {
            logV(TAG, "[onAIReplies] $response")
            getMainHandler().post {
                sendNotification(response)
                for (mCallback in mCallbacks) {
                    Log.e(TAG, "onAIReplies: $mCallback", )
                }
                mCallbacks[response.ownerID]?.onAIReplies(response)
            }
        }

        override fun onNotifyConversation(owner: OwnerWithChats) {
            getMainHandler().post {
                for (value in mCallbacks.values) {
                    value?.onNotifyConversation(owner)
                }
            }
        }

    }

    override fun getServicePkgName(): String = SERVICE_PACKAGE

    override fun getServiceClassName(): String = SERVICE_CLASSNAME

    override fun asInterface(service: IBinder?): ChatBinder {
        return service as ChatBinder
    }

    override fun onBindSuccess() {
        logV(TAG, "[onBindSuccess]")
        getProxy().registerCallback(mSampleDispatchCallback)
    }

    override fun onBinderDied() {
        logV(TAG, "[onBinderDied]")
        getProxy().unregisterCallback(mSampleDispatchCallback)
    }

    /******************/

    fun postRequestTurbo(messages: List<MessageContext>, owner: OwnerWithChats) {
        tryExec {
            logV(TAG, "[postRequest] getProxy().postRequest ${getProxy()}")
            getProxy().postRequestTurbo(messages, owner)
        }
    }

    fun newChatIf(owner: OwnerWithChats) {
        tryExec {
            logV(TAG, "[newChatIf] getProxy().newChatIf ${getProxy()}")
            getProxy().newChatIf(owner)
        }
    }

    fun registerCallback(conversationId: Int, callback: ChatCallback): Boolean {
        return RemoteHelper.exec {
            if (isServiceConnected(true)) {
                logV(TAG, "[registerCallback] getProxy().registerCallback ${getProxy()}")
                val result = getProxy().registerCallback(mSampleDispatchCallback)
                if (result) {
                    mCallbacks[conversationId] = callback
                }
                return@exec result
            } else {
                getTaskQueue().offer {
                    logV(TAG, "[registerCallback] offer")
                    registerCallback(conversationId, callback)
                }
                return@exec false
            }
        }
    }

    fun unregisterCallback(callback: ChatCallback): Boolean {
        logV(TAG, "[unregisterCallback]")
        return RemoteHelper.exec {
            if (isServiceConnected(true)) {
                val values = mCallbacks.values
                while (values.contains(callback)) {
                    values.remove(callback)
                }
                return@exec true
            } else {
                getTaskQueue().offer {
                    unregisterCallback(callback)
                }
                return@exec false
            }
        }
    }

    private fun sendNotification(response: GptResponse<Message>) {
        val notification = buildNotification(response).build()
        notificationManager.notify(1572, notification)
    }

    private fun buildNotification(response: GptResponse<Message>): NotificationCompat.Builder {
        val intent = Intent(mApplication, MainActivity::class.java)
        val target = PendingIntent.getActivity(mApplication, 0, intent, PendingIntent.FLAG_MUTABLE)

        setNotification()

        return NotificationCompat.Builder(mApplication, CHANNEL_ID_STRING)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.ic_face).setContentText(response.getText())
            .setContentIntent(target).setAutoCancel(true)
    }

    private fun setNotification() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID_STRING) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID_STRING,
                mApplication.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}