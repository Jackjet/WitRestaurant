package cn.lsmya.restaurant.setting

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import cn.lsmya.kotlin.apiclient.network.HttpRequest
import cn.lsmya.kotlin.apiclient.network.HttpResponse
import cn.lsmya.kotlin.apiclient.network.HttpResponseHandler
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.adapter.MessageAdapter
import cn.lsmya.restaurant.app.ROUTE
import cn.lsmya.restaurant.base.BaseRecyclerViewAdapter
import cn.lsmya.restaurant.model.MessageListModel
import cn.lsmya.restaurant.model.MessageModel
import cn.lsmya.restaurant.util.Ext.Companion.toast
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity(), BaseRecyclerViewAdapter.OnChildViewClickListener {
    override fun onViewClick(position: Int, id: Int) {
    }

    private var apiHandler = ApiHandler(this)
    private lateinit var httpRequest: HttpRequest

    private val limit = 10
    private var p = 1
    private lateinit var adapter: MessageAdapter
    private lateinit var list: ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        val linearLayoutManager = LinearLayoutManager(this)
        messageRecyclerView.layoutManager = linearLayoutManager
        adapter = MessageAdapter(this, list, this)
        messageRecyclerView.adapter = adapter
        httpRequest = HttpRequest(ROUTE.MESSAGE_LIST, this, apiHandler)
        getData()

    }

    private fun getData() {
        var map = HashMap<String, Any>()
//                map["store_id"] = App.getStoreId()
        map["store_id"] = 2
        map["is_read"] = 0
        map["limit"] = limit
        map["p"] = p
        httpRequest.setPost(map)
        httpRequest.setDebug(true)
        httpRequest.submit()
    }

    class ApiHandler(t: MessageActivity) : HttpResponseHandler<MessageActivity>(t) {
        override fun onSuccess(t: MessageActivity, request: HttpRequest, response: HttpResponse) {
            val data = response.getData(MessageListModel::class.java)
            if (data?.status == 1) {
                val list = data.data._list
                if (t.p == 1) {
                    t.list.clear()
                }
                t.list.addAll(list)
                t.adapter.notifyDataSetChanged()
                if (list.size == 0){
                    if (t.p == 1) {
                        t.messageRecyclerView.stopRefresh(true)
                    }else{
                        t.messageRecyclerView.stopLoadMore()
                    }
                }
            } else if (data?.status == 0) {
                t.toast(data?.info)
            }
        }
    }
}
