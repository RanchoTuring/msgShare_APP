package com.rancho.msgshare.utils;

import com.rancho.msgshare.entity.HttpParam;


import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    private static OkHttpClient httpClient = new OkHttpClient();

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
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params.length != 0) {
            for (HttpParam param : params) {
                bodyBuilder.add(param.getKey(), param.getVal());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(bodyBuilder.build())
                .build();

        httpClient.newCall(request).enqueue(callback);
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
