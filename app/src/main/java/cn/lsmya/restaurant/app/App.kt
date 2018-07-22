package cn.lsmya.restaurant.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sp = getSharedPreferences("app_data", Context.MODE_PRIVATE)
    }

    companion object {

        private var context: Context? = null
        private lateinit var sp: SharedPreferences

        fun getContext() = context

        fun isLogin() = sp?.getBoolean("login", false)

        fun setLogin(login: Boolean) {
            sp?.edit()?.putBoolean("login", login)?.commit()
        }

        fun getStoreId() = sp?.getString("store_id", "")

        fun setStoreId(store_id: String) {
            sp?.edit()?.putString("store_id", store_id)?.commit()
        }
    }
}