package com.aking.aichat.ui.page

import android.view.View
import androidx.navigation.fragment.findNavController
import com.aking.aichat.BR
import com.aking.aichat.R
import com.aking.aichat.database.entity.ConversationEntity
import com.aking.aichat.database.entity.OwnerWithChats
import com.aking.aichat.databinding.FragmentHomeBinding
import com.aking.aichat.ui.adapter.ConversationAdapter
import com.aking.aichat.utl.Constants
import com.aking.aichat.utl.generateRandomName
import com.aking.aichat.vm.HomeViewModel
import com.txznet.common.ui.BaseVMFragment
import com.txznet.common.utils.currentSeconds

/**
 * Created by Rick at 2023/02/23 1:05
 * @Description //TODO $
 */
class HomeFragment : BaseVMFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    override fun getVMExtras(): Any? = null

    override fun FragmentHomeBinding.initView() {
        bindVariables(
            BR.viewModel to vm,
            BR.click to ClickProxy(),
            BR.adapter to ConversationAdapter(findNavController())
        )
    }

    override fun FragmentHomeBinding.initObservable() {
    }

    inner class ClickProxy {
        fun newChats(v: View) {
//            this@HomeFragment.apply {
//                exitTransition = MaterialElevationScale(false).apply {
//                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//                }
//                reenterTransition = MaterialElevationScale(true).apply {
//                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//                }
//            }
            val randomName = generateRandomName()
            val entity = ConversationEntity(
                randomName.hashCode(),
                randomName,
                Constants.avatars.random(),
                currentSeconds()
            )
            val ownerWithChats = OwnerWithChats(entity, emptyList())
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationChat(ownerWithChats)
            findNavController().navigate(action)
        }
    }
}