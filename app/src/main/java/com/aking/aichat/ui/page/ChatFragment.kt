package com.aking.aichat.ui.page

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.navArgs
import com.aking.aichat.BR
import com.aking.aichat.R
import com.aking.aichat.databinding.FragmentChatBinding
import com.aking.aichat.ui.adapter.ChatAdapter
import com.aking.aichat.ui.helper.ControlFocusInsetsAnimationCallback
import com.aking.aichat.ui.helper.TranslateViewInsetsAnimationListener
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

        //OnApplyWindowInsetsListener会自动更新我们的根视图向上移动，根据输入法和底部虚拟导航栏的size
        root.setOnApplyWindowInsetsListener { rootView, windowInsets ->    /*BottomAppBar设置wrap_content同样效果*/
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets, rootView).also {
                val barsIme = it.getInsets(WindowInsetsCompat.Type.ime())
                bottomAppbar.updatePadding(bottom = barsIme.bottom)
            }
            windowInsets
        }

        //当点击文字输入框的时候，应用跟随着软键盘一起移动并且创造了一个更流畅的体验
        ViewCompat.setWindowInsetsAnimationCallback(
            root, TranslateViewInsetsAnimationListener(
                bottomAppbar, WindowInsetsCompat.Type.ime()
            )
        )

        ViewCompat.setWindowInsetsAnimationCallback(
            rvChats, TranslateViewInsetsAnimationListener(
                rvChats, WindowInsetsCompat.Type.ime()
            )
        )

        //自动处理焦点
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