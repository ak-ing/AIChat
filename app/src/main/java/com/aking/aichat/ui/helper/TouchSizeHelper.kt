package com.aking.aichat.ui.helper

import android.view.MotionEvent
import android.view.View
import com.txznet.common.utils.bindAdjustHeight

/**
 * Created by Rick on 2023-02-23  20:22.
 * Description:
 */
class TouchSizeHelper : View.OnTouchListener {
    private var startY = 0f
    private var mHeight = 0

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
                if (v.height + dY <= mHeight) {
                    return false
                }
                v.bindAdjustHeight(v.height + dY.toInt())
                startY = endY
            }
        }
        return true
    }
}