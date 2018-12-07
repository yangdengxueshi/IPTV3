package com.dexin.wanchuan.iptv3.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Gson解析器
 */
public final class GsonParser {
    private GsonParser() {
    }

    private static final class GsonHolder {
        private static final Gson GSON = new Gson();
    }

    /**
     * 从okhttp3.Response对象中获取Json字符串
     *
     * @param response okhttp3.Response对象
     * @return json字符串
     * @throws Exception 抛出的异常
     */
    @NonNull
    public static String getJsonStr(@NotNull final Response response) throws Exception {
        String jsonStr = "";
        if (response.isSuccessful()) {
            final ResponseBody responseBody = response.body();
            if (responseBody != null) jsonStr = responseBody.string();
        }
        response.close();
        return jsonStr;
    }

    /**
     * json字符串转对象
     *
     * @param json     json字符串
     * @param classOfT T的类
     * @param <T>      T
     * @return json字符串转换成的对象
     */
    @Nullable
    public static <T> T toObject(@Nullable final String json, @NonNull final Class<T> classOfT) throws Exception {
        return toObjectOrList(json, classOfT);
    }

    @Nullable
    public static <T> T toObjectOrList(@Nullable final String json, @NonNull final Type typeOfT) throws Exception {
        final T t;
        try {
            t = GsonHolder.GSON.fromJson(json, typeOfT);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return t;
    }
}
