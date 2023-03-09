package com.aking.aichat.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aking.aichat.R
import timber.log.Timber

class BubbleActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble)
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("TAG", "onRestart: ${intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("onDestroy")
    }

    private fun initView() {
        val charSequenceExtra = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
        Log.e("TAG", "onCreate: $charSequenceExtra")
        if (charSequenceExtra != null) {
        }


    }
}