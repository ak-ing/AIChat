package com.aking.aichat.ui.page

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.navArgs
import com.aking.aichat.BR
import com.aking.aichat.R
import com.aking.aichat.databinding.FragmentChatBinding
import com.aking.aichat.ui.adapter.ChatAdapter
import com.aking.aichat.ui.helper.ControlFocusInsetsAnimationCallback
import com.aking.aichat.ui.helper.RootViewDeferringInsetsCallback
import com.aking.aichat.ui.helper.TranslateDeferringInsetsAnimationCallback
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


        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )
        // RootViewDeferringInsetsCallback is both an WindowInsetsAnimation.Callback and an
        // OnApplyWindowInsetsListener, so needs to be set as so.
        ViewCompat.setWindowInsetsAnimationCallback(root, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(root, deferringInsetsListener)

        ViewCompat.setWindowInsetsAnimationCallback(
            bottomAppbar, TranslateDeferringInsetsAnimationCallback(
                view = bottomAppbar,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
                // We explicitly allow dispatch to continue down to binding.messageHolder's
                // child views, so that step 2.5 below receives the call
                dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
            )
        )
        ViewCompat.setWindowInsetsAnimationCallback(
            rvChats, TranslateDeferringInsetsAnimationCallback(
                view = rvChats,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )

        ViewCompat.setWindowInsetsAnimationCallback(
            editMessage, ControlFocusInsetsAnimationCallback(editMessage)
        )
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