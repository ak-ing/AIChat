/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aking.aichat.utl

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import com.aking.aichat.R


private val tmpIntArr = IntArray(2)

/**
 * Function which updates the given [rect] with this view's position and bounds in its window.
 */
fun View.copyBoundsInWindow(rect: Rect, clipPadding: Boolean = false) {
    if (isLaidOut && isAttachedToWindow) {
        val right = if (clipPadding) width - paddingStart - paddingEnd else width
        val bottom = if (clipPadding) height - paddingTop - paddingBottom else height
        rect.set(0, 0, right, bottom)
        getLocationInWindow(tmpIntArr)
        rect.offset(tmpIntArr[0], tmpIntArr[1])
    } else {
        throw IllegalArgumentException(
            "Can not copy bounds as view is not laid out" +
                    " or attached to window"
        )
    }
}

/**
 * Provides access to the hidden ViewGroup#suppressLayout method.
 */
fun ViewGroup.suppressLayoutCompat(suppress: Boolean) {
    if (Build.VERSION.SDK_INT >= 29) {
        suppressLayout(suppress)
    } else {
        hiddenSuppressLayout(this, suppress)
    }
}

/**
 * False when linking of the hidden suppressLayout method has previously failed.
 */
private var tryHiddenSuppressLayout = true

@SuppressLint("NewApi") // Lint doesn't know about the hidden method.
private fun hiddenSuppressLayout(group: ViewGroup, suppress: Boolean) {
    if (tryHiddenSuppressLayout) {
        // Since this was an @hide method made public, we can link directly against it with
        // a try/catch for its absence instead of doing the same through reflection.
        try {
            group.suppressLayout(suppress)
        } catch (e: NoSuchMethodError) {
            tryHiddenSuppressLayout = false
        }
    }
}


/**
 * 把当前的ViewGroup.getClipToPadding存在Tag里面，用于恢复
 */
fun ViewGroup.storeClipToPadding() {
    setTag(R.id.viewgroup_clip_padding, clipToPadding)
}

/**
 * 从storeClipChildren中恢复ViewGroup.getClipToPadding的值
 */
fun ViewGroup.restoreClipToPadding() {
    val stored = getTag(R.id.viewgroup_clip_padding)
    if (stored is Boolean) {
        //确认类型正确
        clipToPadding = stored
    }
    setTag(R.id.viewgroup_clip_padding, null)
}

/**
 * 存储当前的ViewGroup的Tag中的值
 */
fun ViewGroup.storeClipChildren() {
    setTag(R.id.viewgroup_clip_children, clipChildren)
}

/**
 * 从storeClipChildren中恢复ViewGroup.getClipChildren的值
 */
fun ViewGroup.restoreClipChildren() {
    val stored = getTag(R.id.viewgroup_clip_children)
    if (stored is Boolean) {
        clipChildren = stored
    }
    setTag(R.id.viewgroup_clip_children, null)
}

