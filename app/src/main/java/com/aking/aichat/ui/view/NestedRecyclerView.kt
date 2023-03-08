package com.aking.aichat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Rick on 2023-03-06  15:25.
 * Description: 处理DrawerLayout滑动冲突
 */
class NestedRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private var startX = 0f
    private var startY = 0f
    private var totalX = 0f
    private var totalY = 0f
    private var scaledTouchSlop = 0
    private var flag = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun dispatchTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                totalX = 0f
                totalY = 0f
                startX = e.rawX
                startY = e.rawY
                flag = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (flag) return super.dispatchTouchEvent(e)
                val tempX = e.rawX - startX
                val tempY = e.rawY - startY
                totalX += tempX
                totalY += tempY
                if (totalX > totalY) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                if (totalX >= scaledTouchSlop || totalY >= scaledTouchSlop) {
                    flag = true
                }
            }
        }
        return super.dispatchTouchEvent(e)
    }
}