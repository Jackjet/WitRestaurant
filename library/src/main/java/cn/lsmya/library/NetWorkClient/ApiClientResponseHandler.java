package cn.lsmya.library.NetWorkClient;

import android.os.Message;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * 接收回调
 * 执行顺序：submit -> onBefore -> [onSuccess|onError|onCancelled] -> onFinished -> cancel
 *
 * @param <T> 构造方法不可以传为null的值，接收任意类型，使用弱引用机制，当值变为null时回调失效
 */
public abstract class ApiClientResponseHandler<T> extends android.os.Handler {

    public final int OK = 1;
    public final int ERROR = 0;

    private static class MethodMessage {
        protected int method;
        protected ApiClientResponse response;
        protected ApiClientRequest request;
        protected ApiClientError error;

        public MethodMessage(int method, ApiClientRequest request, ApiClientResponse response, ApiClientError error) {
            this.method = method;
            this.request = request;
            this.response = response;
            this.error = error;
        }
    }

    protected final static int METHOD_BEFORE = 0;
    protected final static int METHOD_SUCCESS = 1;
    protected final static int METHOD_ERROR = 2;
    protected final static int METHOD_FINISHED = 3;
    protected final static int METHOD_CANCELLED = 4;
    protected final static int METHOD_PROGRESS = 5;


    private WeakReference<T> mThe;

    // Handler回调，当the被回收或者finished后不会再回调该方法
    protected void handleMessage(T the, Message msg) {
        // blank
    }

    @Deprecated
    @Override
    public void handleMessage(@NonNull Message msg) {
        T the = getThe();
        if (the == null) {
            return;
        }
        if (msg.obj == null || !(msg.obj instanceof MethodMessage)) {
            handleMessage(the, msg);
            return;
        }

        MethodMessage mmsg = (MethodMessage) msg.obj;
        if (mmsg.method == METHOD_BEFORE) {
            onBefore(the, mmsg.request);
        } else if (mmsg.method == METHOD_SUCCESS) {
            try {
                onSuccess(the, mmsg.request, mmsg.response);
            } catch (com.alibaba.fastjson.JSONException e) {
                if (!mmsg.response.isCached()) {// 如果是缓存并且json转换失败的话可能是老版本缓存与当前实体类不一致所致(忽略)
                    mmsg.request.isError = true;
                    mmsg.error = new ApiClientError(200, mmsg.response.getData(), e);
                    onError(the, mmsg.request, mmsg.error);
                }
            }
        } else if (mmsg.method == METHOD_ERROR) {
            onError(the, mmsg.request, mmsg.error);
        } else if (mmsg.method == METHOD_FINISHED) {
            onFinished(the, mmsg.request);
        } else if (mmsg.method == METHOD_CANCELLED) {
            onCancelled(the, mmsg.request);
        } else if (mmsg.method == METHOD_PROGRESS) {
            onProgress(the, mmsg.request, mmsg.request.getProgress());
        }
    }


    /**
     * 获取引用
     */
    private T getThe() {
        if (mThe == null) {
            return null;
        }
        T the = mThe.get();
        if (the == null) {
            return null;
        }
        // 如果Activity已经finish或者销毁，则返回null
        /*
        if (the instanceof Fragment) {
            Activity activity = ((Fragment) the).getActivity();
            if (activity.isFinishing() || Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
                return null;
            }
        } else if (the instanceof Activity) {
            if (((Activity) the).isFinishing() || Build.VERSION.SDK_INT >= 17 && ((Activity) the).isDestroyed()) {
                return null;
            }
        }*/
        return the;
    }

    private ApiClientResponseHandler() {
    }

    public ApiClientResponseHandler(T the) {
        mThe = new WeakReference<>(the);
    }

    final protected boolean sendMessage(int method, @NonNull ApiClientRequest request, @NonNull ApiClientResponse response, ApiClientError error) {
        T the = getThe();
        if (the == null) {
            return false;
        }
        boolean result = true;
        if (method == METHOD_BEFORE) {
            result = onSubmitBefore(the, request);
        } else if (method == METHOD_SUCCESS) {
            try {
                onSubmitSuccess(the, request, response);
            } catch (com.alibaba.fastjson.JSONException e) {
                if (!response.isCached()) {
                    request.isError = true;
                    error = new ApiClientError(200, response.getData(), e);
                    onSubmitError(the, request, error);
                }
            }
        } else if (method == METHOD_ERROR) {
            onSubmitError(the, request, error);
        } else if (method == METHOD_FINISHED) {
            onSubmitFinished(the, request);
        } else if (method == METHOD_CANCELLED) {
            onSubmitCancelled(the, request);
        } else if (method == METHOD_PROGRESS) {
            onSubmitProgress(the, request, request.getProgress());
        }

        Message msg = new Message();
        msg.obj = new MethodMessage(method, request, response, error);
        sendMessage(msg);
        return result;
    }

    //网络请求前回调这里
    public void onBefore(T the, ApiClientRequest request) {
    }

    //网络请求成功后请求这里
    public abstract void onSuccess(T the, ApiClientRequest request, ApiClientResponse response);

    //网络错误、代码异常时走这里
    public void onError(T the, @NonNull ApiClientRequest request, @NonNull ApiClientError error) {
        LogUtil.e(ApiClientResponseHandler.class, "[" + request.getRoute() + "]出现异常, statusCode=" + error.getStatusCode() + ", data=" + error.getData(), error.getException());
    }

    // 网络请求被取消
    public void onCancelled(T the, ApiClientRequest request) {
    }

    // 网络请求进度条改变
    public void onProgress(T the, ApiClientRequest request, int progress) {
    }

    //没有登录或者在其他设备上登录
    public void onLogin(T the, ApiClientRequest request) {
    }

    // 网络请求完毕
    public void onFinished(T the, ApiClientRequest request) {
    }

    //网络请求前回调这里 - 返回false则拦截执行，返回true才会往下执行哦
    public boolean onSubmitBefore(T the, ApiClientRequest request) {
        return true;
    }

    //网络请求成功后请求这里
    public void onSubmitSuccess(T the, ApiClientRequest request, ApiClientResponse response) {
    }

    //网络错误、代码异常时走这里
    public void onSubmitError(T the, ApiClientRequest request, ApiClientError error) {
    }

    // 网络请求被取消
    public void onSubmitCancelled(T the, ApiClientRequest request) {
    }

    // 网络请求完毕
    public void onSubmitFinished(T the, ApiClientRequest request) {
    }

    // 网络请求进度条改变
    public void onSubmitProgress(T the, ApiClientRequest request, int progress) {
    }

}


