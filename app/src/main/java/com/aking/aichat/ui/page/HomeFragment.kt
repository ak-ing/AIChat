package com.aking.aichat.ui.page

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.aking.aichat.BR
import com.aking.aichat.R
import com.aking.aichat.databinding.FragmentHomeBinding
import com.aking.aichat.databinding.ItemConversationBinding
import com.aking.aichat.ui.adapter.ConversationAdapter
import com.aking.aichat.ui.helper.ConversationTouchHelper
import com.aking.aichat.utl.buildOwnerWithChats
import com.aking.aichat.vm.HomeViewModel
import com.aking.openai.database.entity.OwnerWithChats
import com.google.android.material.transition.MaterialElevationScale
import com.txznet.common.ui.BaseVMFragment

/**
 * Created by Rick at 2023/02/23 1:05
 * @Description //TODO $
 */
class HomeFragment : BaseVMFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home),
    ConversationAdapter.ConversationClickListener {
    override fun getVMExtras(): Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enterTransition = MaterialFadeThrough().apply {
//            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//        }
    }

    override fun FragmentHomeBinding.initView() {
        // Postpone enter transitions to allow shared element transitions to run.
        // https://github.com/googlesamples/android-architecture-components/issues/495
        postponeEnterTransition()
        view?.doOnPreDraw { startPostponedEnterTransition() }

        bindVariables(
            BR.viewModel to vm,
            BR.click to ClickProxy(),
            BR.adapter to ConversationAdapter(this@HomeFragment)
        )
        ItemTouchHelper(ConversationTouchHelper(adapter!!)).attachToRecyclerView(rvConversation)
    }

    override fun FragmentHomeBinding.initObservable() {
    }

    //会话点击
    override fun onItemClick(it: OwnerWithChats, bind: ItemConversationBinding) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        val emailCardDetailTransitionName = getString(R.string.chat_detail_transition_name)
        val extras = FragmentNavigatorExtras(bind.root to emailCardDetailTransitionName)
        val directions = HomeFragmentDirections.actionNavigationHomeToNavigationChat(it)
        findNavController().navigate(directions, extras)
    }

    //会话删除
    override fun onDelete(it: OwnerWithChats) {
        vm.deleteConversation(it)
    }

    //会话移动
    override fun onMoved(it: List<OwnerWithChats>) {
        vm.submitList(it)
    }

    inner class ClickProxy {
        fun newChats(v: View) {
            this@HomeFragment.apply {
                exitTransition = null
                reenterTransition = null
            }

            val action =
                HomeFragmentDirections.actionNavigationHomeToNavigationChat(buildOwnerWithChats())
            findNavController().navigate(action)
        }
    }
}