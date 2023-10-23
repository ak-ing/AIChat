package com.aking.aichat.widget

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import com.aking.aichat.R
import com.aking.aichat.ui.page.ChatFragment
import com.aking.aichat.utl.buildOwnerWithChats
import com.txznet.common.ui.BaseActivity
import com.txznet.common.utils.CLASS_TAG
import com.txznet.common.utils.dp
import com.txznet.common.utils.getHeightPixels
import com.txznet.common.utils.getWidthPixels
import timber.log.Timber

class DialogActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble)
        val lp = window.attributes
        lp.width = (getWidthPixels() - 48.dp).toInt()
        lp.height = (getHeightPixels() * 2f / 5f).toInt()
        lp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        lp.y = 100
        initView()
        Timber.tag(CLASS_TAG).e("onCreate: ")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.tag(CLASS_TAG).e("onNewIntent: ")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.tag(CLASS_TAG).e("onRestart: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag(CLASS_TAG).e("onDestroy: ")
    }

    private fun initView() {
        val shortcut = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        //val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
        Timber.tag(CLASS_TAG).v("initView shortcutExtra: $shortcut")
        val bundle = Bundle()
        bundle.putSerializable("ownerWithChat", buildOwnerWithChats())
        if (shortcut != null) {
            bundle.putCharSequence("shortcut", shortcut)
        }

        val chatFragment = ChatFragment()
        chatFragment.hasFocus = true
        chatFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, chatFragment)
            .commitNow()

    }
}