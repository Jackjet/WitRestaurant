package cn.lsmya.restaurant.view.RecyclerViewAdapter;

import android.view.View;

public interface OnBindView {
    void bindView(int position, Object data, View itemView, ViewClickListener.OnChildViewClickListener viewClickListener);
}
