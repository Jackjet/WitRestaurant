package cn.lsmya.restaurant.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import cn.lsmya.library.base.BaseFragment
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.app.App
import cn.lsmya.restaurant.app.ROUTE
import cn.lsmya.restaurant.main.LoginActivity
import cn.lsmya.restaurant.model.UserDataModel
import cn.lsmya.restaurant.util.Glide.GlideUtils
import com.alibaba.fastjson.JSON
import kotlinx.android.synthetic.main.fragment_setting.*
import okhttp3.*
import java.io.IOException

class SettingFragment : BaseFragment(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun onViewCreated(view: View) {
        settingOrder.setOnClickListener(this)
        settingMessage.setOnClickListener(this)
        settingLogout.setOnClickListener(this)
        if (TextUtils.isEmpty(App.getLogo()) || TextUtils.isEmpty(App.getName())) {
            getData()
        } else {
            GlideUtils.getInstence().loadCircle(activity, App.getLogo(), settingLogo)
            settingName.text = App.getName()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.settingOrder -> {
                startActivity(Intent(activity, OrderActivity::class.java))
            }
            R.id.settingMessage -> {
                startActivity(Intent(activity, MessageActivity::class.java))
            }
            R.id.settingLogout -> {
                App.setLogin(false)
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
        }
    }

    fun getData() {
        var okHttpClient = OkHttpClient()

        var requestBody = FormBody.Builder()
                .add("store_id", App.getStoreId())
                .build()
        var request = Request.Builder()
                .url(ROUTE.USER_INFO)
                .post(requestBody)
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response) {
                val code = response.code()
                if (code == 200) {
                    val string = response.body()?.string()
                    var message = Message()
                    message.what = 1
                    message.obj = string
                    handler.sendMessage(message)
                }
            }

        })

    }

    val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    var str = msg.obj as String
                    val model = JSON.parseObject(str, UserDataModel::class.java)
                    if (model.status == 1) {
                        GlideUtils.getInstence().loadCircle(activity, model.data.logo_path, settingLogo)
                        settingName.text = model.data.store_name
                    }
                }
                2 -> {

                }
            }
        }
    }

}