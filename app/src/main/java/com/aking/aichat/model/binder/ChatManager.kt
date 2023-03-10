package com.aking.aichat.model.binder

import android.os.IBinder
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

        val instant by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ChatManager() }
    }

    private val mCallbacks = mutableListOf<ChatCallback>()

    //Dispatch Callbacks
    private val mSampleDispatchCallback: ChatCallback = object : ChatCallback {
        override fun onAIReplies(response: GptResponse<Message>) {
            logV(TAG, "[onTemperatureChanged] $response")
            getMainHandler().post {
                for (callback in mCallbacks) {
                    callback.onAIReplies(response)
                }
            }
        }

        override fun onNotifyConversation(owner: OwnerWithChats) {
            getMainHandler().post {
                for (callback in mCallbacks) {
                    callback.onNotifyConversation(owner)
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

    fun registerCallback(callback: ChatCallback): Boolean {
        return RemoteHelper.exec {
            if (isServiceConnected(true)) {
                logV(TAG, "[registerCallback] getProxy().registerCallback ${getProxy()}")
                val result = getProxy().registerCallback(mSampleDispatchCallback)
                if (result) {
                    mCallbacks.remove(callback)
                    mCallbacks.add(callback)
                }
                return@exec result
            } else {
                getTaskQueue().offer {
                    logV(TAG, "[registerCallback] offer")
                    registerCallback(callback)
                }
                return@exec false
            }
        }
    }

    fun unregisterCallback(callback: ChatCallback): Boolean {
        logV(TAG, "[unregisterCallback]")
        return RemoteHelper.exec {
            if (isServiceConnected(true)) {
                return@exec mCallbacks.remove(callback)
            } else {
                getTaskQueue().offer {
                    unregisterCallback(callback)
                }
                return@exec false
            }
        }
    }

}