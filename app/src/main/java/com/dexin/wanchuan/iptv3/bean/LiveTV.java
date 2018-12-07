package com.dexin.wanchuan.iptv3.bean;

public class LiveTV {

    private int id;
    private int number;
    private String name = "";
    private String url = "";
    private String rtspUrl = "";
    private String httpUrl = "";
    private String hlsUrl = "";
    private boolean checked = false;
    private boolean nameVisible = true;
    private boolean imageVisible = true;
    private String imagePath = "";
    private boolean timeshifting = false;
    private boolean timeshift = false;
    private long tsStartTime = 0l;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isNameVisible() {
        return nameVisible;
    }

    public void setNameVisible(boolean nameVisible) {
        this.nameVisible = nameVisible;
    }

    public boolean isImageVisible() {
        return imageVisible;
    }

    public void setImageVisible(boolean imageVisible) {
        this.imageVisible = imageVisible;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String hlsUrl) {
        this.hlsUrl = hlsUrl;
    }

    public boolean isTimeshifting() {
        return timeshifting;
    }

    public void setTimeshifting(boolean timeshifting) {
        this.timeshifting = timeshifting;
    }

    public boolean isTimeshift() {
        return timeshift;
    }

    public void setTimeshift(boolean timeshift) {
        this.timeshift = timeshift;
    }

    public long getTsStartTime() {
        return tsStartTime;
    }

    public void setTsStartTime(long tsStartTime) {
        this.tsStartTime = tsStartTime;
    }

}
