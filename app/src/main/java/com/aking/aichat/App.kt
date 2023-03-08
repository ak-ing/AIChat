package com.aking.aichat

import android.app.Application
import com.txznet.common.utils.LogUtil
import org.greenrobot.eventbus.EventBus
import timber.log.Timber


/**
 * Created by Rick at 2023/02/23 2:03
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LogUtil.enableLog(true)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(Timber.asTree())
        }

        EventBus.builder().addIndex(MyEventBusIndex()).installDefaultEventBus()
    }

}