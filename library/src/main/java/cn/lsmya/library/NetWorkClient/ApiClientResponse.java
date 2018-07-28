package cn.lsmya.library.NetWorkClient;

import com.alibaba.fastjson.JSON;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * 服务器返回结果实体类
 */
public class ApiClientResponse implements Serializable {

    private String url_;
    private String data;
    private InputStream inputStream;

    private boolean cached;

    public ApiClientResponse() {
    }

    public ApiClientResponse(String url_, String data) {
        this.url_ = url_;
        this.data = data;
    }

    public ApiClientResponse(String url_, InputStream inputStream) {
        this.url_ = url_;
        this.inputStream = inputStream;
    }

    // 判断数据是否来自缓存
    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    @Deprecated
    public void setData(String data) {
        this.data = data;
    }

    // 获取服务器返回的data-json
    public String getData() {
        return data;
    }

    // 将服务器返回的内容映射到实体类
    public <T> T getData(Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

    // 将服务器返回的内容映射到List<实体类>
    public <T> List<T> getList(Class<T> clazz) {
        return JSON.parseArray(data, clazz);
    }

    public String getUrl_() {
        return url_;
    }

    public void setUrl_(String url_) {
        this.url_ = url_;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String toString() {
        return "ApiClientResponse{" +
                "url='" + url_ + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
