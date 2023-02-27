package com.aking.aichat.ui.page

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.aking.aichat.BR
import com.aking.aichat.R
import com.aking.aichat.databinding.FragmentHomeBinding
import com.aking.aichat.vm.MainViewModel
import com.txznet.common.ui.BaseVMFragment

/**
 * Created by Rick at 2023/02/23 1:05
 * @Description //TODO $
 */
class HomeFragment : BaseVMFragment<FragmentHomeBinding, MainViewModel>(R.layout.fragment_home) {
    override fun getVMExtras(): Any? = null

    override fun FragmentHomeBinding.initView() {
        bindVariables(BR.viewModel to vm, BR.click to ClickProxy())
    }

    override fun FragmentHomeBinding.initObservable() {
    }

    inner class ClickProxy {
        fun newChats(v: View) {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_chat)
        }
    }
}