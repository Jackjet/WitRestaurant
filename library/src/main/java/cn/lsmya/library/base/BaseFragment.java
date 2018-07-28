package cn.lsmya.library.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    abstract protected int getLayoutId();

    abstract protected void onViewCreated(View view);

    @Nullable
    @Deprecated
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        onViewCreated(view);
        return view;
    }

    @Deprecated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean _view_visible = false;// 是否可视
    private boolean _view_resume = false;// 是否恢复
    private boolean _view_start = true;//是否已经运行过第一次
    private boolean _view_init = true;//是否已初始化
    private boolean _view_run = false;//是否运行


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        _view_visible = getUserVisibleHint();
        if (_view_visible && _view_resume && !_view_run) {
            if (_view_start) {
                _view_start = false;
                onViewStart();
            }
            _view_run = true;
            onViewShow();
        } else if (!_view_visible && _view_run) {
            _view_run = false;
            onViewHide();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        _view_resume = false;
        if (_view_run) {
            _view_run = false;
            onViewHide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        _view_resume = true;
        if (_view_visible) {
            if (!_view_run) {
                if (_view_start) {
                    _view_start = false;
                    onViewStart();
                }
                _view_run = true;
                onViewShow();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (_view_init) {
            _view_init = false;
            onViewInit();
        }
    }

    // 初次加载Fragment时执行
    protected void onViewInit() {
    }

    // 初次加载Fragment并显示出来的时候运行
    protected void onViewStart() {
    }

    // Fragment被显示时运行
    protected void onViewShow() {
    }

    // Fragment被隐藏时运行
    protected void onViewHide() {
    }

    /**
     * activity 标题栏双击
     */
    public void onTitleDoubleClick(View v) {
    }
}
