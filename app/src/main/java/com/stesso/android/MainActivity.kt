package com.stesso.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.bottom_navigation_bar

class MainActivity : AppCompatActivity() {

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

    }

    fun init() {
        bottom_navigation_bar.addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, "dd"))
                .addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, "dd"))
                .addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, "dd"))
                .addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, "dd"))
                .setMode(BottomNavigationBar.MODE_FIXED)
                .initialise()
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
                1 -> HomeFragment()
                else -> HomeFragment()
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
