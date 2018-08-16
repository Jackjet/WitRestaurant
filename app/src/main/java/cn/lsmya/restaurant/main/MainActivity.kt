package cn.lsmya.restaurant.main

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.app.App
import cn.lsmya.restaurant.app.ROUTE
import cn.lsmya.restaurant.eat.EatFragment
import cn.lsmya.restaurant.model.UserDataModel
import cn.lsmya.restaurant.setting.SettingFragment
import cn.lsmya.restaurant.takeOut.TakeOutFragment
import com.alibaba.fastjson.JSON
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainOneClick.setOnClickListener { choose(1) }
        mainTwoClick.setOnClickListener { choose(2) }
        mainThreeClick.setOnClickListener { choose(3) }
        choose(1)
        getData()
    }

    private fun choose(position: Int) {
        when (position) {
            1 -> {
                replaceFragment(TakeOutFragment())
                mainOneImg.setImageResource(R.drawable.take_out)
                mainTwoImg.setImageResource(R.drawable.eat_un)
                mainThreeImg.setImageResource(R.drawable.setting_un)
                mainOneText.setTextColor(ContextCompat.getColor(this, R.color.tabSelect))
                mainTwoText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainThreeText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainOneClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabSelectColor))
                mainTwoClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
                mainThreeClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
            }
            2 -> {
                replaceFragment(EatFragment())
                mainOneImg.setImageResource(R.drawable.take_out_un)
                mainTwoImg.setImageResource(R.drawable.eat)
                mainThreeImg.setImageResource(R.drawable.setting_un)
                mainOneText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainTwoText.setTextColor(ContextCompat.getColor(this, R.color.tabSelect))
                mainThreeText.setTextColor(ContextCompat.getColor(this, R.color.tabUnSelect))
                mainOneClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
                mainTwoClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabSelectColor))
                mainThreeClick.setBackgroundColor(ContextCompat.getColor(this, R.color.tabUnSelectColor))
            }
            3 -> {
                replaceFragment(SettingFragment())
                mainOneImg.setImageResource(R.drawable.take_out_un)
                mainTwoImg.setImageResource(R.drawable.eat_un)
                mainThreeImg.setImageResource(R.drawable.setting)
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

    private fun getData() {
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

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    var str = msg.obj as String
                    val model = JSON.parseObject(str, UserDataModel::class.java)
                    App.setLogo(model.data.logo_path)
                    App.setName(model.data.store_name)
                }
            }
        }
    }
}
