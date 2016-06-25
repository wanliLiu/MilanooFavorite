package com.superdeal.bean;

import java.io.Serializable;

public class FavoriteBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2420461721242161704L;

    private String Title;
    private String Url;

    public FavoriteBean(String mTitle, String mUrl) {
        Title = mTitle;
        Url = mUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }


}
