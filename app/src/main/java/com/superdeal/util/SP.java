package com.superdeal.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地存储
 *
 * @author wst
 */
public class SP {

    /**
     * 语言
     */
    public static final String LANGUAGE = "language";
    /**
     * 货币
     */
    public static final String CURRENCY = "currency";
    /**
     * 汇率信息
     */
    public static final String CurrencyRate = "CurrencyRate";

    /**
     * 系统语言改变
     */
    public static final String SystemLanuageChange = "lanuageChange";

    /**
     * 设备唯一ID
     */
    public static final String DEVICE_ID = "DEVICE_ID";

    /**
     * 用户注册的邮箱
     */
    public static final String USER_Email = "last_email";

    /**
     * 用户注册的邮箱
     */
    public static final String New_Coupon = "NewCoupon";

    /**
     * 搜索的历史数据
     */
    public static final String SearchHistory = "search_history";

    /**
     * 帮助修改bug,记录bug日志错误，快速找到产品ID
     */
    public static final String HelpDugProductID = "Product_id_debug";

    /**
     * 用来标记是否第一次登陆程序，parse推送上传通道没有
     */
    public static final String HasParseChannelSub = "HasParseChannelSub";

    /**
     * 第一次用的时候，清除登陆信息
     */
    public static final String isFist = "ISFistTime";
    /**
     * 上一次登陆的时间
     */
    public static final String LastLoginTime = "LastLoginTime";

    /**
     * 是否允许接受推送
     */
    public static final String EnablePushNotification = "EnablePushNotification";
    /**
     * 是否需要合并wishlist的数据
     */
    public static final String NeedMergeWisht = "NeedMergeWisht";


    public static SharedPreferences getSp(Context ctx) {
        return ctx.getSharedPreferences("setting", Context.MODE_MULTI_PROCESS);
    }

    public static SharedPreferences.Editor getEdit(Context ctx) {
        return ctx.getSharedPreferences("setting", Context.MODE_MULTI_PROCESS).edit();
    }

}
