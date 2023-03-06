package com.aking.aichat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

/**
 * Created by Rick on 2023-03-06  15:25.
 * Description:
 */
class NestedRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private var startX = 0f;
    private var startY = 0f

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = e.rawX
                startY = e.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val tempX = e.rawX - startX
                val tempY = e.rawY - startY
                if (tempY.absoluteValue > tempX.absoluteValue) {
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false
                }
            }
        }
        return super.onTouchEvent(e)
    }

}