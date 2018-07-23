package cn.lsmya.restaurant.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.lsmya.restaurant.R
import cn.lsmya.restaurant.base.BaseRecyclerViewAdapter
import cn.lsmya.restaurant.model.MessageModel
import java.util.ArrayList

class MessageAdapter(context: Context?, list: ArrayList<*>?, onChildViewClickListener: OnChildViewClickListener?) : BaseRecyclerViewAdapter<MessageAdapter.ViewHolder>(context, list, onChildViewClickListener) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        var model = list[position] as MessageModel
        holder.title.text = model.title
        holder.content.text = model.content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_list, parent, false))
    }

    class ViewHolder : RecyclerView.ViewHolder {

        lateinit var title: TextView
        lateinit var content: TextView

        constructor(itemView: View) : super(itemView) {
            title = itemView.findViewById(R.id.itemTitle)
            content = itemView.findViewById(R.id.itemTitle)
        }
    }
}