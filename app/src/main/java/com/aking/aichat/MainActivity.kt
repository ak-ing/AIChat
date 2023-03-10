package com.aking.aichat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aking.aichat.databinding.ActivityMainBinding
import com.aking.aichat.utl.Constants
import com.txznet.common.utils.contentView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initView()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val extra = intent.getIntExtra(Constants.CONVERSATION_ID, -1)
    }

    private fun initView() {
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?)!!
        val navController = navHostFragment.navController
        binding.navView.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_setting
                ), binding.drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            it.setupWithNavController(navController)
        }
        // ?????????????????????["???" ?????? "???"]??????
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout,
            /*binding.toolbar, ??????toolbar???????????????onOptionsItemSelected???onSupportNavigateUp */
            R.string.nav_open_drawer, R.string.nav_close_drawer
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)    // 0???1 ??? 1???????????????
                //??????????????????,????????????????????????
                //???????????????view
                val mContent = binding.drawerLayout.getChildAt(0)
                val scale = 1 - slideOffset          //1???0
                val endScale = 0.8f + scale * 0.2f   //??????1 ??? ??????0.8
                val startScale = 1 - 0.3f * scale    //??????0.7 ??? ?????? 1

                //????????????????????????????????????????????????
                drawerView.scaleX = startScale
                drawerView.scaleY = startScale
                //?????????????????????
                drawerView.alpha = 0.6f + 0.4f * (1 - scale)  //0.6 ??? 1

                //????????????????????????????????????????????????
                mContent.translationX = drawerView.measuredWidth * (1 - scale)
                //??????????????????????????????????????????button?????????????????????
                mContent.invalidate()
                //????????????????????????????????????????????????
                mContent.scaleX = endScale
                mContent.scaleY = endScale
            }
        }
        actionBarDrawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.app_bar_navigation, menu)
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                if (binding.drawerLayout.getDrawerLockMode(binding.navView) != LOCK_MODE_UNLOCKED) {
                    binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                }
                binding.toolbar.menu.findItem(R.id.navigation_search).isVisible = true
            } else {
                binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                binding.toolbar.menu.findItem(R.id.navigation_search).isVisible = false
            }
        }
        return result
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_search -> {
                val navController = findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.action_navigation_home_to_navigation_search)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}