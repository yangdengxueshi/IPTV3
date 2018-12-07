package com.dexin.wanchuan.iptv3.bean;

/**
 * Created by Administrator on 2018/11/30.
 */

public class HotelServiceMenu {
    private int id;
    private String name;
    private String content;
    private Class<?> cls;
    private boolean skip;
    private String url;

    public HotelServiceMenu(int id, String name, String content, Class<?> cls, boolean skip, String url) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.cls = cls;
        this.skip = skip;
        this.url = url;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
