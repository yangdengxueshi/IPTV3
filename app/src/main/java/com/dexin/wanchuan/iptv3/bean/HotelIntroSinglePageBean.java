package com.dexin.wanchuan.iptv3.bean;

/**
 * 酒店介绍单个页面实体
 */
public final class HotelIntroSinglePageBean {
    /**
     * content : 健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-
     * 健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-
     * 健身器材-健身器材-健身器材-健身器材-健身器材-健身器材-
     * id : 1
     * imagePath : ../res/custom/hotel/10353143,5120,2880.jpg
     * name : 健身器材
     * typeId : 1
     */

    private String content = "";
    private int id;
    private String imagePath = "";
    private String name = "";
    private int typeId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
