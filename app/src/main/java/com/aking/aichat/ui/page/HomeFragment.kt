package com.aking.aichat.ui.page

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
    }

    override fun FragmentHomeBinding.initObservable() {
    }
}