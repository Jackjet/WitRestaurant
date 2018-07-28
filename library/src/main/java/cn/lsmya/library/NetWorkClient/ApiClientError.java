package cn.lsmya.library.NetWorkClient;

/**
 * 错误/异常 实体类
 */
public class ApiClientError {
    protected int statusCode;
    private Exception exception;
    private String data;

    public ApiClientError(int statusCode, String data, Exception exception) {
        this.data = data;
        this.statusCode = statusCode;
        this.exception = exception;
    }

    // 获取HTTP状态码
    public int getStatusCode() {
        return statusCode;
    }

    // 获取异常
    public Exception getException() {
        return exception;
    }

    // 获取原网页内容
    public String getData() {
        return data;
    }
}
