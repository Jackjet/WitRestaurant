package cn.lsmya.restaurant.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup

class TabLayoutAdapter(fm: FragmentManager, var mFragments: ArrayList<Fragment>, var mTitles: ArrayList<String>) : FragmentStatePagerAdapter(fm) {


    override fun getItem(p0: Int): Fragment = mFragments[p0]

    override fun getCount(): Int = mFragments.size

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

    override fun getPageTitle(position: Int): CharSequence? = mTitles[position]

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position) as Fragment
    }
}