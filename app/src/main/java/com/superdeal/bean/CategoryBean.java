package com.superdeal.bean;

import java.io.Serializable;

public class CategoryBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6327553482355931298L;

    private int id;
    private int status;
    private int sort;
    private int cid;
    private String name;
    private int langid;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLangid() {
        return langid;
    }

    public void setLangid(int langid) {
        this.langid = langid;
    }

}
