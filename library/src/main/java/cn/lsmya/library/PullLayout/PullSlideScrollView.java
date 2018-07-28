package cn.lsmya.library.PullLayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 可防止ScrollView和横向的ListView滑动事件发生冲突
 */
public class PullSlideScrollView extends ScrollView implements PullView {
    private OnScrollListener listener;
    private OnScrollChangeListener mOnScrollChangeListener;
    private OnDrawListener mOnDrawListener;


    public PullSlideScrollView(Context context) {
        super(context);
    }

    public PullSlideScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
    }

    public PullSlideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullSlideScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canPullDown() {
        return getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        return getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight());
    }

    @Override
    public void setOnScrollListener(PullView.OnScrollListener listener) {
        this.listener = listener;
    }

    private int preY = 0;

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        int dy = scrollY - preY;
        preY = scrollY;
        if (dy > 0 && null != listener && clampedY) {
            listener.onScrollBottom();
        }
        /*
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChange(this, scrollX, scrollY, scrollX, scrollY);
        }*/
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /*
        if (getScrollY() + getHeight() >= computeVerticalScrollRange() && listener != null) {
            listener.onScrollBottom();
        }
        */
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }


    @Override
    public void setOnDrawListener(OnDrawListener listener) {
        mOnDrawListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnDrawListener != null) {
            mOnDrawListener.onDraw(canvas);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        this.mOnScrollChangeListener = l;
    }


    public interface OnScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v          The view whose scroll position has changed.
         * @param scrollX    Current horizontal scroll origin.
         * @param scrollY    Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChange(PullSlideScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }


    /**
     * 以下代码：防止发生滑动冲突
     */
    private GestureDetector mGestureDetector;
    OnTouchListener mGestureListener;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}
