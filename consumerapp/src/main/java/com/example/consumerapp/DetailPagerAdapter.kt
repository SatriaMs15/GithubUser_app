package com.example.consumerapp

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    lateinit var username: String

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.tab_text_0,
                R.string.tab_text_1,
                R.string.tab_text_2
        )
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DetailFragment.newInstance(username)
            1 -> fragment = FollowerFragment.newInstance(username)
            2 -> fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }

}