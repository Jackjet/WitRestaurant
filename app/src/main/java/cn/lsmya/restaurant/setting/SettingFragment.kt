package cn.lsmya.restaurant.setting

import android.content.Intent
import android.view.View
import cn.lsmya.library.base.BaseFragment
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.app.App
import cn.lsmya.restaurant.main.LoginActivity
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun onViewCreated(view: View) {
        settingOrder.setOnClickListener(this)
        settingMessage.setOnClickListener(this)
        settingLogout.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.settingOrder -> {
                startActivity(Intent(activity,OrderActivity::class.java))
            }
            R.id.settingMessage -> {
                startActivity(Intent(activity,MessageActivity::class.java))
            }
            R.id.settingLogout -> {
                App.setLogin(false)
                startActivity(Intent(activity,LoginActivity::class.java))
                activity?.finish()
            }
        }
    }
}