package com.stesso.android

import android.graphics.Color
import android.os.Bundle
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.flame.kotlinstudy.ui.MineFragment
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

    private fun init() {
        bottom_navigation_bar.addItem(BottomNavigationItem(R.drawable.home, "首页").setActiveColor(Color.RED))
                .addItem(BottomNavigationItem(R.drawable.info_icon, "NOW"))
                .addItem(BottomNavigationItem(R.drawable.search, "搜索"))
                .addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, "我的"))
                .setMode(BottomNavigationBar.MODE_FIXED)
                .initialise()

        bottom_navigation_bar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {

            }

            override fun onTabUnselected(position: Int) {
            }

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
        mCurrPosition = position
        var curr = supportFragmentManager.findFragmentByTag(tag[position])
        if (curr == null) {
            curr = when (position) {
                0 -> HomeFragment()
                1 -> MineFragment()
                else -> MineFragment()
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_fragment, curr, tag[position])
                    .commit()

        } else {
            supportFragmentManager
                    .beginTransaction()
                    .show(curr)
                    //.attach(curr)
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

}
