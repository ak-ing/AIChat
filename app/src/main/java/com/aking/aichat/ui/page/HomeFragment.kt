package com.aking.aichat.ui.page

import androidx.appcompat.app.AppCompatActivity
import com.aking.aichat.BR
import com.aking.aichat.R
import com.aking.aichat.databinding.FragmentHomeBinding
import com.aking.aichat.ui.helper.TouchSizeHelper
import com.aking.aichat.vm.MainViewModel
import com.txznet.common.ui.BaseVMFragment

/**
 * Created by Rick at 2023/02/23 1:05
 * @Description //TODO $
 */
class HomeFragment : BaseVMFragment<FragmentHomeBinding, MainViewModel>(R.layout.fragment_home) {
    override fun getVMExtras(): Any? = null

    override fun FragmentHomeBinding.initView() {
        setVariable(BR.viewModel, vm)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbarHistory)
        dragContent.setOnTouchListener(TouchSizeHelper())
    }

    override fun FragmentHomeBinding.initObservable() {
    }
}