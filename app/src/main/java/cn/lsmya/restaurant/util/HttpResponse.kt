package cn.lsmya.kotlin.apiclient.network

import com.google.gson.Gson

class HttpResponse {
    private lateinit var route: String
    private lateinit var data: String
    private var success: Boolean = false

    constructor(route: String, data: String){
        this.route = route
        this.data = data
    }
    constructor(route: String, data: String, success: Boolean) {
        this.route = route
        this.data = data
        this.success = success
    }

    fun isRoute(url: String): Boolean = route == url

    fun isSuccess(): Boolean = success


    fun getData() = data

    fun setRoute(url: String) {
        route = url
    }

    fun setResult(result: String) {
        this.data = result
    }

    fun setSuccess(isSuccess: Boolean) {
        success = isSuccess
    }

    fun <T> getData(clazz: Class<T>): T? = Gson().fromJson(data, clazz)
}