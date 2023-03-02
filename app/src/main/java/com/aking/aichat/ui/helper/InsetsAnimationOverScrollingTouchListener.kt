package com.aking.aichat.ui.helper

import android.graphics.Rect
import android.os.CancellationSignal
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.Window
import android.view.animation.LinearInterpolator
import androidx.core.graphics.Insets
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationControlListenerCompat
import androidx.core.view.WindowInsetsAnimationControllerCompat
import androidx.core.view.WindowInsetsCompat
import com.aking.aichat.utl.copyBoundsInWindow
import kotlin.math.roundToInt


/**
 * Created by Rick on 2023-03-02  11:06.
 * Description:
 */
class InsetsAnimationOverScrollingTouchListener(private val window: Window) : View.OnTouchListener {
    //动画控制器
    private var insetsAnimationController: WindowInsetsAnimationControllerCompat? = null

    //当前的需要的监听器对象
    private var currentControlRequest: WindowInsetsAnimationControlListenerCompat? = null

    //初始的屏幕填充物
    private var startImeInsets: Insets = Insets.NONE

    //填充物是否被使用
    private var isImeShownAtStart = false

    //是否处理
    private var isHandling = false

    //最后触摸点的X
    private var lastTouchX = 0f

    //最后触摸点的Y
    private var lastTouchY = 0f

    //window的最后位置的Y
    private var lastWindowY = 0f

    //bounds容器
    private val bounds: Rect = Rect()

    //用于取消正在进行的操作
    private var cancellationSignal: CancellationSignal? = null

    //动画插值器
    private val linearInterpolator = LinearInterpolator()
    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {

                //当按下屏幕的时候
                //获取起始位置的坐标
                lastTouchX = event.x
                lastTouchY = event.y
                //将view当前的宽高信息存在bounds之中
                v.copyBoundsInWindow(bounds)
                lastWindowY = bounds.top.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {

                //当手指移动的时候
                //此时的View（就是demo中的RecycleView）可能正在以WindowInsetsAnimation的形式移动
                //过程中，我们需要确保它对我们触摸的响应
                //我们通过跟踪RecycleView在窗口中的Y位置来实现这一操做的同时检查当前的bounds的差异
                v.copyBoundsInWindow(bounds)
                val windowOffsetY = bounds.top - lastWindowY
                //到起始点的X方向的距离
                val dx = event.x - lastTouchX
                //到起始点的Y轴的距离
                val dy = event.y - lastTouchY + windowOffsetY
                if (!isHandling) {
                    //做一下触摸拦截，防止左右滑动的时候触发，和是否比touch slop大（触发事件的最小移动距离）
                    val dx1 = Math.abs(dx)
                    val dy1 = Math.abs(dy)
                    if (dx1 < dy1 && dy1 >= ViewConfiguration.get(v.context).scaledTouchSlop) {
                        isHandling = true
                    }
                }
                if (isHandling) {
                    //开始进行处理
                    if (currentControlRequest != null) {
                        updateImeInsets(dy)
                    } else if (!isImeShownAtStart && dy < 0 && v.canScrollVertically(-1)) {
                        //1.当前没有控制，输入法就不要显示
                        //2.用户正在向上滑动，
                        //3.开始控制输入法
                        startControlRequest(v)
                    }
                    //记录下X, Y位置，以及视图的Y窗口位用来给下一个触摸事件
                    lastTouchY = event.y
                    lastTouchX = event.x
                    lastWindowY = bounds.top.toFloat()
                }
            }
            MotionEvent.ACTION_UP -> {
                finish()
            }
            MotionEvent.ACTION_CANCEL -> {

                //取消当前的[WindowInsetsAnimationController],结束动画，恢复到手势开始时的状态。
                insetsAnimationController!!.finish(isImeShownAtStart)
                //操作取消
                cancellationSignal!!.cancel()
                //恢复初始状态
                reset()
            }
        }

        return false
    }

    //根据手指的移动距离 更新屏幕的填充物
    private fun updateImeInsets(dy: Float): Boolean {
        val controller = insetsAnimationController
        return if (controller == null) {
            Log.e("insetsAnimationController:", "我空的")
            false
        } else {
            //获取控制器对象
            //隐藏时的bottom位置
            val hiddenBottom = controller.hiddenStateInsets.bottom
            //显示时候的bottom位置
            val shownBottom = controller.shownStateInsets.bottom
            val startBottom: Int
            val endBottom: Int
            if (isImeShownAtStart) {
                //填充物被使用的时候
                startBottom = shownBottom
                endBottom = hiddenBottom
            } else {
                //填充物没被使用的时候
                startBottom = hiddenBottom
                endBottom = shownBottom
            }
            //计算出新的填充物 利用初始值和当前值 附加滚动条dy的值
            var insetBottom = (startImeInsets.bottom + controller.currentInsets.bottom - dy).roundToInt()
            //将填充物位置限定再startBottom和endBottom之间
            //(这样子就不会输入框在键盘完全弹出来之后还在向上/向下移动了)
            if (startBottom < endBottom) {
                if (insetBottom < startBottom) {
                    insetBottom = startBottom
                }
                if (insetBottom > endBottom) {
                    insetBottom = endBottom
                }
            }
            if (endBottom < startBottom) {
                if (insetBottom < endBottom) {
                    insetBottom = endBottom
                }
                if (insetBottom > startBottom) {
                    insetBottom = startBottom
                }
            }
            controller.setInsetsAndAlpha( //将计算出的填充物给视图来挤压屏幕空间
                Insets.of(
                    0, 0, 0,
                    insetBottom
                ),  //透明度
                1f,  //由此返回fraction（人话：动画进度）
                (insetBottom - startBottom).toFloat() / (endBottom - startBottom)
            )
            true
        }
    }

    //开启控制
    private fun startControlRequest(view: View) {
        //追踪输入法填充物和可见性
        val windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets)
        startImeInsets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.ime())
        isImeShownAtStart = windowInsetsCompat.isVisible(WindowInsetsCompat.Type.ime())
        //为了控制一个WindowInsetsAnimation，需要传入一个监听器
        //这个监听器跟踪当前请求，并存储当前的WindowInsetsAnimationController
        val listener: WindowInsetsAnimationControlListenerCompat =
            object : WindowInsetsAnimationControlListenerCompat {


                override fun onReady(controller: WindowInsetsAnimationControllerCompat, types: Int) {
                    if (currentControlRequest === this) {
                        onRequestReady(controller)
                    } else {
                        finish()
                    }
                }

                override fun onFinished(controller: WindowInsetsAnimationControllerCompat) {
                }

                override fun onCancelled(controller: WindowInsetsAnimationControllerCompat?) {
                    reset()
                }
            }
        WindowCompat.getInsetsController(window, view).controlWindowInsetsAnimation(
            WindowInsetsCompat.Type.ime(),
            -1,
            linearInterpolator,
            cancellationSignal,
            listener
        )
        currentControlRequest = listener
    }

    //准备控制器
    private fun onRequestReady(controller: WindowInsetsAnimationControllerCompat) {
        insetsAnimationController = controller
    }

    //结束当前的WindowInsetsAnimationController，结束掉动画和切换
    //如果用户滚动了超过50%，IME的可见性设置为结束状态。
    private fun finish() {
        insetsAnimationController?.let {
            if (it.currentFraction >= 0.5f) {
                //进度大于50%
                it.finish(!isImeShownAtStart)
            } else {
                //进度小于50%
                it.finish(isImeShownAtStart)
            }
        }
        cancellationSignal?.cancel()
        reset()
    }

    //回归初始状态
    private fun reset() {
        isHandling = false
        lastTouchX = 0f
        lastTouchY = 0f
        lastWindowY = 0f
        bounds.setEmpty()
        insetsAnimationController = null
        currentControlRequest = null
        startImeInsets = Insets.NONE
        isImeShownAtStart = false
    }

}