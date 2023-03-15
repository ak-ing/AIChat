package com.aking.aichat.ui.helper

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.lifecycle.LifecycleService
import com.aking.aichat.MainActivity
import com.aking.aichat.R
import com.aking.aichat.utl.AlertWindowUtil

/**
 * Created by Rick on 2023-03-15  17:29.
 * Description:
 */
class FloatViewManager(private val context: Context) {

    private val floatingView: View
    private val layoutParams: WindowManager.LayoutParams
    private val animatorShow: ObjectAnimator
    private val animatorHide: ObjectAnimator
    private val mWindowManager = context.getSystemService(LifecycleService.WINDOW_SERVICE) as WindowManager
    private var clip = "粘贴板内容"
    val intent = Intent()

    init {
        floatingView = buildFloatView()
        layoutParams = buildLayoutParam()
        animatorShow = ObjectAnimator.ofFloat(floatingView, "alpha", 0.0f, 1.0f)
        animatorShow.duration = 500
        animatorHide = ObjectAnimator.ofFloat(floatingView, "alpha", 1.0f, 0.0f)
        animatorHide.duration = 500
        animatorHide.startDelay = 3000
        animatorHide.doOnEnd {
            mWindowManager.removeView(floatingView)
        }
    }

    fun show(clip: String) {
        if (AlertWindowUtil.commonROMPermissionCheck(context)) {
            this.clip = clip
            mWindowManager.addView(floatingView, layoutParams)
            animatorShow.start()
            animatorHide.start()
        }
    }

    private fun buildFloatView(): View {
        val layoutInflater =
            context.getSystemService(LifecycleService.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.layout_floatview, null)
        view.setOnClickListener {
            Toast.makeText(context, clip, Toast.LENGTH_SHORT).show()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        return view
    }

    private fun buildLayoutParam(): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams()
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY  //--//系统内部错误提示，显示于所有内容之上
        params.format = PixelFormat.RGBA_8888
        params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE) //当窗口可以获得焦点（没有设置FLAG_NOT_FOCUSALBE选项）时，仍然将窗口范围之外的点设备事件（鼠标、触摸屏）发送给后面的窗口处理
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
        params.x = 0
        params.y = 0
        return params
    }

}