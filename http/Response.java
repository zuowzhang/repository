package com.meizu.push.http;

/**
 * Created by zbin on 16-11-12.
 */
public class Response<R> {
    int mStatusCode;
    R mResult;
    String mErrMsg;
    Throwable mThrowable;

    public interface IListener<R> {
        /**
         * http请求得到成功服务器2xx响应
         * @param result http 响应的结果
         */
        void onSuccess(R result);

        /**
         * http请求得到服务器非2xx响应
         * @param statusCode 响应码
         * @param errMsg 响应的错误信息
         */
        void onError(int statusCode, String errMsg);

        /**
         * http请求发送失败
         * @param throwable 异常信息
         */
        void onException(Throwable throwable);
    }
}
