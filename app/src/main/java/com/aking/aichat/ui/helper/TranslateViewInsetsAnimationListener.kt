package com.aking.aichat.ui.helper

import android.graphics.Rect
import android.view.View
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import com.aking.aichat.utl.copyBoundsInWindow
import com.google.android.material.math.MathUtils.lerp


/**
 * Created by Rick on 2023-03-02  10:33.
 * Description: 当点击文字输入框的时候，应用跟随着软键盘一起移动并且创造了一个更流畅的体验
 */
class TranslateViewInsetsAnimationListener(private val view: View, private val typeMask: Int) :
    WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {

    //开始和结束的view bounds容器
    private val startBounds: Rect = Rect()
    private val endBounds: Rect = Rect()


    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
        super.onPrepare(animation)
        //将View当前状态存入startBounds
        view.copyBoundsInWindow(startBounds, true)
    }

    override fun onStart(
        animation: WindowInsetsAnimationCompat,
        bounds: WindowInsetsAnimationCompat.BoundsCompat
    ): WindowInsetsAnimationCompat.BoundsCompat {
        //获取结束状态的bounds
        view.copyBoundsInWindow(endBounds, true)
        view.translationX = (startBounds.right - endBounds.right).toFloat()
        view.translationY = (startBounds.bottom - endBounds.bottom).toFloat()
        return bounds
    }

    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnimations: MutableList<WindowInsetsAnimationCompat>
    ): WindowInsetsCompat {
        //这个监听器只关心与typeMask匹配的动画
        //第一个动画
        var filteredAnim: WindowInsetsAnimationCompat? = null
        //只关心typeMask匹配的anim
        filteredAnim = runningAnimations.firstOrNull { o -> o.typeMask == typeMask }
        //用插值器获取动画进度,进行操作
        if (filteredAnim != null) {
            view.translationX = lerp(
                startBounds.right - endBounds.right.toFloat(),
                0f,
                filteredAnim.interpolatedFraction
            )

            view.translationY = lerp(
                startBounds.bottom - endBounds.bottom.toFloat(),
                0f,
                filteredAnim.interpolatedFraction
            )
        }

        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        //根据typeMask过滤到匹配的WindowInsetsAnimation对象
        if (animation.typeMask == this.typeMask) {
            //属性重置
            view.translationX = 0f
            view.translationY = 0f
            //清除状态
            startBounds.setEmpty()
            endBounds.setEmpty()
        }
    }
}