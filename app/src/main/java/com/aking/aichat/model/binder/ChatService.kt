package com.aking.aichat.model.binder

import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.*
import com.aking.aichat.R
import com.aking.aichat.ui.helper.FloatViewManager
import com.txznet.common.utils.CLASS_TAG
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

    private val mainHandle = Handler(Looper.myLooper()!!)
    private val binder by lazy { ChatBinder(lifecycleScope) }
    private val mClipboardManager by lazy { getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
    private val floatingManager by lazy { FloatViewManager(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        startServiceForeground()
        Timber.tag(CLASS_TAG).d("[onCreate]")
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
        mClipboardManager.addPrimaryClipChangedListener {
            mainHandle.removeCallbacksAndMessages(null)
            mainHandle.postDelayed(::handlerFloatView, 100)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        Timber.tag(CLASS_TAG).d("[onBind]")
        return binder
    }

    private fun handlerFloatView() {
        mClipboardManager.primaryClip?.let {
            floatingManager.show("${it.getItemAt(0).text}")
        }
    }

    private fun startServiceForeground() {
        val notificationManager = NotificationManagerCompat.from(this)
        val channel: NotificationChannelCompat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannelCompat.Builder(
                    CHANNEL_ID_STRING,
                    NotificationManager.IMPORTANCE_MIN
                ).setName(getString(R.string.app_name)).build()

            notificationManager.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_STRING)
                .setSmallIcon(IconCompat.createWithResource(this, R.drawable.ic_face))
                .setContentTitle("ChatService").setContentText("挂起中").build()
            startForeground(CHANNEL_ID, notification)
        }
    }

    private inner class ApplicationLifecycleObserver : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            //应用移至前台
            binder.isForeground = true
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            //应用移至后台
            binder.isForeground = false
        }
    }

}