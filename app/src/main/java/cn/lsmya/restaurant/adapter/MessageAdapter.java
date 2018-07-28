package cn.lsmya.restaurant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lsmya.restaurant.R;
import cn.lsmya.library.base.BaseRecyclerViewAdapter;
import cn.lsmya.restaurant.model.MessageModel;

public class MessageAdapter extends BaseRecyclerViewAdapter<MessageAdapter.ViewHolder> {
    public MessageAdapter(Context context, List list, OnChildViewClickListener onChildViewClickListener) {
        super(context, list, onChildViewClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        MessageModel model = (MessageModel) list.get(position);
        holder.title.setText(model.getTitle());
        holder.content.setText(model.getContent());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTitle)
        TextView title;
        @BindView(R.id.itemContent)
        TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
