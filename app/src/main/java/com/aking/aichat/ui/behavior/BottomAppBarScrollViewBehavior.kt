package com.aking.aichat.ui.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomappbar.BottomAppBar

/**
 * Created by Rick on 2023-02-22  15:31.
 * Description: 在BottomAppBar的上方
 */
class BottomAppBarScrollViewBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {
    private var dependencyHeight = 0

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is BottomAppBar
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (dependencyHeight == 0) {
            dependencyHeight = dependency.height
            updateMargin(child, dependency)
        }
        if (dependency.translationY == 0f) return false
        updateMargin(child, dependency)
        return true
    }

    private fun updateMargin(child: View, dependency: View) {
        child.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = (dependency.height - dependency.translationY).toInt()
        }
    }

}