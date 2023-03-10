package com.aking.aichat.widget

import android.content.Intent
import android.os.Bundle
import com.aking.aichat.R
import com.aking.aichat.ui.page.ChatFragment
import com.aking.aichat.utl.buildOwnerWithChats
import com.txznet.common.ui.BaseActivity
import com.txznet.common.utils.CLASS_TAG
import timber.log.Timber

class BubbleActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble)
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
        chatFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, chatFragment)
            .commitNow()

    }
}