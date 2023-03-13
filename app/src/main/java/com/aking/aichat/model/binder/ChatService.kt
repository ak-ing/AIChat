package com.aking.aichat.model.binder

import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.*
import com.aking.aichat.R
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

    private val binder by lazy { ChatBinder(lifecycleScope) }

    override fun onCreate() {
        super.onCreate()
        Timber.tag(CLASS_TAG).d("[onCreate]")
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startServiceForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        Timber.tag(CLASS_TAG).d("[onBind]")
        return binder
    }

    private fun startServiceForeground() {
        val notificationManager = NotificationManagerCompat.from(this)
        val channel: NotificationChannelCompat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannelCompat
                .Builder(CHANNEL_ID_STRING, NotificationManager.IMPORTANCE_MIN)
                .setName(getString(R.string.app_name))
                .build()

            notificationManager.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_STRING)
                .setSmallIcon(IconCompat.createWithResource(this, R.drawable.ic_face)).build()
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