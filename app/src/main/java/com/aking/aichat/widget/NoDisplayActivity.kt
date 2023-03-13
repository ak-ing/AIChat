package com.aking.aichat.widget

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_SOUND
import androidx.core.app.NotificationCompat.DEFAULT_VIBRATE
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.aking.aichat.R
import com.txznet.common.utils.CLASS_TAG
import timber.log.Timber

/**
 * Created by Rick on 2023-03-09  17:03.
 * Description:
 */
class NoDisplayActivity : Activity() {

    companion object {
        private const val DEFAULT_ID = "message_shortCutId"
        private const val CHANNEL_ID_STRING = "AIChatChannel"
    }

    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val charSequenceExtra = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        //val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
        Timber.tag(CLASS_TAG).v("onCreate: $charSequenceExtra")
        if (charSequenceExtra != null) {
            initBubble(charSequenceExtra)
        }
        finish()
    }

    private fun initBubble(charSequenceExtra: CharSequence) {
        val chatPartner =
            Person.Builder().setIcon(IconCompat.createWithResource(this, R.drawable.ic_face))
                .setName("Chat partner").setImportant(true).build()
        setNotification()
        updateShortCutInfo(chatPartner)
        showNotification(chatPartner, charSequenceExtra)
    }

    private fun setNotification() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID_STRING) == null) {
            val channel = NotificationChannelCompat.Builder(
                    CHANNEL_ID_STRING,
                    NotificationManager.IMPORTANCE_HIGH
                ).setName(getString(R.string.app_name)).build()
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateShortCutInfo(chatPartner: Person) {
        val intent = Intent(this, BubbleActivity::class.java)
        // Create sharing shortcut
        val build = ShortcutInfoCompat.Builder(this, DEFAULT_ID).setShortLabel(chatPartner.name!!)
            .setCategories(setOf("android.intent.category.DEFAULT"))
            .setIntent(intent.setAction("com.aking.aichat.action.BubbleActivity"))
            .setLongLived(true).setIcon(chatPartner.icon).build()
        ShortcutManagerCompat.pushDynamicShortcut(this, build)
    }

    private fun showNotification(chatPartner: Person, param: CharSequence) {
        // Create bubble intent
        val target =
            Intent(this, BubbleActivity::class.java).putExtra(Intent.EXTRA_PROCESS_TEXT, param)
        val bubbleIntent =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    this, 0, target, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getActivity(this, 0, target, PendingIntent.FLAG_UPDATE_CURRENT)
            }

        val messagingStyle = NotificationCompat.MessagingStyle(chatPartner)

        // Create bubble metadata
        val bubbleData = NotificationCompat.BubbleMetadata.Builder(bubbleIntent, chatPartner.icon!!)
            .setDesiredHeight(600).setAutoExpandBubble(true).setSuppressNotification(true).build()

        // Create notification, referencing the sharing shortcut
        val builder =
            NotificationCompat.Builder(this, CHANNEL_ID_STRING).setBubbleMetadata(bubbleData)
                .setSmallIcon(R.drawable.ic_face).setContentIntent(bubbleIntent).setAutoCancel(true)
                .setShortcutId(DEFAULT_ID).setStyle(messagingStyle)
                .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE).addPerson(chatPartner)

        notificationManager.notify(1573, builder.build())
    }
}