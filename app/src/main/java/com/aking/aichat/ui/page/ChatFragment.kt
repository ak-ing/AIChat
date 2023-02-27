package com.aking.aichat.ui.page

import android.view.View
import com.aking.aichat.BR
import com.aking.aichat.R
import com.aking.aichat.databinding.FragmentChatBinding
import com.aking.aichat.ui.adapter.ChatAdapter
import com.aking.aichat.vm.ChatViewModel
import com.txznet.common.ui.BaseVMFragment

/**
 * Created by Rick on 2023-02-24  16:09.
 * Description:
 */
class ChatFragment : BaseVMFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    override fun getVMExtras(): Any? = null

    override fun FragmentChatBinding.initView() {
        bindVariables(BR.viewModel to vm, BR.click to ClickProxy(), BR.adapter to ChatAdapter())
    }

    override fun FragmentChatBinding.initObservable() {
    }

    inner class ClickProxy {
        fun clickSend(v: View) {
            binding.editMessage.text.apply {
                vm.postRequest("$this")
                clear()
            }
        }
    }
}