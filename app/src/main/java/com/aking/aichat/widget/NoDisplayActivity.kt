package com.aking.aichat.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_SOUND
import androidx.core.app.NotificationCompat.DEFAULT_VIBRATE
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.aking.aichat.R
import com.aking.aichat.ui.BubbleActivity
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

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val charSequenceExtra = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        //val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
        Timber.v("onCreate: $charSequenceExtra")
        if (charSequenceExtra != null) {
            initBubble(charSequenceExtra)
        }
        finish()
    }

    private fun initBubble(charSequenceExtra: CharSequence) {
        val chatPartner = Person.Builder()
            .setName("Chat partner")
            .setIcon(IconCompat.createWithResource(this, R.drawable.ic_face))
            .setImportant(true)
            .build()
        setNotification()
        updateShortCutInfo(chatPartner)
        showNotification(chatPartner, charSequenceExtra)
    }

    private fun setNotification() {
        val channel = NotificationChannel(
            CHANNEL_ID_STRING, getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateShortCutInfo(chatPartner: Person) {
        // Create sharing shortcut
        val build = ShortcutInfoCompat.Builder(this, DEFAULT_ID)
            .setShortLabel(chatPartner.name!!)
            .setCategories(setOf("android.intent.category.DEFAULT"))
            .setIntent(Intent(Intent.ACTION_DEFAULT))
            .setLongLived(true)
            .setIcon(chatPartner.icon)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(this, build)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(chatPartner: Person, param: CharSequence) {
        // Create bubble intent
        val target = Intent(this, BubbleActivity::class.java)
            .putExtra(Intent.EXTRA_PROCESS_TEXT, param)
        val bubbleIntent =
            PendingIntent.getActivity(this, 0, target, PendingIntent.FLAG_UPDATE_CURRENT /* flags */)

        val messagingStyle = NotificationCompat.MessagingStyle(chatPartner)

        // Create bubble metadata
        val bubbleData = NotificationCompat.BubbleMetadata.Builder(bubbleIntent, chatPartner.icon!!)
            .setDesiredHeight(600)
            .setAutoExpandBubble(true)
            .setSuppressNotification(true)
            .build()

        // Create notification, referencing the sharing shortcut
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_STRING)
            .setBubbleMetadata(bubbleData)
            .setSmallIcon(R.drawable.ic_face)
            .setShortcutId(DEFAULT_ID)
            .setStyle(messagingStyle)
            .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE)
            .addPerson(chatPartner)

        notificationManager.notify(1573, builder.build())
    }
}