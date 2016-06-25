package com.superdeal.base;

import com.superdeal.util.MLog;

/**
 * https://developers.facebook.com/ facebook开发者
 */
public class Constants {

    /**
     * 常量
     */
    public static final String Intent_Title = "Intent_Title";
    public static final String Intent_URL = "Intent_URL";
    public static final String Intent_ID = "WebsiteID";

    //首页启动停留的时间
    public static final int DelayTime = 2000;

    //服务器地址
    public static String SERVER;
    public static String ICON;

    static {
        //测试环境
        if (!MLog.OnlineEnv) {

            SERVER = "http://shopping.zoopple.com/";//http://test.milanoo.com/
            ICON = SERVER + "upload/";
        } else {
            SERVER = "http://shopping.zoopple.com/";//http://test.milanoo.com/
            ICON = SERVER + "upload/";
        }
    }
}
