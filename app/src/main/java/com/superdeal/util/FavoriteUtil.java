package com.superdeal.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.superdeal.bean.FavoriteBean;

public class FavoriteUtil {

    /**
     * 添加一个收藏记录，有救删除，没有就添加
     *
     * @param bean
     */
    public static void AddFavorite(Context ctx, FavoriteBean favorite) {
        //
        if (!isHaveTheFavorite(ctx, favorite)) {
            List<FavoriteBean> list;
            String data = SP.getSp(ctx).getString("Favorite", "");
            if (TextUtils.isEmpty(data)) {
                list = new ArrayList<FavoriteBean>();
            } else {
                list = JSON.parseArray(data, FavoriteBean.class);
            }
            list.add(favorite);
            SP.getEdit(ctx).putString("Favorite", JSON.toJSONString(list));
        } else {
            List<FavoriteBean> list = JSON.parseArray(SP.getSp(ctx).getString("Favorite", ""), FavoriteBean.class);
            for (FavoriteBean favoriteBean : list) {
                if (favoriteBean.getTitle().equals(favorite.getTitle()) &&
                        favoriteBean.getUrl().equals(favorite.getUrl())) {
                    list.remove(favoriteBean);
                    break;
                }
            }
            SP.getEdit(ctx).putString("Favorite", JSON.toJSONString(list));
        }
    }

    /**
     * @return
     */
    public static boolean isHaveTheFavorite(Context ctx, FavoriteBean favorite) {
        String data = SP.getSp(ctx).getString("Favorite", "");

        if (!TextUtils.isEmpty(data)) {
            List<FavoriteBean> temp = JSON.parseArray(data, FavoriteBean.class);
            if (temp != null && temp.size() > 0) {
                for (FavoriteBean bean : temp) {
                    if (bean.getTitle().equals(favorite.getTitle()) &&
                            bean.getUrl().equals(favorite.getUrl())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @param ctx
     * @return
     */
    public static List<FavoriteBean> getFavoriteList(Context ctx) {
        String data = SP.getSp(ctx).getString("Favorite", "");
        if (!TextUtils.isEmpty(data)) {
            return JSON.parseArray(data, FavoriteBean.class);
        }

        return null;
    }
}
