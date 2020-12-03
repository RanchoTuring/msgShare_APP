package com.rancho.msgshare.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 基于Gson库封装的JsonUtil工具类
 */
public class JsonUtil {
    static Gson gson = new Gson();

    public static <T> T getObject(String jsonStr, Class<T> clazz) {
        return gson.fromJson(jsonStr, clazz);
    }

    public static <T> List<T> getList(String jsonStr, Class<T> clazz) {
        return gson.fromJson(jsonStr, new TypeToken<List<T>>() {
        }.getType());
    }
}
