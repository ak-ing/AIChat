package com.aking.aichat.ui.helper

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.txznet.common.utils.bindAdjustHeight
import com.txznet.common.utils.dp
import com.txznet.common.utils.getHeightPixels
import kotlin.math.min

/**
 * Created by Rick on 2023-02-23  20:22.
 * Description:
 */
class TouchSizeHelper : View.OnTouchListener {
    private var startY = 0f
    private var mHeight = 0
    private val heightPixel = getHeightPixels()

    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mHeight == 0) {
                    mHeight = v.height
                }
                startY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val endY = event.rawY
                val dY = endY - startY
                val preHeight = v.height + dY
                if (preHeight <= mHeight || preHeight >= heightPixel) {
                    return false
                }
                v.bindAdjustHeight(v.height + dY.toInt())
                val percent = (preHeight * 1.0 - mHeight) / (heightPixel - mHeight)
                val min = min(percent * 2, 1.0)
                dispatchAlpha(v, 1 - min.toFloat())
                v.z = (min * 12.dp).toFloat()
                startY = endY
            }
        }
        return true
    }

    private fun dispatchAlpha(view: View, alpha: Float) {
        if (view is ViewGroup) {
            view.children.forEach { it.alpha = alpha }
        }
    }

}