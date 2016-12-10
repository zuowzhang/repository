package com.meizu.push.http;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by zbin on 16-11-12.
 */
public abstract class Request<R> {
    String mUrl;
    Method mMethod;
    Map<String, String> mHeaderMap;
    Map<String, String> mParamMap;
    boolean mTrustedHost = false;

    public Request(String url,
                   Method method,
                   Map<String, String> paramMap,
                   Map<String, String> headerMap) {
        mUrl = url;
        if(TextUtils.isEmpty(mUrl)) {
            throw new RuntimeException("the parameter url must be not null");
        }
        mMethod = method;
        if(mMethod == null) {
            mMethod = Method.GET;
        }
        mHeaderMap = headerMap;
        mParamMap = paramMap;
    }

    public Request(String url,
                   Method method) {
        this(url, method, null, null);
    }

    protected String parseCharset(Map<String, List<String>> headers) {
        List<String> contentType = headers.get("content-type");
        if (contentType != null) {
            for(String item : contentType) {
                String[] pair = item.trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return "utf-8";
    }

    public Request<R> setHeaderMap(Map<String, String> headerMap) {
        mHeaderMap = headerMap;
        return this;
    }

    public Request<R> setParamMap(Map<String, String> paramMap) {
        mParamMap = paramMap;
        return this;
    }

    /**
     * 对https下发证书是否选择信任
     * @param trusted
     * @return
     */
    public Request<R> setTrustedHost(boolean trusted) {
        mTrustedHost = trusted;
        return this;
    }

    protected abstract Response<R> parserNetworkResponse(NetworkResponse networkResponse);


    public enum Method {
        GET,
        POST
    }

}
