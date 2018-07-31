package cn.lsmya.restaurant.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import cn.lsmya.library.base.BaseTitleActivity
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.app.App
import cn.lsmya.restaurant.app.ROUTE
import cn.lsmya.restaurant.util.Ext.Companion.toast
import com.alibaba.fastjson.JSON
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException

class LoginActivity : BaseTitleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var inflate = LayoutInflater.from(this).inflate(R.layout.activity_login, null)
        back.visibility = View.GONE
        setTitle("IDME商家中心")
        setContentView(inflate)
        if (App.isLogin()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        login_login.setOnClickListener {
            val mobile = login_mobile.text.toString()
            val password = login_password.text.toString()
            if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(password)) {
                toast("手机号或密码不能为空！")
            } else {
                login(mobile, password)
            }
        }
    }

    private fun login(mobile: String, password: String) {
        val build = FormBody.Builder()
                .add("username", mobile)
                .add("password", password)
                .build()
        val request = Request.Builder()
                .url(ROUTE.LOGIN)
                .post(build)
                .build()
        val mOkHttpClient = OkHttpClient()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val message = Message()
                message.what = 3
                handler.sendMessage(message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()?.string()
                val parseObject = JSON.parseObject(string)
                val status = parseObject["status"]
                if (status == 1) {
                    val message = Message()
                    message.what = 1
                    message.obj = parseObject["info"]
                    handler.sendMessage(message)
                } else {
                    val message = Message()
                    message.obj = parseObject["info"]
                    message.what = 2
                    handler.sendMessage(message)
                }
            }
        })
    }

    internal var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    toast("登录成功！")
                    App.setStoreId(msg.obj as String)
                    App.setLogin(true)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                2 -> {
                    toast(msg.obj as String)
                }
                3 -> {
                    toast("登录失败！")
                }
            }
        }
    }
}
