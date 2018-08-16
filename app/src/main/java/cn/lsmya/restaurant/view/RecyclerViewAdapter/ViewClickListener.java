package cn.lsmya.restaurant.view.RecyclerViewAdapter;

import android.view.View;

/**
 * view的点击事件
 */
public class ViewClickListener implements View.OnClickListener {
    OnChildViewClickListener onViewClickListener;
    int position;
    int id;

    public ViewClickListener(OnChildViewClickListener onViewClickListener, int position, int id) {
        this.onViewClickListener = onViewClickListener;
        this.position = position;
        this.id = id;
    }

    @Override
    public void onClick(View v) {
        onViewClickListener.onChildClick(position, id);
    }
    /**
     * item中子view的点击事件（回调）
     */
    public interface OnChildViewClickListener {
        /**
         * @param position adapter_recycler_view_item position
         * @param id       点击的view的id，调用时根据不同的view传入不同的id加以区分
         */
        void onChildClick(int position, int id);
    }
}