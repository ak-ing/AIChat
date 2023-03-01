package com.aking.aichat.ui.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior

/**
 * Created by Rick on 2023/3/2  0:01.
 * Description:
 */
class HideBottomViewBehavior<T : View>(context: Context?, attrs: AttributeSet?) :
    HideBottomViewOnScrollBehavior<T>(context, attrs) {
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
        if (dyConsumed > 0) {
            slideUp(child)
        } else if (dyConsumed < 0) {
            slideDown(child)
        }
    }
}