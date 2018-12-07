package com.dexin.wanchuan.iptv3.bean;

/**
 * Created by Administrator on 2018/11/29.
 */

public class MainMenu {
    private int view_id;
    private String name;
    private Class<?> cls;
    private boolean skip;

    public MainMenu() {
    }

    public MainMenu(int view_id, String name, Class<?> cls, boolean skip) {
        this.view_id = view_id;
        this.name = name;
        this.cls = cls;
        this.skip = skip;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public int getView_id() {
        return view_id;
    }

    public void setView_id(int view_id) {
        this.view_id = view_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
}
