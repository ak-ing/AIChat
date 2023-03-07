package com.txznet.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

/**
 * Binding适配器
 */


@BindingAdapter(
    value = ["topSystemWindowInsets", "bottomSystemWindowInsets", "isInsetsMargin"],
    requireAll = false
)
fun View.bindSystemWindowInsets(topInsets: Boolean, bottomInsets: Boolean, isInsetsMargin: Boolean = false) {
    setOnApplyWindowInsetsListener { v, insets ->
        WindowInsetsCompat.toWindowInsetsCompat(insets, v).also {
            if (isInsetsMargin) it.insetsMargin(v, topInsets, bottomInsets)
            else it.insetsPadding(v, topInsets, bottomInsets)
        }
        insets
    }
}

private fun WindowInsetsCompat.insetsPadding(v: View, topInsets: Boolean, bottomInsets: Boolean) {
    val top = if (topInsets) getInsets(WindowInsetsCompat.Type.statusBars()).top else v.paddingTop
    val bottom =
        if (bottomInsets) getInsets(WindowInsetsCompat.Type.navigationBars()).bottom else v.paddingBottom
    if (topInsets || bottomInsets) v.updatePadding(top = top, bottom = bottom)
}

private fun WindowInsetsCompat.insetsMargin(v: View, topInsets: Boolean, bottomInsets: Boolean) {
    val top = if (topInsets) getInsets(WindowInsetsCompat.Type.statusBars()).top else v.marginTop
    val bottom =
        if (bottomInsets) getInsets(WindowInsetsCompat.Type.navigationBars()).bottom else v.marginBottom
    if (topInsets || bottomInsets) v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        topMargin = top
        bottomMargin = bottom
    }
}

@BindingAdapter(value = ["adjustIme"], requireAll = false)
fun View.bindAdjustIme(adjustIme: Boolean) {
    if (!adjustIme) return
    setOnApplyWindowInsetsListener { v, insets ->
        WindowInsetsCompat.toWindowInsetsCompat(insets, v).also {
            val bottom = it.getInsets(WindowInsetsCompat.Type.ime()).bottom
            v.updatePadding(bottom = bottom)
        }
        insets
    }
}

@BindingAdapter(value = ["selected"], requireAll = false)
fun View.bindSelect(select: Boolean) {
    this.isSelected = select
}

@BindingAdapter(value = ["adjustWidth"])
fun View.bindAdjustWidth(adjustWidth: Int) {
    val params = this.layoutParams
    params.width = adjustWidth
    this.layoutParams = params
}

@BindingAdapter(value = ["adjustHeight"])
fun View.bindAdjustHeight(adjustHeight: Int) {
    val params = this.layoutParams
    params.height = adjustHeight
    this.layoutParams = params
}

@BindingAdapter(value = ["onTouchListener"])
fun View.bindOnTouchListener(onTouchListener: View.OnTouchListener) {
    this.setOnTouchListener(onTouchListener)
}

@BindingAdapter(value = ["adapter"], requireAll = false)
fun RecyclerView.bindAdapter(adapter: RecyclerView.Adapter<*>) {
    this.adapter = adapter
}

@BindingAdapter(value = ["submitList"], requireAll = false)
fun RecyclerView.submitList(list: List<Any>?) {
    adapter ?: return
    (adapter as? ListAdapter<Any, *>)?.submitList(list)
}

@BindingAdapter(value = ["dividerItemDecoration"], requireAll = false)
fun RecyclerView.bindDividerItemDecoration(require: Boolean) {
    if (require) {
        addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }
}

@BindingAdapter(value = ["bottomDecoration"], requireAll = false)
fun RecyclerView.bindBottomDecoration(dp: Int) {
    this.addItemDecoration(object : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.bottom = dp.dp.toInt()
        }
    })
}

@BindingAdapter(value = ["itemDecoration"], requireAll = false)
fun RecyclerView.bindItemDecoration(dp: Int) {
    this.addItemDecoration(object : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            val size = dp.dp.toInt()
            outRect.bottom = size
            outRect.top = size
            outRect.right = size
            outRect.left = size
        }
    })
}

/**
 * 是否显示
 */
@BindingAdapter("showIf")
fun View.bindShowIf(show: Boolean) {
    visibility = if (show) {
        VISIBLE
    } else {
        GONE
    }
}

/**
 * 点击防抖
 */
@BindingAdapter(
    "debouncingClick", "debouncingDuration", "debouncingWithEnable", "debouncingCountDown", requireAll = false
)
fun View.bindClickDebouncing(
    clickListener: View.OnClickListener,
    duration: Long,
    withEnable: Boolean,
    countdownCallback: OnDebouncingClickListener.OnCountdownCallback? = null
) {
    this.applyDebouncing(
        clickListener,
        if (duration == 0L) OnDebouncingClickListener.Companion.DEBOUNCING_DEFAULT_VALUE else duration,
        withEnable,
        countdownCallback
    )
}

@BindingAdapter(
    "glideSrc", "glideCenterCrop", "glideCircularCrop", requireAll = false
)
fun ImageView.bindGlideSrc(
    @DrawableRes drawableRes: Int?, centerCrop: Boolean = false, circularCrop: Boolean = false
) {
    if (drawableRes == null) return
    /* Glide加载 */
    createGlideRequest(
        context, drawableRes, centerCrop, circularCrop
    ).into(this)
}

@SuppressLint("CheckResult")
private fun createGlideRequest(
    context: Context, @DrawableRes src: Int, centerCrop: Boolean, circularCrop: Boolean
): RequestBuilder<Drawable> {
    val req = Glide.with(context).load(src)
    if (centerCrop) req.centerCrop()
    if (circularCrop) req.circleCrop()
    return req
}


/**
 * 时间转日期显示
 */
@BindingAdapter("textTimestamp", "timeFormat", requireAll = false)
fun TextView.bindTimestamp(timestamp: Long, timeFormat: String? = null) {
    text = timestamp.parseDate(timeFormat)
}
