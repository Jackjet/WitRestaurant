package cn.lsmya.library.NetWorkClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.alibaba.fastjson.JSON;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 与服务器请求 + 回调处理 【核心】
 */
public class ApiClientRequest implements Runnable {
    public int what;
    public Object obj;
    public int arg1;
    public String arg2;
    private int progress;

    // 线程池
    private static final ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(15);
    @Nullable
    private volatile Future threadFuture;

    private WeakReference<Context> contextWeakReference;
    private ApiClientResponseHandler apiClientResponseHandler;
    private Map<String, Object> post;
    private File file;
    private String route;

    private boolean isFinished;
    private boolean isCancelled;
    private boolean isStarted;
    protected boolean isError;

    private boolean debug = false;

    // 是否为重复执行任务
    private boolean threadSubmitOnce;

    // 是否开启调试模式（开启后打印json等数据供参考调试）
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    // 获取之前发送的数据
    public Map<String, Object> getPost() {
        if (post == null) {
            post = new HashMap<>();
        }
        return post;
    }

    //获取上传进度
    public int getProgress() {
        return progress;
    }

    // 获取上传的文件
    public File getFile() {
        return file;
    }

    // 获取请求的路由
    public String getRoute() {
        return route;
    }

    // 设置发送数据
    public void setPost(Map<String, Object> post) {
        this.post = post;
    }

    // 设置请求路由
    public void setRoute(String route) {
        this.route = route;
    }

    // 设置上传文件
    public void setFile(File file) {
        this.file = file;
    }

    // 判断路由是否为
    public boolean isRoute(@NonNull String route) {
        return route.equals(this.route);
    }

    public Context getContext() {
        return contextWeakReference.get();
    }

    // 获取当前设置的Handler回调
    public ApiClientResponseHandler getApiClientResponseHandler() {
        return apiClientResponseHandler;
    }

    // 设置新的Handler回调
    public void setApiClientResponseHandler(ApiClientResponseHandler apiClientResponseHandler) {
        this.apiClientResponseHandler = apiClientResponseHandler;
    }

    // 是否已经开始运行
    public boolean isStarted() {
        return isStarted;
    }

    @Deprecated
    // 是否已经运行结束 @See isDone();
    public boolean isFinished() {
        return isFinished;
    }

    // 是否已被取消
    public boolean isCancelled() {
        return isCancelled;
    }

    // 是否发生错误
    public boolean isError() {
        return isError;
    }

    public ApiClientRequest(@NonNull Context context) {
        this(context, null, null, null, null);
    }

    public ApiClientRequest(@NonNull Context context, ApiClientResponseHandler apiClientResponseHandler) {
        this(context, null, null, null, apiClientResponseHandler);
    }

    public ApiClientRequest(@NonNull Context context, String route, ApiClientResponseHandler apiClientResponseHandler) {
        this(context, route, null, null, apiClientResponseHandler);
    }

    public ApiClientRequest(@NonNull Context context, String route, Map<String, Object> post, ApiClientResponseHandler apiClientResponseHandler) {
        this(context, route, post, null, apiClientResponseHandler);
    }

    public ApiClientRequest(@NonNull Context context, String route, File file, ApiClientResponseHandler apiClientResponseHandler) {
        this(context, route, null, file, apiClientResponseHandler);
    }

    public ApiClientRequest(@NonNull Context context, String route, Map<String, Object> post, File file, ApiClientResponseHandler apiClientResponseHandler) {
        this.contextWeakReference = new WeakReference<>(context);
        this.route = route;
        this.post = post;
        this.file = file;
        this.apiClientResponseHandler = apiClientResponseHandler;
    }

    // 线程是否已经结束
    public boolean isDone() {
        return isCancelled || isFinished || threadFuture == null || threadFuture.isCancelled() || threadFuture.isDone();
    }

    // 立即执行 先读缓存 并且保存最新数据到缓存
    public void submitWithLoadAndSaveCache() {
        if (threadFuture != null) {
            isCancelled = true;
            threadFuture.cancel(true);
        }
        isFinished = isCancelled = isError = isStarted = false;
        threadSubmitOnce = true;
        threadFuture = threadPool.submit(this);
    }

    // 立即执行 并保存本次缓存
    public void submitWithSaveCache() {
        if (threadFuture != null) {
            isCancelled = true;
            threadFuture.cancel(true);
        }
        isFinished = isCancelled = isError = isStarted = false;
        threadSubmitOnce = true;
        threadFuture = threadPool.submit(this);
    }

    // 立即执行
    public void submit() {
        if (threadFuture != null) {
            isCancelled = true;
            threadFuture.cancel(true);
        }
        isFinished = isCancelled = isError = isStarted = false;
        threadSubmitOnce = true;
        threadFuture = threadPool.submit(this);
    }

    @Deprecated
    // 每隔delay毫秒运行一次(每次间隔固定时间)
    public void submitScheduleWithFixedDelay(long initialDelay, long delay) {
        if (threadFuture != null) {
            isCancelled = true;
            threadFuture.cancel(true);
        }
        isFinished = isCancelled = isError = isStarted = false;
        threadSubmitOnce = false;
        threadFuture = threadPool.scheduleWithFixedDelay(this, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    // 每隔delay毫秒运行一次(上次执行完毕后再间隔delay毫秒后执行下一次)
    public void submitScheduleAtFixedRate(long initialDelay, long delay) {
        if (threadFuture != null) {
            isCancelled = true;
            threadFuture.cancel(true);
        }
        isFinished = isCancelled = isError = isStarted = false;
        threadSubmitOnce = false;
        threadFuture = threadPool.scheduleAtFixedRate(this, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    // 计划delay毫秒后执行
    public void submitSchedule(long delay) {
        if (threadFuture != null) {
            isCancelled = true;
            threadFuture.cancel(true);
        }
        isFinished = isCancelled = isError = isStarted = false;
        threadSubmitOnce = true;
        threadFuture = threadPool.schedule(this, delay, TimeUnit.MILLISECONDS);
    }

    // 取消当前线程
    public void cancel() {
        if (threadSubmitOnce && isFinished || isCancelled) {
            return;
        }
        isCancelled = true;

        if (threadFuture != null) {
            threadFuture.cancel(true);
            threadFuture = null;
        }

        if (!isStarted) {
            apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_BEFORE, this, null, null);
        }
        apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_CANCELLED, this, null, null);
        if (!isFinished) {
            apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_FINISHED, this, null, null);
        }
    }

    // 取消当前计划任务
    public void cancelSchedule() {
        if (threadSubmitOnce && isFinished || isCancelled) {
            return;
        }
        if (threadFuture != null) {
            threadFuture.cancel(false);
            threadFuture = null;
        }
    }

    private ProgressDialog progressDialog;

    public void showProgress() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("正在加载");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    @Deprecated
    @Override
    public void run() {
        progress = -1;
        isFinished = false;
        isCancelled = false;
        isError = false;
        isStarted = true;

        int statusCode = 0;
        HttpURLConnection httpURLConnection = null;
        DataOutputStream outputStream = null;
        try {

            boolean beforeCheck = apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_BEFORE, this, null, null);
            if (!beforeCheck) {
                return;
            }

            // key强制转换为字符串
            Map<String, Object> _post;
            if (post == null) {
                _post = null;
            } else {
                _post = new HashMap<>();
                for (Map.Entry entry : post.entrySet()) {
                    _post.put(String.valueOf(entry.getKey()), entry.getValue());
                }
            }
            String params = "";
            if (_post != null) {
                for (Map.Entry<String, Object> entry : _post.entrySet()) {
                    params = params + entry.getKey() + "=" + URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8") + "&";
                }
                int length1 = params.length();
                params = params.substring(0, length1 - 1);
                debug_log("send-上传参数", JSON.toJSONString(_post));
            } else {
                debug_log("send-上传参数", "没有上传参数");
            }

            URL url = new URL(route);
            debug_log("send-接口地址", url);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(60000);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            if (isCancelled || Thread.interrupted()) {
                return;
            }

            if (file == null) {//不上传文件
                if (debug) {
                    debug_log("send-请求头部", httpURLConnection.getRequestProperties());
                }
                if (_post != null) {
                    outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    outputStream.writeBytes(params);
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }


            } else {//上传文件
                String BOUNDARY = UUID.randomUUID().toString();
                final String PREFIX = "--";
                final String LINEND = "\r\n";

                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                if (debug) {
                    debug_log("send-请求头部", httpURLConnection.getRequestProperties());
                }


                //获取请求内容输出流
                outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                String fileName = file.getName();
                //开始写表单格式内容
                //写参数
                Set<String> keys = _post.keySet();
                for (String key : keys) {
                    outputStream.writeBytes(PREFIX + BOUNDARY + LINEND);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"");
                    outputStream.write(key.getBytes());
                    outputStream.writeBytes("\"" + LINEND);
                    outputStream.writeBytes(LINEND);
                    outputStream.write(String.valueOf(_post.get(key)).getBytes());
                    outputStream.writeBytes(LINEND);
                }
                if (debug) {
                    debug_log("send-body-file-name", file.getName());
                    debug_log("send-body-file-type", getFileContentType(file));
                }
                //写文件
                outputStream.writeBytes(PREFIX + BOUNDARY + LINEND);
                outputStream.writeBytes("Content-Disposition: form-data; " + "name=\"file\"; " + "filename=\"");
                //防止中文乱码
                outputStream.write(fileName.getBytes());
                outputStream.writeBytes("\"" + LINEND);
                outputStream.writeBytes("Content-Type: " + getFileContentType(file) + LINEND);
                outputStream.writeBytes(LINEND);
                //根据路径读取文件
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                long available = fis.available();
                long len = 0;
                int length = -1;
                while ((length = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                    len = len + length;
                    double progress1 = len / (double) available * 100;
                    setProgress((int) progress1);
                }
                if (progress != 100) {
                    setProgress(100);
                }
                outputStream.writeBytes(LINEND);
                fis.close();
                outputStream.writeBytes(PREFIX + BOUNDARY + PREFIX + LINEND);
                outputStream.writeBytes(LINEND);
                outputStream.flush();


            }
            /**
             * 请求结束
             */

            if (isCancelled || Thread.interrupted()) {
                return;
            }
            statusCode = httpURLConnection.getResponseCode();
            if (debug) {
                debug_log("receive-请求码", statusCode + " " + httpURLConnection.getResponseMessage());
                debug_log("receive-请求头", httpURLConnection.getHeaderFields());
            }

            BufferedInputStream bis = new BufferedInputStream(statusCode == 200 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024 * 8];
            int length;
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bis.close();
            bos.flush();
            bos.close();

            if (isCancelled || Thread.interrupted()) {
                return;
            }
            String result = baos.toString();
            debug_log("receive-返回数据", result);

            if (statusCode != 200) {
                isError = true;
                ApiClientError error = new ApiClientError(statusCode, result, new Exception("HTTP状态码≠200"));
                apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_ERROR, this, null, error);
                return;
            }

            ApiClientResponse response;
            try {
                response = new ApiClientResponse(route, result);
            } catch (Exception e) {
                isError = true;
                ApiClientError error = new ApiClientError(statusCode, result, e);
                apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_ERROR, this, null, error);
                return;
            }

            if (isCancelled || Thread.interrupted()) {
                return;
            }

            response.setCached(false);
            apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_SUCCESS, this, response, null);

        } catch (InterruptedIOException e) {
            // do nothing. 线程中断时走这里
        } catch (Exception e) {
            if (!(e instanceof IOException)) {
                LogUtil.e(ApiClientRequest.class, "非IO异常", e);
            }
            if (!isCancelled && !Thread.interrupted()) {
                isError = true;
                ApiClientError error = new ApiClientError(statusCode, null, e);
                apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_ERROR, this, null, error);
            }
        } finally {
            if (!Thread.interrupted()) {
                isFinished = true;
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (!isCancelled && !Thread.interrupted()) {
                apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_FINISHED, this, null, null);
            }
            if (threadSubmitOnce) {
                threadFuture = null;
            }
        }
    }

    // 更新进度条

    private void setProgress(int progress) {
        this.progress = progress;
        apiClientResponseHandler.sendMessage(ApiClientResponseHandler.METHOD_PROGRESS, this, null, null);
    }

    @NonNull
    @Override
    public String toString() {
        return "ApiClientRequest{" +
                "route='" + route + '\'' +
                ", post=" + post +
                ", file=" + file +
                ", isError=" + isError +
                ", isStarted=" + isStarted +
                ", isCancelled=" + isCancelled +
                ", isFinished=" + isFinished +
                '}';
    }

    // 输出调试内容
    private void debug_log(String cmd, Object txt) {
        if (debug) {
            Log.e("ApiClientRequestDebug", "[" + route + "][" + cmd + "] " + txt);
        }
    }


    // 获取要上传文件的Content-Type
    private String getFileContentType(@NonNull File file) {
        String contentType;
        try {
            contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.getName()));
        } catch (Exception e) {
            contentType = "";
        }
        if (TextUtils.isEmpty(contentType)) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

}
