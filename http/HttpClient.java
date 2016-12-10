package com.meizu.push.http;

import com.meizu.push.base.ExecutorProxy;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * Created by zbin on 16-11-12.
 */
public class HttpClient {

    private byte[] params2Bytes(Map<String, String> params, String paramsEncoding) throws IOException {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (String key : params.keySet()) {
                if (key == null) {
                    continue;
                }
                String value = params.get(key);
                encodedParams.append(URLEncoder.encode(key, paramsEncoding));
                encodedParams.append('=');
                if (value == null) {
                    encodedParams.append("");
                } else {
                    encodedParams.append(URLEncoder.encode(value, paramsEncoding));
                }
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new IOException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    private void trustAllHosts(HttpsURLConnection connection) throws NoSuchAlgorithmException,
            KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        connection.setSSLSocketFactory(sc.getSocketFactory());
    }


    NetworkResponse exec(final Request<?> request) {
        NetworkResponse networkResponse = new NetworkResponse();
        try {
            final URL url = new URL(request.mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(connection instanceof HttpsURLConnection) {
                if(request.mTrustedHost) {
                    trustAllHosts((HttpsURLConnection)connection);
                }
                ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        if(request.mTrustedHost) {
                            return true;
                        } else {
                            HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
                            return verifier.verify(url.getHost(), sslSession) ? true :
                                    verifier.verify(s, sslSession);
                        }
                    }
                });
            }
            if(request.mHeaderMap != null) {
                for(Map.Entry<String, String> entry : request.mHeaderMap.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            connection.setRequestMethod(request.mMethod.name());
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.setInstanceFollowRedirects(true);
            if(request.mParamMap != null) {
                connection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(params2Bytes(request.mParamMap, "utf-8"));
                dataOutputStream.close();
            }
            InputStream inputStream = null;
            networkResponse.mStatusCode = connection.getResponseCode();
            networkResponse.mHeaderMap = connection.getHeaderFields();
            if(networkResponse.mStatusCode / 100 == 2) {
                inputStream = connection.getInputStream();
            }
            if(inputStream == null) {
                inputStream = connection.getErrorStream();
            }
            if(inputStream != null) {
                int len;
                byte[] buffer = new byte[512];
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((len = inputStream.read(buffer)) > 0) {
                    byteStream.write(buffer, 0, len);
                }
                networkResponse.mRawData = byteStream.toByteArray();
                inputStream.close();
            }
        } catch (Throwable throwable) {
            networkResponse.mThrowable = throwable;
        }
        return networkResponse;
    }

    /**
     * 同步执行http请求
     * @param request 请求
     * @return 只要是非2xx响应，一律返回null，只要不返回null,一定是能正确解析响应结果
     */
    public <R> R execute(Request<R> request) {
        NetworkResponse networkResponse = exec(request);
        Response<R> response = request.parserNetworkResponse(networkResponse);
        return response.mResult;
    }

    /**
     * 异步执行http请求
     * @param request 请求
     * @param listener 监听器
     * @param <R> http响应的类型(String, Json...)
     */
    public <R> void enqueue(final Request<R> request, final Response.IListener<R> listener) {
        ExecutorProxy.get().execute(new Runnable() {
            @Override
            public void run() {
                NetworkResponse networkResponse = exec(request);
                Response<R> response = request.parserNetworkResponse(networkResponse);
                if(response.mResult != null) {
                    listener.onSuccess(response.mResult);
                } else if(response.mThrowable != null) {
                    listener.onException(response.mThrowable);
                } else {
                    listener.onError(response.mStatusCode, response.mErrMsg);
                }
            }
        });
    }
}
