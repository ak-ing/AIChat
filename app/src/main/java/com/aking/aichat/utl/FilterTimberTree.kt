package com.aking.aichat.utl

import timber.log.Timber

/**
 * Created by Rick at 2023/3/14 0:49.
 * @Description:
 */
class FilteringTimberTree(
    private val allowedTags: List<String> = ArrayList(),
    private var filteringEnabled: Boolean = true
) : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (!filteringEnabled || allowedTags.contains(tag)) {
            super.log(priority, tag, message, t)
        }
    }
}