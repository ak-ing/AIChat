package com.aking.aichat.utl

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.EFFECT_TICK
import android.os.Vibrator
import com.txznet.common.AppGlobal


/**
 * Created by Rick at 2023/3/17 0:58.
 * @Description:
 */
object VibratorManager {

    private val vibrator by lazy { AppGlobal.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    fun vibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(EFFECT_TICK))
        } else {
            vibrator.vibrate(VibrationEffect.createOneShot(5, 150))
        }
    }

}