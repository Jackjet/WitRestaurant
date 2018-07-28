package cn.lsmya.library.PullLayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.lsmya.library.R;


public class PullLayout extends RelativeLayout {

    // 刷新成功
    public static final int SUCCEED = 0;

    // 刷新全部
    public static final int ALL = 2;

    // 刷新失败
    public static final int FAIL = 1;

    // 刷新取消
    public static final int CANCEL = 3;

    //是否可以下拉
    private boolean canRefresh = true;

    // 是否可以上拉
    private boolean canLoadMore = true;

    // 监听器
    private List<OnPullListener> onPullListenerLists;

    // 上次点击的坐标
    private float preY;

    // mListView和pullView是同一个类
    private View mListView;
    private PullView pullView;

    // 头部 刷新view
    private View refreshView;
    private int refreshViewHeight;

    // 底部 加载更多view
    public View loadMoreView;
    private int loadMoreViewHeight;

    // 下拉距离
    private float pullDown;

    //上拉距离
    private float pullUp;

    private boolean inLoadMore;
    private boolean inRefresh;

    // 下拉刷新时ready前的mStatus用于在取消后恢复mStatus的值
    private int mLastStatus = M_STATUS_NONE;

    // 当前状态
    private int mStatus = M_STATUS_NONE;

    public static final int M_STATUS_NONE = 0;// 什么也木干

    private static final int M_STATUS_LOAD_MORE_ING = 1;// 正在加载更多
    private static final int M_STATUS_LOAD_MORE_ALL = 2;//已加载全部
    private static final int M_STATUS_LOAD_MORE_DONE = 3;//加载完成

    private static final int M_STATUS_REFRESH_READY = 4;// 松开立即刷新
    private static final int M_STATUS_REFRESH_ING = 5;// 正在刷新
    private static final int M_STATUS_REFRESH_DONE = 6;// 刷新完成

    // 下拉刷新速度变焦
    private float pullDownRadio = 2;

    // 过滤多点触控
    private boolean isMultiplePoints = false;

    // 下拉回弹
    private RefreshRollbackHandler refreshRollbackHandler;

    // 回弹是否跳到正在刷新
    private boolean isShowRefreshView;

    // 上次更新时间
    private long lastRefreshTime = -1;

    // 正在加载
    private View loadingView;
    private boolean isShowLoading;
    private View errorView;
    private boolean isShowError;
    private String showErrorTitle;
    private int showErrorImg;
    private Toast errorAlertToast;

    // 下拉刷新箭头旋转180度
    private RotateAnimation rotateAnimation;

    private HolderViewLoadMore holderViewLoadMore;
    private HolderViewRefresh holderViewRefresh;

    /**
     * 刷新
     *
     * @param silent 是否静默，true则不显示下拉效果
     */
    public void refresh(boolean silent) {
        cancelRefreshRollback();
        if (canRefresh && !silent && pullView.canPullDown()) {
            mListView.scrollTo(0, 0);
            pullDown = refreshViewHeight;
            inRefresh = true;
        } else {
            pullDown = 0;
            inRefresh = false;
        }
        requestLayout();
        _refresh();
    }

    /**
     * 加载更多
     *
     * @param silent 是否静默，true则不显示上拉效果
     */
    public void loadMore(boolean silent) {
        if (mStatus != M_STATUS_NONE && mStatus != M_STATUS_LOAD_MORE_ALL) {
            return;
        }
        if (canLoadMore && !silent && pullView.canPullUp()) {
            pullUp = -loadMoreViewHeight;
            inLoadMore = true;
            requestLayout();
        }
        if (mStatus == M_STATUS_NONE) {
            _load_more();
        } else if (mStatus == M_STATUS_LOAD_MORE_ALL) {// 已加载全部
            setLoadMoreStatus(M_STATUS_LOAD_MORE_ALL, -1);
        }
    }

    // 初次刷新
    private boolean _first_refresh = true;

    // 刷新
    private void _refresh() {
        setRefreshStatus(M_STATUS_REFRESH_ING, -1);
        for (OnPullListener listener : onPullListenerLists) {
            listener.onRefresh(this, _first_refresh);
        }
        if (_first_refresh) {
            _first_refresh = false;
        }
    }

    // 加载更多咯
    private void _load_more() {
        setLoadMoreStatus(M_STATUS_LOAD_MORE_ING, -1);
        for (OnPullListener listener : onPullListenerLists) {
            listener.onLoadMore(this);
        }
    }

    // ApiClientResponseWithPullHandler回调
    public void setStatus(int net_status) {
        if (mStatus == M_STATUS_LOAD_MORE_ING) {// 当前 正在加载更多
            setLoadMoreStatus(M_STATUS_LOAD_MORE_DONE, net_status);

        } else if (mStatus == M_STATUS_REFRESH_ING) {// 当前 正在刷新
            setRefreshStatus(M_STATUS_REFRESH_DONE, net_status);

        } else {
            mStatus = M_STATUS_NONE;
            inRefresh = inLoadMore = false;
            pullUp = pullDown = 0;
        }
    }

    private class HolderViewLoadMore {
        TextView loadFull;
        TextView more;
        ProgressBar loading;
    }

    // 上拉加载更多 - 负责更新状态 并 改界面
    private void setLoadMoreStatus(int status, int net_status) {
        if (status == M_STATUS_LOAD_MORE_ING && mStatus == M_STATUS_LOAD_MORE_ING) {
            return;
        }

        TextView loadFull = holderViewLoadMore.loadFull;
        TextView more = holderViewLoadMore.more;
        ProgressBar loading = holderViewLoadMore.loading;

        if (status == M_STATUS_LOAD_MORE_ING) {// 正在加载更多
            mStatus = M_STATUS_LOAD_MORE_ING;
            more.setText("正在加载...");
            more.setVisibility(VISIBLE);
            loading.setVisibility(VISIBLE);
            loadFull.setVisibility(GONE);

        } else if (status == M_STATUS_LOAD_MORE_DONE) {// 加载完成
            more.setVisibility(GONE);
            loading.setVisibility(GONE);
            if (net_status == ALL) {
                loadFull.setVisibility(VISIBLE);
                mStatus = M_STATUS_LOAD_MORE_ALL;
                if (pullView.canPullUp()) {
                    pullUp = -loadMoreViewHeight;
                    inLoadMore = true;
                    _onDrawCheckLoadMorePullUp = true;
                } else {
                    pullUp = 0;
                    inLoadMore = false;
                }
            } else {
                loadFull.setVisibility(GONE);
                mStatus = M_STATUS_NONE;
                pullUp = 0;
                inLoadMore = false;
            }
            requestLayout();
        } else if (status == M_STATUS_LOAD_MORE_ALL) {//  已加载全部 --特殊
            more.setVisibility(GONE);
            loading.setVisibility(GONE);
            loadFull.setVisibility(VISIBLE);
        } else if (status == M_STATUS_REFRESH_ING) {//  下拉刷新 - 正在刷新 --特殊
            more.setText("正在刷新...");
            more.setVisibility(VISIBLE);
            loading.setVisibility(VISIBLE);
            loadFull.setVisibility(GONE);
        }
    }

    private class HolderViewRefresh {
        TextView tip;
        TextView lastUpdate;
        ProgressBar refreshing;
        ImageView loading;
    }

    // 下拉刷新 - 负责更新状态 并 改界面
    private void setRefreshStatus(int status, int net_status) {
        if (status == M_STATUS_REFRESH_READY && mStatus == M_STATUS_REFRESH_READY
                || status == M_STATUS_REFRESH_ING && mStatus == M_STATUS_REFRESH_ING
                || status == M_STATUS_REFRESH_DONE && mStatus == M_STATUS_REFRESH_DONE) {
            return;
        }
        if (status == M_STATUS_REFRESH_READY || status == M_STATUS_REFRESH_ING || status == M_STATUS_REFRESH_DONE) {
            inLoadMore = false;// 正在加载更多时允许下拉刷新并关掉之前的加载更多
            pullUp = 0;
        }

        TextView tip = holderViewRefresh.tip;
        TextView lastUpdate = holderViewRefresh.lastUpdate;
        ProgressBar refreshing = holderViewRefresh.refreshing;
        ImageView loading = holderViewRefresh.loading;

        if (status == M_STATUS_REFRESH_DONE) {
            lastRefreshTime = System.currentTimeMillis();
        }

        long timeDiff = (System.currentTimeMillis() - lastRefreshTime) / 1000;
        if (lastRefreshTime == -1) {
            lastUpdate.setVisibility(GONE);
        } else if (timeDiff <= 2) {
            lastUpdate.setText("上次刷新:刚刚");
        } else if (timeDiff <= 60) {
            lastUpdate.setText("上次刷新:" + timeDiff + "秒前");
        } else if (timeDiff <= 60 * 60) {
            lastUpdate.setText("上次刷新:" + (timeDiff / 60) + "分钟前");
        } else if (timeDiff <= 24 * 60 * 60) {
            lastUpdate.setText("上次刷新:" + (timeDiff / 60 / 60) + "小时前");
        } else {
            lastUpdate.setText("上次刷新:" + (timeDiff / 24 / 60 / 60) + "天前");
        }

        if (status == M_STATUS_REFRESH_READY) { // 松开立即刷新
            if (mStatus == M_STATUS_NONE || mStatus == M_STATUS_LOAD_MORE_ALL) {
                mLastStatus = mStatus;
            }
            mStatus = M_STATUS_REFRESH_READY;
            tip.setText("松开立即刷新");
            refreshing.setVisibility(GONE);
            lastUpdate.setVisibility(VISIBLE);
            loading.setVisibility(VISIBLE);
            loading.startAnimation(rotateAnimation);

        } else if (status == M_STATUS_REFRESH_ING) {// 正在刷新咯
            mStatus = M_STATUS_REFRESH_ING;
            tip.setText("正在刷新");
            loading.setVisibility(GONE);
            refreshing.setVisibility(VISIBLE);
            loading.clearAnimation();

        } else if (status == M_STATUS_REFRESH_DONE) { // 刷新完成
            mStatus = M_STATUS_REFRESH_DONE;
            tip.setText("刷新完成");
            refreshing.setVisibility(GONE);
            loading.setVisibility(GONE);
            refreshRollbackHandler.removeMessages(M_STATUS_REFRESH_DONE);
            refreshRollbackHandler.sendMessageDelayed(refreshRollbackHandler.obtainMessage(M_STATUS_REFRESH_DONE, net_status), 600);

        } else if (status == M_STATUS_NONE) {
            if (mStatus == M_STATUS_REFRESH_READY) {
                loading.clearAnimation();
            }
            if (mStatus == M_STATUS_NONE || mStatus == M_STATUS_LOAD_MORE_ALL) {
                mLastStatus = mStatus;
            } else {
                mStatus = mLastStatus;
            }
            tip.setText("下拉刷新");
            refreshing.setVisibility(GONE);
            loading.setVisibility(VISIBLE);
        }
    }

    public void setViewLoad(boolean isView) {
        if (isView) {
            loadMoreView.setVisibility(VISIBLE);
        } else {
            loadMoreView.setVisibility(GONE);
        }
    }

    // 设置是否可以上拉加载更多
    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    // 设置是否可以上拉刷新
    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    // 添加上拉下拉监听器
    public void addOnPullListener(OnPullListener onPullListener) {
        onPullListenerLists.add(onPullListener);
    }

    public PullLayout(Context context) {
        super(context);
        init();
    }

    public PullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    // 初始化
    private void init() {
        onPullListenerLists = new ArrayList<>();
        refreshRollbackHandler = new RefreshRollbackHandler(this);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.background_light));
    }

    // 显示正在加载
    public void showLoading() {
        isShowLoading = true;
        isShowError = false;
        _check_show_view();
    }

    // 隐藏正在加载
    public void hideLoading() {
        if (isShowLoading) {
            isShowLoading = false;
            _check_show_view();
        }
    }

    // 显示错误
    public void showErrorAlert(String title) {
        if (errorAlertToast != null) {
            errorAlertToast.cancel();
        }
        errorAlertToast = Toast.makeText(getContext(), title, Toast.LENGTH_SHORT);
        errorAlertToast.show();
    }

    // 显示错误
    public void showError(String title) {
        isShowError = true;
        isShowLoading = false;
        showErrorTitle = title;
        showErrorImg = -1;
        _check_show_view();
    }

    // 显示错误
    public void showError(String title, int img_res_id) {
        isShowError = true;
        isShowLoading = false;
        showErrorTitle = title;
        showErrorImg = img_res_id;
        _check_show_view();
    }

    // 隐藏错误
    public void hideError() {
        if (isShowError) {
            isShowError = false;
            _check_show_view();
        }
    }

    // 检查并更新界面（加载中/错误）
    private void _check_show_view() {
        if (isShowError || isShowLoading) {
            if (pullUp != 0 || pullDown != 0) {
                pullUp = pullDown = 0;
                inRefresh = inLoadMore = false;
                requestLayout();
            }
        }
        if (isShowError) {
            if (showErrorTitle == null) {
                showErrorTitle = "发生错误";
            }
            TextView title = (TextView) errorView.findViewById(R.id.title);
            title.setText(showErrorTitle);
            if (showErrorImg != -1) {
                ImageView image = (ImageView) errorView.findViewById(R.id.image);
                image.setImageResource(showErrorImg);
            }
            errorView.setVisibility(VISIBLE);
            loadingView.setVisibility(GONE);
            mListView.setVisibility(GONE);
            refreshView.setVisibility(VISIBLE);
            loadMoreView.setVisibility(GONE);
        } else if (isShowLoading) {
            loadingView.setVisibility(VISIBLE);
            errorView.setVisibility(GONE);
            mListView.setVisibility(GONE);
            refreshView.setVisibility(GONE);
            loadMoreView.setVisibility(GONE);
        } else {
            errorView.setVisibility(GONE);
            loadingView.setVisibility(GONE);
            mListView.setVisibility(VISIBLE);
            refreshView.setVisibility(VISIBLE);
            loadMoreView.setVisibility(VISIBLE);
        }
    }

    // 布局初始化
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mListView = getChildAt(0);
        pullView = (PullView) mListView;

        LayoutParams lp;

        // 网络出错
        errorView = inflate(getContext(), R.layout.pull_layout_error, null);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(errorView, lp);
        errorView.findViewById(R.id.layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (OnPullListener listener : onPullListenerLists) {
                    listener.onErrorClickReload(PullLayout.this);
                }
            }
        });

        // 正在加载
        loadingView = inflate(getContext(), R.layout.pull_layout_loading, null);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(loadingView, lp);

        // 下拉刷新 - 头部
        refreshView = inflate(getContext(), R.layout.pull_layout_head, null);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(refreshView, lp);
        refreshView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int height = refreshView.getMeasuredHeight();
                if (height > 0) {
                    refreshViewHeight = height;
                    refreshView.removeOnLayoutChangeListener(this);
                }
            }
        });
        // 下拉刷新 箭头旋转
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.reverse_anim);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        // 上拉加载更多 - 尾部
        loadMoreView = inflate(getContext(), R.layout.pull_layout_foot, null);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(loadMoreView, lp);
        loadMoreView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int height = loadMoreView.getMeasuredHeight();
                if (height > 0) {
                    loadMoreViewHeight = height;
                    loadMoreView.removeOnLayoutChangeListener(this);
                }
            }
        });

        // 关掉回弹效果
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // 监听快速向上滑动到最底部
        pullView.setOnScrollListener(new PullView.OnScrollListener() {
            @Override
            public void onScrollBottom() {
                if (canLoadMore && preY != 0 && !inRefresh) {
                    if (mStatus == M_STATUS_NONE || mStatus == M_STATUS_LOAD_MORE_ING || mStatus == M_STATUS_LOAD_MORE_ALL) {// 正在刷新时，不允许加载更多
                        if (pullView.canPullUp()) {
                            pullUp = -loadMoreViewHeight;
                            inLoadMore = true;
                            requestLayout();
                            if (mStatus == M_STATUS_NONE) {// 触发加载更多
                                _load_more();
                            } else if (mStatus == M_STATUS_LOAD_MORE_ALL) {// 已加载全部
                                setLoadMoreStatus(M_STATUS_LOAD_MORE_ALL, -1);
                            }
                        }
                    }
                }
            }
        });

        holderViewLoadMore = new HolderViewLoadMore();
        holderViewLoadMore.loadFull = (TextView) loadMoreView.findViewById(R.id.loadFull);
        holderViewLoadMore.more = (TextView) loadMoreView.findViewById(R.id.more);
        holderViewLoadMore.loading = (ProgressBar) loadMoreView.findViewById(R.id.loading_foot);

        holderViewRefresh = new HolderViewRefresh();
        holderViewRefresh.tip = (TextView) refreshView.findViewById(R.id.tip);
        holderViewRefresh.lastUpdate = (TextView) refreshView.findViewById(R.id.lastUpdate);
        holderViewRefresh.refreshing = (ProgressBar) refreshView.findViewById(R.id.refreshing);
        holderViewRefresh.loading = (ImageView) refreshView.findViewById(R.id.loading_head);

        _check_show_view();
        /*
        pullView.setOnDrawListener(new PullView.OnDrawListener() {
            @Override
            public void onDraw(Canvas canvas) {
                if (_onDrawCheckLoadMorePullUp) {
                    _onDrawCheckLoadMorePullUp = false;
                    if (inLoadMore && mStatus == M_STATUS_LOAD_MORE_ALL && pullView.canPullUp() == false) {
                        pullUp = 0;
                        inLoadMore = false;
                        requestLayout();
                    }
                }
            }
        });*/

        mListView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                if (_onDrawCheckLoadMorePullUp) {
                    _onDrawCheckLoadMorePullUp = false;
                    if (inLoadMore && mStatus == M_STATUS_LOAD_MORE_ALL && !pullView.canPullUp()) {
                        pullUp = 0;
                        inLoadMore = false;
                        requestLayout();
                    }
                }
                return true;
            }
        });
    }

    // 用于解决加载更多加载完成后界面更新不及时造成的 pullView.canPullUp()读取不是最新数据的问题
    boolean _onDrawCheckLoadMorePullUp = false;

    // 防止下拉过程中误触发长按事件和点击事件
    boolean bTouchCancel = false;


    private float diff = 8;
    private float mDownY;
    private float mDownX;
    private boolean click = false;

    private int mActivePointerId;

    // 监听处理触摸滑动
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (isShowLoading) {
            // 显示 正在加载 时不允许上拉下拉操作
            return super.dispatchTouchEvent(ev);
        }
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            boolean needRequestLayout = false;
            if (inLoadMore && !pullView.canPullUp()) {
                inLoadMore = false;
                pullUp = 0;
                needRequestLayout = true;
            }
            if (inRefresh && !pullView.canPullDown()) {
                inRefresh = false;
                pullDown = 0;
                needRequestLayout = true;
            }
            if (needRequestLayout) {
                requestLayout();
            }
        }

 //       Log.e("lastChildBottom=========", action + " -- " + pullView.canPullUp());


        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                preY = ev.getY();
                isMultiplePoints = false;
                bTouchCancel = false;

                mDownX = ev.getX();
                mDownY = ev.getY();
                click = true;
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP: {
                // 过滤多点触碰
                isMultiplePoints = true;
                click = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (click) {
                    float x = ev.getX();
                    float y = ev.getY();
                    if (Math.abs(mDownX - x) > diff || Math.abs(mDownY - y) > diff) {
                        click = false;
                    }
                }

                boolean stop = false;// 是否拦截Touch事件向下传播
                float currentY = ev.getY();
                float dy = currentY - preY;
                preY = currentY;
                if (isMultiplePoints) {
                    isMultiplePoints = false;
                    return true;// break;
                }
                // 下面四个变量只是为了减少requestLayout次数
                float needRequestLayout_pullDown = pullDown;
                float needRequestLayout_pullUp = pullUp;

                if (canRefresh) {// 刷新
                    if (dy > 0) {// 往下拉
                        if (inRefresh) {
                            pullDown += dy / pullDownRadio;
                            if (pullDown >= getMeasuredHeight() - 10) {//下拉距离不允许超出控件高度
                                pullDown = getMeasuredHeight() - 10;
                            }
                        } else {
                            if (pullView.canPullDown()) {// 子视图不能往下划了,开始显示下拉加载更多
                                inRefresh = true;
                            }
                        }
                        if ((mStatus == M_STATUS_REFRESH_ING || mStatus == M_STATUS_REFRESH_DONE) && pullDown > refreshViewHeight / 3) {
                            //正在刷新中，往下拉动超过一半 或者 下拉回弹时 显示【正在刷新】
                            isShowRefreshView = true;
                        }
                    } else if (dy < 0) {// 往上拉
                        if (inRefresh) {
                            pullDown += dy / pullDownRadio;
                            if (pullDown <= 0) {
                                inRefresh = false;
                                pullDown = 0;
                            }
                        }
                        isShowRefreshView = false;
                    }
                    if (mStatus != M_STATUS_REFRESH_ING && mStatus != M_STATUS_REFRESH_DONE) {
                        if (inRefresh) {
                            if (pullDown >= refreshViewHeight) {
                                //显示 松开立即刷新
                                setRefreshStatus(M_STATUS_REFRESH_READY, -1);
                            } else if (pullDown > 0) {
                                // 显示 下拉可以刷新
                                setRefreshStatus(M_STATUS_NONE, -1);
                            }
                        }
                    }
                    pullDownRadio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * pullDown));
                    if (inRefresh) {
                        stop = true;
                    }
                } else {
                    inRefresh = false;
                    pullDown = 0;
                }

                if (canLoadMore && !inRefresh && !isShowError && mStatus != M_STATUS_REFRESH_READY) {// 加载更多
                    if (dy < 0) {// 往上拉
                        if (inLoadMore) {
                            if (pullView.canPullUp()) {
                                pullUp += dy;
                                if (pullUp <= -loadMoreViewHeight) {
                                    pullUp = -loadMoreViewHeight;
                                }
                            } else {
                                inLoadMore = false;
                                pullUp = 0;
                            }
                        } else {
                            if (pullView.canPullUp()) {// 子视图不能往上划了
                                inLoadMore = true;
                                if (mStatus == M_STATUS_NONE) {// 开始加载更多
                                    _load_more();
                                } else if (mStatus == M_STATUS_LOAD_MORE_ALL) {// 已加载全部
                                    setLoadMoreStatus(M_STATUS_LOAD_MORE_ALL, -1);
                                } else if (mStatus == M_STATUS_REFRESH_ING) {
                                    setLoadMoreStatus(M_STATUS_REFRESH_ING, -1);
                                } else {
                                    //  do nothing ??
                                }
                            }
                        }
                    } else if (dy > 0) {// 往下拉
                        if (inLoadMore) {
                            if (pullView.canPullUp()) {
                                pullUp += dy;
                                if (pullUp >= 0) {
                                    inLoadMore = false;
                                    pullUp = 0;
                                }
                            } else {
                                inLoadMore = false;
                                pullUp = 0;
                            }
                        }

                    }

                    if (inLoadMore) {
                        stop = true;
                    }
                } else {
                    inLoadMore = false;
                    pullUp = 0;
                }

                if (needRequestLayout_pullUp != pullUp || needRequestLayout_pullDown != pullDown) {
                    requestLayout();
                }

                bTouchCancel = Math.abs(pullUp) + pullDown > 8;
                if (stop) {
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mStatus == M_STATUS_REFRESH_READY && inRefresh) {// 下拉刷新松开手 触发刷新
                    isShowRefreshView = true;
                    // (自动回弹后 触发刷新)
                }
                if (!click && bTouchCancel) {//防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                runRefreshRollback(true);
                break;
            }
            default: {
                click = false;
            }
        }

        _onDrawCheckLoadMorePullUp = false;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isShowLoading) {// 显示 正在加载 时不能显示下拉刷新和上拉加载
            super.onLayout(changed, l, t, r, b);
            return;
        } else if (isShowError) {
            inRefresh = false;
            pullDown = 0;
        }
        int y = (int) (pullUp + pullDown);
        refreshView.layout(0, y - 999, refreshView.getMeasuredWidth(), y);
        if (isShowError) {
            errorView.layout(0, y, mListView.getMeasuredWidth(), y + mListView.getMeasuredHeight());
        } else {
            mListView.layout(0, y, mListView.getMeasuredWidth(), y + mListView.getMeasuredHeight());
            loadMoreView.layout(0, y + mListView.getMeasuredHeight(), loadMoreView.getMeasuredWidth(), y + mListView.getMeasuredHeight() + loadMoreViewHeight);
        }
    }


    //刷新加载回调接口
    public interface OnPullListener {

        //刷新
        void onRefresh(PullLayout pullToRefreshLayout, boolean firstRefresh);

        //加载更多
        void onLoadMore(PullLayout pullToRefreshLayout);

        // 错误页面点击重新加载
        void onErrorClickReload(PullLayout pullToRefreshLayout);

        class Simple implements OnPullListener {

            @Override
            public void onRefresh(PullLayout pullToRefreshLayout, boolean firstRefresh) {

            }

            @Override
            public void onLoadMore(PullLayout pullToRefreshLayout) {

            }

            @Override
            public void onErrorClickReload(PullLayout pullToRefreshLayout) {

            }
        }
    }

    private Timer timer;
    @Nullable
    private TimerTask mTask;

    // 开启 - 下拉刷新自动回弹
    private void runRefreshRollback(final boolean checkRefresh) {
        if (mTask != null) {
            mTask.cancel();
        }
        mTask = new TimerTask() {
            @Override
            public void run() {
                refreshRollbackHandler.obtainMessage(M_STATUS_REFRESH_READY, checkRefresh).sendToTarget();
            }
        };
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(mTask, 0, 5);
    }

    // 取消 - 下拉刷新自动回弹
    private void cancelRefreshRollback() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    // 自动回弹
    private static class RefreshRollbackHandler extends Handler {
        @NonNull
        private final WeakReference<PullLayout> mThe;

        RefreshRollbackHandler(PullLayout the) {
            this.mThe = new WeakReference<>(the);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            PullLayout the = mThe.get();
            if (the == null) {
                return;
            }
            if (msg.what == M_STATUS_REFRESH_DONE) {// 刷新完成
                if (the.mStatus != M_STATUS_REFRESH_DONE) {
                    return;
                }
                int net_status = (int) msg.obj;
                if (net_status == ALL) {
                    the.mStatus = M_STATUS_LOAD_MORE_ALL;
                } else {
                    the.mStatus = M_STATUS_NONE;
                }
                the.runRefreshRollback(false);

            } else if (msg.what == M_STATUS_REFRESH_READY) {// 刷新自动回弹
                boolean checkRefresh = (boolean) msg.obj;// 是否回弹完成后刷新
                float needRequestLayout_pullDown = the.pullDown;
                if (checkRefresh && the.isShowRefreshView && the.mStatus == M_STATUS_REFRESH_READY && the.inRefresh && the.pullDown <= the.refreshViewHeight) {
                    the._refresh();
                    the.pullDown = the.refreshViewHeight;
                    the.cancelRefreshRollback();
                } else if (the.isShowRefreshView && (the.mStatus == M_STATUS_REFRESH_DONE || the.mStatus == M_STATUS_REFRESH_ING) && the.inRefresh && the.pullDown <= the.refreshViewHeight) {
                    the.pullDown = the.refreshViewHeight;
                    the.cancelRefreshRollback();
                } else {
                    if (the.pullDown > 0) {
                        // 回弹速度随下拉距离增大而增大
                        the.pullDown -= (float) (8 + 5 * Math.tan(Math.PI / 2 / the.getMeasuredHeight() * the.pullDown));
                    }
                    if (the.pullDown <= 0) {  // 已完成回弹
                        the.pullDown = 0;
                        the.cancelRefreshRollback();
                    }
                }
                if (needRequestLayout_pullDown != the.pullDown) {
                    the.requestLayout();
                }
            }
        }
    }
}
