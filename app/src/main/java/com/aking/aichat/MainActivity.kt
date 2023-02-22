package com.aking.aichat

import androidx.navigation.fragment.NavHostFragment
import com.aking.aichat.databinding.ActivityMainBinding
import com.aking.aichat.vm.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.txznet.common.ui.BaseVMActivity

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override fun getVMExtras(): Any? = null

    override fun ActivityMainBinding.initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }

    override fun ActivityMainBinding.initObservable() {
    }

}