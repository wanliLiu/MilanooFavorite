package com.superdeal.bean;

import java.io.Serializable;

public class WebsiteTagBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6327553482355931298L;

    private int id;
    private String name;
    private String logo;
    private String url;
    private int sort;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }


}
