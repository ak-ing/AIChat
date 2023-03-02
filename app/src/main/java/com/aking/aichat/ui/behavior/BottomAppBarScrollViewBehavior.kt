package com.aking.aichat.ui.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import com.aking.aichat.utl.copyBoundsInWindow
import com.google.android.material.bottomappbar.BottomAppBar

/**
 * Created by Rick on 2023-02-22  15:31.
 * Description: 在BottomAppBar的上方
 */
class BottomAppBarScrollViewBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {
    private val rect = Rect()

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is BottomAppBar
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        child.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            if (dependency.translationY == 0f) {
                dependency.copyBoundsInWindow(rect, true)
                bottomMargin = rect.height() + dependency.paddingBottom
                return true
            }
            bottomMargin = (dependency.height - dependency.translationY).toInt()
        }
        return true
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        return super.onMeasureChild(
            parent,
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )
    }

}