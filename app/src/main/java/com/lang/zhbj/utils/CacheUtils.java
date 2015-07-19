package com.lang.zhbj.utils;

import android.content.Context;

/**
 * Created by Lang on 2015/7/18.
 */
public class CacheUtils {

    /**
     * 设置缓存
     * @param context 上下文
     * @param key url
     * @param value json文件
     */
    public static void setCache(Context context, String key, String value){
        PrefUtil.setString(context, key, value);
    }

    /**
     * 获取缓存
     * @param context 上下文
     * @param key url
     * @return json数据
     */
    public static String getCache(Context context, String key){
        return PrefUtil.getString(context, key, null);
    }
}
