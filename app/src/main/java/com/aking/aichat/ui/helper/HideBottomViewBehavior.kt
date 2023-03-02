package com.aking.aichat.ui.helper

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior

/**
 * Created by Rick on 2023/3/2  0:01.
 * Description:
 */
class HideBottomViewBehavior<T : View>(context: Context?, attrs: AttributeSet?) :
    HideBottomViewOnScrollBehavior<T>(context, attrs) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        val a = super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
        Log.e("TAG", "onStartNestedScroll: $a")
        return true
    }

    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        Log.i("TAG", "onNestedScroll: ")
        if (dyConsumed > 1) {
            slideUp(child)
        } else if (dyConsumed < -1) {
            slideDown(child)
        }
    }
}