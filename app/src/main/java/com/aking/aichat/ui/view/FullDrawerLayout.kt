package com.aking.aichat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import com.txznet.common.utils.getWidthPixels
import timber.log.Timber
import kotlin.math.max

/**
 * Created by Rick on 2023-02-24  12:23.
 * Description: 全屏触发滑动的抽屉栏
 */
class FullDrawerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : DrawerLayout(context, attrs) {
    private val displayWidthPercentage = 1.0

    override fun onFinishInflate() {
        super.onFinishInflate()
        val widthPixels = getWidthPixels()
        Timber.tag("widthPixels").i("$widthPixels")
        hookLeftDrag(widthPixels)
        hookRightDrag(widthPixels)
    }

    private fun hookLeftDrag(widthPixels: Int) {
        try {// displayWidthPercentage传1开启全面屏手势滑动，小于1设定滑动范围
            val leftDraggerField = this::class.java.superclass.getDeclaredField("mLeftDragger")
            leftDraggerField.isAccessible = true
            val leftDragger: ViewDragHelper = leftDraggerField.get(this) as ViewDragHelper
            val edgeSizeField = leftDragger::class.java.getDeclaredField("mDefaultEdgeSize")
            edgeSizeField.isAccessible = true
            val edgeSize = edgeSizeField.getInt(leftDragger)
            val max = max(edgeSize, (widthPixels * displayWidthPercentage).toInt())
            edgeSizeField.setInt(leftDragger, max)
            // 禁止长按滑出负一屏
            // 获取Layout的ViewDragCallBack实例mLeftCallback
            // 更改其属性mPeekRunnable
            val leftCallbackField = this::class.java.superclass.getDeclaredField("mLeftCallback")
            leftCallbackField.isAccessible = true
            // 因为无法直接访问私有内部类，所以该私有内部类实现的接口非常重要，通过多态的方式获取实例
            val leftCallback: ViewDragHelper.Callback = leftCallbackField.get(this) as ViewDragHelper.Callback
            val peekRunnableField = leftCallback::class.java.getDeclaredField("mPeekRunnable")
            peekRunnableField.isAccessible = true
            peekRunnableField.set(leftCallback, Runnable { })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hookRightDrag(widthPixels: Int) {
        try {// displayWidthPercentage传1开启全面屏手势滑动，小于1设定滑动范围
            val rightDraggerField = this::class.java.superclass.getDeclaredField("mRightDragger")
            rightDraggerField.isAccessible = true
            val leftDragger: ViewDragHelper = rightDraggerField.get(this) as ViewDragHelper
            val edgeSizeField = leftDragger::class.java.getDeclaredField("mDefaultEdgeSize")
            edgeSizeField.isAccessible = true
            val edgeSize = edgeSizeField.getInt(leftDragger)
            val max = max(edgeSize, (widthPixels * displayWidthPercentage).toInt())
            edgeSizeField.setInt(leftDragger, max)
            // 禁止长按滑出负一屏
            val rightCallbackField = this::class.java.superclass.getDeclaredField("mRightCallback")
            rightCallbackField.isAccessible = true
            // 因为无法直接访问私有内部类，所以该私有内部类实现的接口非常重要，通过多态的方式获取实例
            val leftCallback: ViewDragHelper.Callback =
                rightCallbackField.get(this) as ViewDragHelper.Callback
            val peekRunnableField = leftCallback::class.java.getDeclaredField("mPeekRunnable")
            peekRunnableField.isAccessible = true
            peekRunnableField.set(leftCallback, Runnable { })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}