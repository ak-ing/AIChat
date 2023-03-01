package com.aking.aichat.ui.page

import android.view.View
import androidx.navigation.fragment.navArgs
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
    private val args: ChatFragmentArgs by navArgs()

    override fun getVMExtras(): Any = args.conversation

    override fun FragmentChatBinding.initView() {
        bindVariables(BR.viewModel to vm, BR.click to ClickProxy(), BR.adapter to ChatAdapter())
    }

    override fun FragmentChatBinding.initObservable() {
        vm.postRequest("OpenAi的max_tokens字段有什么用？")
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