
package com.superdeal.util;

import android.util.Log;

import com.superdeal.BuildConfig;

public class MLog {

    public enum DebugControl {
        Mode_Online_Test,//线上测试环境
        Mode_Online,//线上环境
        Mode_OffLine//测试环境
    }

    /**
     * 注意，上线前需要设置成Mode_Online模式
     */
    private static DebugControl Mode = DebugControl.Mode_Online;
    /**
     * 是否开启全局打印日志
     * 发版的时候需要flase
     */
    public static boolean EnDebug;
    /**
     * 发版的时候为true
     */
    public static boolean AppReleaseVersion;
    /**
     * 是线上环境还是测试环境
     * 为ture为线上环境
     * 为false为测试环境
     * <p>
     * 发版的时候为true
     */
    public static boolean OnlineEnv;


    private static final String DEFAULT_TAG = "superdeal";

    /**
     * 初始化
     */
    static {
        if (BuildConfig.DEBUG)
            Mode = DebugControl.Mode_OffLine;
        else
            Mode = DebugControl.Mode_Online;

        switch (Mode) {
            case Mode_Online_Test://线上测试环境
                //打印日志关
                EnDebug = true;
                AppReleaseVersion = false;
                OnlineEnv = true;
                break;
            case Mode_Online://线上环境
                //打印日志关
                EnDebug = false;
                AppReleaseVersion = true;
                OnlineEnv = true;
                break;
            case Mode_OffLine://测试环境
                //打印日志关
                EnDebug = true;
                AppReleaseVersion = false;
                OnlineEnv = false;
                break;
            default:
                break;
        }
    }


    public static void i(String tag, String msg) {
        if (EnDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (EnDebug)
            Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (EnDebug)
            Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (EnDebug)
            Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (EnDebug)
            Log.w(tag, msg);
    }

    public static void i(String msg) {
        if (EnDebug)
            Log.i(DEFAULT_TAG, msg);
    }

    public static void e(String msg) {
        if (EnDebug)
            Log.e(DEFAULT_TAG, msg);
    }

    public static void d(String msg) {
        if (EnDebug)
            Log.d(DEFAULT_TAG, msg);
    }

    public static void v(String msg) {
        if (EnDebug)
            Log.v(DEFAULT_TAG, msg);
    }

    public static void w(String msg) {
        if (EnDebug)
            Log.w(DEFAULT_TAG, msg);
    }

}
