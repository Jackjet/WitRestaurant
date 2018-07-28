package cn.lsmya.library.PullLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PullRecyclerView extends RecyclerView implements PullView {
    private PullView.OnScrollListener listener;
    private OnDrawListener mOnDrawListener;

    public PullRecyclerView(Context context) {
        super(context);
    }

    public PullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canPullDown() {
        if (getChildCount() == 0) { // 没有item的时候也可以下拉刷新
            return true;
        } else if (getVerticalScrollbarPosition() == 0 && (getChildCount() == 0 || getChildAt(0).getTop() >= 0)) {   // 滑到ListView的顶部了
            return true;
        }
        return false;
    }

    @Override
    public boolean canPullUp() {
        //原先用的上拉加载更多方法
     /*   if (getChildCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        }

        if (!canScrollVertically(1)) {
            return true;
        } else {
            return false;
        }*/

        if (this == null) {
            return false;
        }
        return computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange();


/*
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = getScrollState();

        Log.e("xxxx", "底部 = " + visibleItemCount +", "+ lastVisibleItemPosition +", "+ (totalItemCount - 1) +", "+  state +", "+  SCROLL_STATE_IDLE);


        if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 ){
            Log.e("xxxx", "底部");
            return true;
        }else {
            Log.e("xxxx", "非底部");
            return false;
        }*/
        /*
        Log.e("xxxx", "底部 = " + canScrollVertically(1));

        if (computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()) {
            Log.e("xxxx", "底部");
            return true;
        }
        Log.e("xxxx", "非底部");
        */
/*

        LayoutManager layoutManager = getLayoutManager();

        //得到当前显示的最后一个item的view
        View lastChildView = layoutManager.getChildAt(layoutManager.getChildCount() - 1);
        //得到lastChildView的bottom坐标值
        int lastChildBottom = lastChildView.getBottom();
        //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
        int recyclerBottom = getBottom() - getPaddingBottom();
        //通过这个lastChildView得到这个view当前的position值
        int lastPosition = layoutManager.getPosition(lastChildView);
        //判断lastChildView的bottom值跟recyclerBottom
        //判断lastPosition是不是最后一个position
        //如果两个条件都满足则说明是真正的滑动到了底部
        Log.e("lastChildBottom===", lastChildBottom + ", " + recyclerBottom + ", ,,,, ," + lastPosition + ", " + (layoutManager.getItemCount() - 1));
        if (lastPosition == layoutManager.getItemCount() - 1) {
            Log.e("xxxx", "底部");
            return true;
        } else {
            Log.e("xxxx", "非底部");
            return false;
        }
*/
    }

    @Override
    public void setOnDrawListener(OnDrawListener listener) {
        mOnDrawListener = listener;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnDrawListener != null) {
            mOnDrawListener.onDraw(canvas);
        }
    }

    @Override
    public void setOnScrollListener(PullView.OnScrollListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (touchDown && !canScrollVertically(1)) {
                    if (listener != null) {
                        listener.onScrollBottom();
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private float preY;
    private boolean touchDown = false;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                preY = ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float currentY = ev.getY();
                float dy = currentY - preY;
                preY = currentY;
                touchDown = dy < 0;
            }
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }
}

