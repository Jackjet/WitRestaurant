package cn.lsmya.restaurant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lsmya.library.base.BaseRecyclerViewAdapter;
import cn.lsmya.library.util.TimeUtils;
import cn.lsmya.restaurant.R;
import cn.lsmya.restaurant.model.OrderDataModel;

public class TakeOutListAdapter extends BaseRecyclerViewAdapter<TakeOutListAdapter.ViewHolder> {
    public TakeOutListAdapter(Context context, List list, OnChildViewClickListener onChildViewClickListener) {
        super(context, list, onChildViewClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        OrderDataModel model = (OrderDataModel) list.get(position);
        holder.name.setText(model.getNickname());
        holder.address.setText(model.getOrder_address_log().getAddress());
        holder.money.setText("¥" + model.getOrder_total());
        holder.time.setText(TimeUtils.todayYyyyMmDdHhMmSs(new Date(Long.parseLong(model.getCreate_time()) * 1000)));
        switch (model.getStatus()) {
            case "pay":
                holder.todoClick.setVisibility(View.VISIBLE);
                holder.todoClick.setOnClickListener(new OnViewClickListener(onChildViewClickListener, position, 1));
                break;
            case "ship":
                holder.doingClick.setVisibility(View.VISIBLE);
                holder.doingClick.setOnClickListener(new OnViewClickListener(onChildViewClickListener, position, 2));
                break;
            case "success":
                break;
        }
        holder.data.setText(model.getOrder_exhort());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_takeout_list, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTakeOutList_name)
        TextView name;
        @BindView(R.id.itemTakeOutList_address)
        TextView address;
        @BindView(R.id.itemTakeOutList_money)
        TextView money;
        @BindView(R.id.itemTakeOutList_time)
        TextView time;
        @BindView(R.id.itemTakeOutList_data)
        TextView data;
        @BindView(R.id.itemTakeOutList_todoClick)
        Button todoClick;
        @BindView(R.id.itemTakeOutList_doingClick)
        Button doingClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
