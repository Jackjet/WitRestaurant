package cn.lsmya.library.NetWorkClient;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.lsmya.library.PullLayout.PullLayout;

public abstract class ApiClientResponseWithPullHandler<T extends ApiClientResponseWithPullHandler.GetPullLayout> extends ApiClientResponseHandler<T> {
    // 刷新成功
    protected static final int SUCCEED = PullLayout.SUCCEED;

    // 刷新全部
    protected static final int ALL = PullLayout.ALL;

    // 刷新失败
    protected static final int FAIL = PullLayout.FAIL;

    // 刷新取消
    protected static final int CANCEL = PullLayout.CANCEL;

    // 刷新忽略
    protected static final int IGNORE = 99999;


    public ApiClientResponseWithPullHandler(T the) {
        super(the);
    }

    // 联网成功 返回值可以为[SUCCEED|ALL|FAIL]
    public abstract int onSuccess(T the, ApiClientRequest request, ApiClientResponse response, @Nullable PullLayout pullLayout);

    // 联网错误 返回值[true=通知pullLayout | false=不通知pullLayout]
    public boolean onError(T the, @NonNull ApiClientRequest request, @NonNull ApiClientError error, @Nullable PullLayout pullLayout) {
        super.onError(the, request, error);
        return true;
    }

    // 联网取消 返回值[true=通知pullLayout | false=不通知pullLayout]
    public boolean onCancelled(T the, ApiClientRequest request, @Nullable PullLayout pullLayout) {
        return true;
    }

    @Deprecated
    @Override
    final public void onSuccess(@NonNull T the, ApiClientRequest request, ApiClientResponse response) {
        PullLayout pullLayout = the.getPullLayout();
        int status = onSuccess(the, request, response, pullLayout);
        if (pullLayout != null && status != IGNORE) {
            pullLayout.setStatus(status);
        }
    }

    @Deprecated
    @Override
    final public void onError(@NonNull T the, @NonNull ApiClientRequest request, @NonNull ApiClientError error) {
        PullLayout pullLayout = the.getPullLayout();
        boolean r = onError(the, request, error, pullLayout);
        if (pullLayout != null && r) {
            pullLayout.setStatus(FAIL);
        }
    }

    @Deprecated
    @Override
    final public void onCancelled(@NonNull T the, ApiClientRequest request) {
        PullLayout pullLayout = the.getPullLayout();
        boolean r = onCancelled(the, request, pullLayout);
        if (pullLayout != null && r) {
            pullLayout.setStatus(CANCEL);
        }
    }

    // 之所以引入这个，是因为利用T the的弱引用去获取PullLayout，免得再去强引用
    public interface GetPullLayout {
        @Nullable
        PullLayout getPullLayout();
    }
}
