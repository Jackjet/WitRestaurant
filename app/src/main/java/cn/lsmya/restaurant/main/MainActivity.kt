package cn.lsmya.restaurant.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.eat.EatFragment
import cn.lsmya.restaurant.setting.SettingFragment
import cn.lsmya.restaurant.takeOut.TakeOutFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainOneClick.setOnClickListener { choose(1) }
        mainTwoClick.setOnClickListener { choose(2) }
        mainThreeClick.setOnClickListener { choose(3) }
        choose(1)
    }

    private fun choose(position: Int) {
        when (position) {
            1 -> {
                replaceFragment(TakeOutFragment())
                mainOneImg.setImageResource(R.drawable.tab)
                mainTwoImg.setImageResource(R.drawable.tab_un)
                mainThreeImg.setImageResource(R.drawable.tab_un)
                mainOneText.setTextColor(ContextCompat.getColor(this, R.color.tabSelect))
                mainTwoText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainThreeText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainOneClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabSelectColor))
                mainTwoClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
                mainThreeClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
            }
            2 -> {
                replaceFragment(EatFragment())
                mainOneImg.setImageResource(R.drawable.tab_un)
                mainTwoImg.setImageResource(R.drawable.tab)
                mainThreeImg.setImageResource(R.drawable.tab_un)
                mainOneText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainTwoText.setTextColor(ContextCompat.getColor(this, R.color.tabSelect))
                mainThreeText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainOneClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
                mainTwoClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabSelectColor))
                mainThreeClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
            }
            3 -> {
                replaceFragment(SettingFragment())
                mainOneImg.setImageResource(R.drawable.tab_un)
                mainTwoImg.setImageResource(R.drawable.tab_un)
                mainThreeImg.setImageResource(R.drawable.tab)
                mainOneText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainTwoText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainThreeText.setTextColor(ContextCompat.getColor(this, R.color.tabSelect))
                mainOneClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
                mainTwoClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
                mainThreeClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabSelectColor))
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_fl, fragment)
        ft.commit()
    }
}
