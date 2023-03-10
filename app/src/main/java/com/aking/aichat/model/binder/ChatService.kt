package com.aking.aichat.model.binder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
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
        startServiceForeground()
        Timber.tag(CLASS_TAG).d("[onCreate]")
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        Timber.tag(CLASS_TAG).d("[onBind]")
        return binder
    }

    private fun startServiceForeground() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                CHANNEL_ID_STRING, getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID_STRING)
                .setSmallIcon(IconCompat.createWithResource(this, R.drawable.ic_face)).build()
            startForeground(CHANNEL_ID, notification)
        }
    }

}