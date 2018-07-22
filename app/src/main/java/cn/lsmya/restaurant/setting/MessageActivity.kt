package cn.lsmya.restaurant.setting

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.lsmya.kotlin.apiclient.network.HttpRequest
import cn.lsmya.kotlin.apiclient.network.HttpResponse
import cn.lsmya.kotlin.apiclient.network.HttpResponseHandler
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.app.App
import cn.lsmya.restaurant.app.ROUTE

class MessageActivity : AppCompatActivity() {

    private var apiHandler = ApiHandler(this)
    private lateinit var httpRequest: HttpRequest

    private val limit = 10
    private var p = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        httpRequest = HttpRequest(ROUTE.MESSAGE_LIST, this, apiHandler)
        httpRequest.setDebug(true)
        httpRequest.submit()

    }
    class ApiHandler(t: MessageActivity) : HttpResponseHandler<MessageActivity>(t) {
        override fun onBefore(t: MessageActivity, request: HttpRequest) {
            super.onBefore(t, request)
            if (request.isRoute(ROUTE.MESSAGE_LIST)){
                var map = HashMap<String,Any>()
//                map["store_id"] = App.getStoreId()
                map["store_id"] = 2
                map["is_read"] = 0
                map["limit"] = t.limit
                map["p"] = t.p
                request.setPost(map)
            }
        }
        override fun onSuccess(t: MessageActivity, request: HttpRequest, response: HttpResponse) {
        }
    }
}
