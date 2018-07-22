package cn.lsmya.restaurant.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.lsmya.restaurant.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
