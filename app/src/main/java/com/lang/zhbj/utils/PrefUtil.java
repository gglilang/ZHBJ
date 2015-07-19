package com.lang.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lang on 2015/7/11.
 */
public class PrefUtil {

    private static final String PREF_NAME = "config";

    public static boolean getBoolean(Context context, String key, boolean defaultValue){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key, String defaultValue){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return  sp.getString(key, defaultValue);
    }

    public static void setString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
}
