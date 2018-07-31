package cn.lsmya.restaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lsmya.restaurant.R;
import cn.lsmya.restaurant.model.GoodsModel;

public class FoodsAdapter extends BaseAdapter {
    private Context context;
    private List<GoodsModel> list;

    public FoodsAdapter(Context context, List<GoodsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GoodsModel getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder ho = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_food_list, null);
            ho = new ViewHolder(view);
            view.setTag(ho);
        } else {
            ho = (ViewHolder) view.getTag();
        }
        ho.name.setText(getItem(i).getGoods_title());
        ho.money.setText("¥" + getItem(i).getGoods_price() + "*" + getItem(i).getBuy_num());
        return view;
    }

    class ViewHolder {
        private TextView name;
        private TextView money;

        public ViewHolder(View view) {
            name = view.findViewById(R.id.itemFoodsList_name);
            money = view.findViewById(R.id.itemFoodsList_money);
        }
    }
}