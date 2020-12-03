package com.rancho.msgshare.utils;

import com.rancho.msgshare.entity.HttpParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    /**
     * 重写saveFromResponse和loadForRequest方法，使OkHttpClient可以存储Cookie
     */
    private static OkHttpClient httpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }).build();

    public static void get(String url, Callback callback, HttpParam... params) {
        String query = "";
        if (params.length != 0) {
            query = getQueryString(params);
        }
        Request request = new Request.Builder()
                .url(url + query)
                .build();

        httpClient.newCall(request).enqueue(callback);
    }

    public static void post(String url, Callback callback, HttpParam... params) {
        Request request = new Request.Builder()
                .url(url)
                .post(getRequestBody(params))
                .build();

        httpClient.newCall(request).enqueue(callback);
    }

    public static void put(String url, Callback callback, HttpParam... params) {
        Request request = new Request.Builder()
                .url(url)
                .put(getRequestBody(params))
                .build();

        httpClient.newCall(request).enqueue(callback);
    }

    public static void delete(String url, Callback callback, HttpParam... params) {
        Request request = new Request.Builder()
                .url(url)
                .delete(getRequestBody(params))
                .build();

        httpClient.newCall(request).enqueue(callback);
    }

    private static RequestBody getRequestBody(HttpParam... params) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params.length != 0) {
            for (HttpParam param : params) {
                bodyBuilder.add(param.getKey(), param.getVal());
            }
        }
        return bodyBuilder.build();
    }

    private static String getQueryString(HttpParam... params) {
        StringBuilder sb = new StringBuilder("?");
        for (HttpParam param : params) {
            sb.append(param.getKey());
            sb.append("=");
            sb.append(param.getVal());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
