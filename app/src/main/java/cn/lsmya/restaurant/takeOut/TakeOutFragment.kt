package cn.lsmya.restaurant.takeOut

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import cn.lsmya.restaurant.base.BaseFragment
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.adapter.TabLayoutAdapter
import kotlinx.android.synthetic.main.fragment_take_out_eat.*

class TakeOutFragment : BaseFragment() {
    private lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var titleList: ArrayList<String>

    private lateinit var toDoFragment: TakeOutToDoFragment
    private lateinit var doingFragment: TakeOutDoingFragment
    private lateinit var doneFragment: TakeOutDoneFragment

    override val layoutId: Int
        get() = R.layout.fragment_take_out_eat

    override fun onViewCreated(view: View) {
        takeOutEatTitle.text = "外卖"
        fragmentList = ArrayList()
        titleList = ArrayList()
        titleList.add("待派送")
        toDoFragment = TakeOutToDoFragment()
        fragmentList.add(toDoFragment)
        titleList.add("派送中")
        doingFragment = TakeOutDoingFragment()
        fragmentList.add(doingFragment)
        titleList.add("已派送")
        doneFragment = TakeOutDoneFragment()
        fragmentList.add(doneFragment)
        takeOutEatViewPager.adapter = TabLayoutAdapter(childFragmentManager, fragmentList, titleList)
        takeOutEatTabLayout.setupWithViewPager(takeOutEatViewPager)
        takeOutEatViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                takeOutEatViewPager.adapter?.saveState()
            }
        })
        takeOutEatViewPager.offscreenPageLimit = fragmentList.size
    }

}