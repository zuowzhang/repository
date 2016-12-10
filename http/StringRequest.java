package com.meizu.push.http;

import java.util.Map;

/**
 * Created by zbin on 16-11-12.
 */
public class StringRequest extends Request<String> {

    public StringRequest(String url,
                         Method method) {
        super(url, method);
    }

    public StringRequest(String url, Method method, Map<String, String> headerMap, Map<String, String> paramMap) {
        super(url, method, headerMap, paramMap);
    }

    @Override
    protected Response<String> parserNetworkResponse(NetworkResponse networkResponse) {
        Response<String> response = new Response<>();
        response.mStatusCode = networkResponse.mStatusCode;
        response.mThrowable = networkResponse.mThrowable;
        if(response.mThrowable == null) {
            String msg = "";
            try {
                if(networkResponse.mRawData != null && networkResponse.mRawData.length > 0) {
                    msg = new String(networkResponse.mRawData, parseCharset(networkResponse.mHeaderMap));
                }
            } catch (Exception e) {
                msg = e.getMessage();
            }
            if(response.mStatusCode / 100 == 2) {
                response.mResult = msg;
            } else {
                response.mErrMsg = msg;
            }
        }
        return response;
    }
}
