package com.aking.aichat

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.aking.aichat.model.binder.ChatService
import com.aking.openai.database.ChatDatabase
import com.txznet.common.utils.LogUtil
import timber.log.Timber


/**
 * Created by Rick at 2023/02/23 2:03
 */
class App : Application(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        ChatDatabase.init(this)
        LogUtil.enableLog(true)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(Timber.asTree())
        }

        startService()
    }

    private fun startService() {
        val startServiceIntent = Intent(this, ChatService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startServiceIntent)
        } else {
            startService(startServiceIntent)
        }
    }

    override fun getViewModelStore(): ViewModelStore = mAppViewModelStore

}