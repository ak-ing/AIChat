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
import com.aking.aichat.ui.helper.RootViewDeferringInsetsCallback
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
        val str = "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试"
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)
        vm.postRequest(str)


//        val deferringInsetsListener = RootViewDeferringInsetsCallback(
//            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
//            deferredInsetTypes = WindowInsetsCompat.Type.ime()
//        )
//        // RootViewDeferringInsetsCallback is both an WindowInsetsAnimation.Callback and an
//        // OnApplyWindowInsetsListener, so needs to be set as so.
//        ViewCompat.setWindowInsetsAnimationCallback(root, deferringInsetsListener)
//        ViewCompat.setOnApplyWindowInsetsListener(root, deferringInsetsListener)
//
//        ViewCompat.setWindowInsetsAnimationCallback(
//            bottomAppbar, TranslateDeferringInsetsAnimationCallback(
//                view = bottomAppbar,
//                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
//                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
//                // We explicitly allow dispatch to continue down to binding.messageHolder's
//                // child views, so that step 2.5 below receives the call
//                dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
//            )
//        )
//        ViewCompat.setWindowInsetsAnimationCallback(
//            rvChats, TranslateDeferringInsetsAnimationCallback(
//                view = rvChats,
//                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
//                deferredInsetTypes = WindowInsetsCompat.Type.ime()
//            )
//        )
//
//        ViewCompat.setWindowInsetsAnimationCallback(
//            editMessage, ControlFocusInsetsAnimationCallback(editMessage)
//        )

        root.setOnApplyWindowInsetsListener { rootView, windowInsets ->    /*BottomAppBar设置wrap_content同样效果*/
            //OnApplyWindowInsetsListener会自动更新我们的根视图向上移动，根据输入法和底部虚拟导航栏的size
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets, rootView).also {
                val barsIme = it.getInsets(WindowInsetsCompat.Type.ime())
                bottomAppbar.updatePadding(bottom = barsIme.bottom)
            }
            windowInsets
        }


        //TranslateViewInsetsAnimationListener会自动它会自动移动他的绑定视图作为软键盘动画
//        ViewCompat.setWindowInsetsAnimationCallback(rvChats, TranslateViewInsetsAnimationListener(
//            rvChats,
//            WindowInsetsCompat.Type.ime()
//        ))
//
        //当点击文字输入框的时候，应用跟随着软键盘一起移动并且创造了一个更流畅的体验
        ViewCompat.setWindowInsetsAnimationCallback(
            root, TranslateViewInsetsAnimationListener(
                bottomAppbar,
                WindowInsetsCompat.Type.ime()
            )
        )
        //通过监听用户对RecycleView滚动监听来控制拖动打开IME（软键盘）
//        root.setOnTouchListener(InsetsAnimationTouchListener())
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