package com.superdeal.util;

import android.content.Context;
import android.text.TextUtils;

public class UserUtil {

    public static final String UserName = "UserName";
    public static final String UserId = "UserId";
    public static final String UserEmail = "UserEmail";

    /**
     * 保存用户信息
     *
     * @param id
     * @param Name
     * @param email
     */
    public static void saveUserInfo(Context ctx, String id, String Name, String email) {
        saveInfo(ctx, UserName, Name);
        saveInfo(ctx, UserId, id);
        saveInfo(ctx, UserEmail, email);
    }

    /**
     * 保存信息
     *
     * @param key
     * @param Value
     */
    private static void saveInfo(Context ctx, String key, String Value) {
        SP.getEdit(ctx).putString(key, Value).apply();
    }

    /**
     * @param ctx
     * @param key
     * @return
     */
    public static String getValue(Context ctx, String key) {

        return SP.getSp(ctx).getString(key, "");
    }

    /**
     * 当前是否登陆
     *
     * @return
     */
    public static boolean getLoginStaus(Context ctx) {
        if (TextUtils.isEmpty(getValue(ctx, UserId))) {
            return false;
        }

        return true;
    }
}
