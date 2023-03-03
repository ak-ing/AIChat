package com.aking.aichat

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

    private fun initView() {
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?)!!
        val navController = navHostFragment.navController
        binding.navView.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_setting
                ), binding.drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            it.setupWithNavController(navController)
        }
        // 设置左上角图标["三" —— "←"]效果
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            /*binding.toolbar, 传入toolbar就不会回调onOptionsItemSelected和onSupportNavigateUp */
            R.string.nav_open_drawer,
            R.string.nav_close_drawer
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)    // 0到1 ， 1是完全展开
                //实现侧滑动画,整个布局移动效果
                //获取抽屉的view
                val mContent = binding.drawerLayout.getChildAt(0)
                val scale = 1 - slideOffset          //1到0
                val endScale = 0.8f + scale * 0.2f   //最大1 到 最小0.8
                val startScale = 1 - 0.3f * scale    //最小0.7 到 最大 1

                //设置左边菜单滑动后的占据屏幕大小
                drawerView.scaleX = startScale
                drawerView.scaleY = startScale
                //设置菜单透明度
                drawerView.alpha = 0.6f + 0.4f * (1 - scale)  //0.6 到 1

                //设置内容界面水平和垂直方向偏转量
                mContent.translationX = drawerView.measuredWidth * (1 - scale)
                //设置内容界面操作无效（比如有button就会点击无效）
                mContent.invalidate()
                //设置右边菜单滑动后的占据屏幕大小
                mContent.scaleX = endScale
                mContent.scaleY = endScale
            }

            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
//                syncState()
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