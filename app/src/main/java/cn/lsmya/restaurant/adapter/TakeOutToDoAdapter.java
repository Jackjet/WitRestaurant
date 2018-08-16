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

public class TakeOutToDoAdapter extends BaseRecyclerViewAdapter<TakeOutToDoAdapter.ViewHolder> {
    public TakeOutToDoAdapter(Context context, List list, OnChildViewClickListener onChildViewClickListener) {
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
        holder.todoClick.setOnClickListener(new OnViewClickListener(onChildViewClickListener, position, 1));
        holder.data.setText(model.getOrder_exhort());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_takeout_todo, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTakeOutToDo_name)
        TextView name;
        @BindView(R.id.itemTakeOutToDo_address)
        TextView address;
        @BindView(R.id.itemTakeOutToDo_money)
        TextView money;
        @BindView(R.id.itemTakeOutToDo_time)
        TextView time;
        @BindView(R.id.itemTakeOutToDo_data)
        TextView data;
        @BindView(R.id.itemTakeOutToDo_todoClick)
        Button todoClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
