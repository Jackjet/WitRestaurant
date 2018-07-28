package cn.lsmya.library.PullLayout;

import android.graphics.Canvas;

public interface PullView {
    /**
     * 判断是否可以下拉，如果不需要下拉功能可以直接return false
     *
     * @return true如果可以下拉否则返回false
     */
    boolean canPullDown();

    /**
     * 判断是否可以上拉，如果不需要上拉功能可以直接return false
     *
     * @return true如果可以上拉否则返回false
     */
    boolean canPullUp();

    void setOnScrollListener(OnScrollListener listener);

    interface OnScrollListener {
        // 滚动到最底部时执行
        void onScrollBottom();
    }

    void setOnDrawListener(OnDrawListener listener);

    interface OnDrawListener {
        void onDraw(Canvas canvas);
    }
}
