package cn.lsmya.restaurant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class EatListDoingAdapter extends BaseRecyclerViewAdapter<EatListDoingAdapter.ViewHolder> {
    public EatListDoingAdapter(Context context, List list, OnChildViewClickListener onChildViewClickListener) {
        super(context, list, onChildViewClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        OrderDataModel model = (OrderDataModel) list.get(position);
        holder.name.setText(model.getNickname());
        String seatDesk = model.getSeat_desk();
        String seatNumber = model.getSeat_number();
        if (TextUtils.isEmpty(seatDesk) || seatDesk.equals("null")
                || TextUtils.isEmpty(seatNumber) || seatNumber.equals("null")) {
            holder.address.setText(null);
        } else {
            holder.address.setText(model.getSeat_desk() + " " + model.getSeat_number());

        }
        holder.money.setText("¥" + model.getOrder_total());
        holder.time.setText(TimeUtils.todayYyyyMmDdHhMmSs(new Date(Long.parseLong(model.getCreate_time()) * 1000)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_eat_doing, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemEatDoing_name)
        TextView name;
        @BindView(R.id.itemEatDoing_address)
        TextView address;
        @BindView(R.id.itemEatDoing_money)
        TextView money;
        @BindView(R.id.itemEatDoing_time)
        TextView time;
        @BindView(R.id.itemEatDoing_data)
        TextView data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
