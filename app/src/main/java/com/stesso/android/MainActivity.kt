package com.stesso.android

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import cn.jzvd.Jzvd
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.bottom_navigation_bar

class MainActivity : BaseActivity() {

    private val CURR_POSITION = "curr_position"
    private val PRE_POSITION = "pre_position"
    private var mCurrPosition: Int = 0
    private var mPrePosition: Int = 0

    private val tag = arrayOf("home", "discover", "news", "person")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            mCurrPosition = savedInstanceState.getInt(CURR_POSITION, 0)
            mPrePosition = savedInstanceState.getInt(PRE_POSITION, -1)
        } else {
            mPrePosition = -1
            mCurrPosition = 0
        }
        init()
        fillFragment(mCurrPosition)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val index =intent?.getIntExtra(INDEX,0)
        bottom_navigation_bar.selectTab(index?:0)
    }

    private fun init() {
        bottom_navigation_bar.addItem(BottomNavigationItem(R.drawable.icon1_drawable, "首页"))
                .addItem(BottomNavigationItem(R.drawable.icon2_drawable, "NOW"))
                .addItem(BottomNavigationItem(R.drawable.icon3_drawable, "搜索"))
                .addItem(BottomNavigationItem(R.drawable.icon4_drawable, "我的"))
                .setMode(BottomNavigationBar.MODE_FIXED)
                .initialise()

        bottom_navigation_bar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {}

            override fun onTabUnselected(position: Int) {}

            override fun onTabSelected(position: Int) {
                fillFragment(position)
            }
        })

    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURR_POSITION, mCurrPosition)
        outState.putInt(PRE_POSITION, mPrePosition)
        super.onSaveInstanceState(outState)
    }

    private fun fillFragment(position: Int) {
        if (mPrePosition == position) {
            return
        }
        MineFragment.reload = true
        mCurrPosition = position
        var curr = supportFragmentManager.findFragmentByTag(tag[position])
        if (curr == null) {
            curr = when (position) {
                0 -> HomeFragment()
                1 -> NewsFragment()
                2 -> SearchFragment()
                else -> {
                    MineFragment()
                }
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_fragment, curr, tag[position])
                    .commit()

        } else {
            supportFragmentManager
                    .beginTransaction()
                    .show(curr)
                   // .attach(curr)
                    .commit()
        }
        if (mPrePosition != -1) {
            val pre = supportFragmentManager.findFragmentByTag(tag[mPrePosition])
            if (pre != null) {
                supportFragmentManager
                        .beginTransaction()
                        .hide(pre)
                        //.detach(pre)
                        .commit()
            }
        }
        mPrePosition = mCurrPosition
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

}
