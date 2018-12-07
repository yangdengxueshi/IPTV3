package com.dexin.wanchuan.iptv3.bean;

public class Settings {

    public static final String PROTOCOL_RTSP = "rtsp";
    public static final String PROTOCOL_UDP = "udp";
    public static final String PROTOCOL_MSCORE = "mscore";
    public static final String PROTOCOL_HTTP = "http";
    public static final String PROTOCOL_HLS = "hls";
    public static final int CHARGE_MODE_PREPAID = 1;
    public static final int CHARGE_MODE_ADVANCE = 2;

    private String symbol = "";
    private int vodFreeMinutes = 5;
    private int apkUpdateType = 1;
    private String protocol = PROTOCOL_RTSP;
    private int timeout = 1;
    private boolean showWaterMark = false;
    private boolean showWaterMarkVod = false;
    private boolean showWelcomeBackground = false;
    private boolean showLogo = true;
    private String timeshiftServerIp = "127.0.0.1";
    private int timeshiftServerPort = 9090;
    private String timeshiftPlayIp = "127.0.0.1";
    private String timeshiftPlayPort = "8080";
    private String timeshiftPlayPath = "/iptv2/vod";
    private String timeshiftPlayProtocol = "http";
    private int timeshiftDays = 7;
    private int chargeMode = 1;
    private int indexArea; // 1:image,2:video,3:直播
    private String timeFormat = "";
    private boolean checkOutCleanFavorite = false;
    private String operatorNumber = "";
    private boolean menuCaterVisible;
    private boolean menuSceneryVisible;
    private boolean menuRecVisible = false;
    private boolean menuAppVisible = true;
    private boolean menuVodVisible = true;
    private boolean menuRoomServiceVisible = false;
    private boolean menuHotelVisible = true;
    private boolean menuSystemVisible = true;
    private boolean menuConsumeVisible = false;
    private boolean menuCheckOutVisible = false;
    private boolean menuCIBNVisible = false;
    private boolean menuiqiyiVisible = false;
    private int pictureInterval = 5; // 图片切换时间间隔,单位：秒
    private int timeshiftHours = 2;
    private int pmsType = 0;
    private String phone = ""; // 首页电话号码

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public boolean isShowWaterMark() {
        return showWaterMark;
    }

    public void setShowWaterMark(boolean showWaterMark) {
        this.showWaterMark = showWaterMark;
    }

    public boolean isShowWaterMarkVod() {
        return showWaterMarkVod;
    }

    public void setShowWaterMarkVod(boolean showWaterMarkVod) {
        this.showWaterMarkVod = showWaterMarkVod;
    }

    public boolean isShowWelcomeBackground() {
        return showWelcomeBackground;
    }

    public void setShowWelcomeBackground(boolean showWelcomeBackground) {
        this.showWelcomeBackground = showWelcomeBackground;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getVodFreeMinutes() {
        return vodFreeMinutes;
    }

    public void setVodFreeMinutes(int vodFreeMinutes) {
        this.vodFreeMinutes = vodFreeMinutes;
    }

    public int getApkUpdateType() {
        return apkUpdateType;
    }

    public void setApkUpdateType(int apkUpdateType) {
        this.apkUpdateType = apkUpdateType;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getTimeshiftServerIp() {
        return timeshiftServerIp;
    }

    public void setTimeshiftServerIp(String timeshiftServerIp) {
        this.timeshiftServerIp = timeshiftServerIp;
    }

    public int getTimeshiftServerPort() {
        return timeshiftServerPort;
    }

    public void setTimeshiftServerPort(int timeshiftServerPort) {
        this.timeshiftServerPort = timeshiftServerPort;
    }

    public String getTimeshiftPlayIp() {
        return timeshiftPlayIp;
    }

    public void setTimeshiftPlayIp(String timeshiftPlayIp) {
        this.timeshiftPlayIp = timeshiftPlayIp;
    }

    public String getTimeshiftPlayPort() {
        return timeshiftPlayPort;
    }

    public void setTimeshiftPlayPort(String timeshiftPlayPort) {
        this.timeshiftPlayPort = timeshiftPlayPort;
    }

    public String getTimeshiftPlayPath() {
        return timeshiftPlayPath;
    }

    public void setTimeshiftPlayPath(String timeshiftPlayPath) {
        this.timeshiftPlayPath = timeshiftPlayPath;
    }

    public String getTimeshiftPlayProtocol() {
        return timeshiftPlayProtocol;
    }

    public void setTimeshiftPlayProtocol(String timeshiftPlayProtocol) {
        this.timeshiftPlayProtocol = timeshiftPlayProtocol;
    }

    public int getTimeshiftDays() {
        return timeshiftDays;
    }

    public void setTimeshiftDays(int timeshiftDays) {
        this.timeshiftDays = timeshiftDays;
    }

    public int getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(int chargeMode) {
        this.chargeMode = chargeMode;
    }

    public int getIndexArea() {
        return indexArea;
    }

    public void setIndexArea(int indexArea) {
        this.indexArea = indexArea;
    }

    public boolean isCheckOutCleanFavorite() {
        return checkOutCleanFavorite;
    }

    public void setCheckOutCleanFavorite(boolean checkOutCleanFavorite) {
        this.checkOutCleanFavorite = checkOutCleanFavorite;
    }

    public String getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorNumber(String operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    public boolean isShowLogo() {
        return showLogo;
    }

    public void setShowLogo(boolean showLogo) {
        this.showLogo = showLogo;
    }

    public boolean isMenuCaterVisible() {
        return menuCaterVisible;
    }

    public void setMenuCaterVisible(boolean menuCaterVisible) {
        this.menuCaterVisible = menuCaterVisible;
    }

    public boolean isMenuSceneryVisible() {
        return menuSceneryVisible;
    }

    public void setMenuSceneryVisible(boolean menuSceneryVisible) {
        this.menuSceneryVisible = menuSceneryVisible;
    }

    public boolean isMenuRecVisible() {
        return menuRecVisible;
    }

    public void setMenuRecVisible(boolean menuRecVisible) {
        this.menuRecVisible = menuRecVisible;
    }

    public boolean isMenuVodVisible() {
        return menuVodVisible;
    }

    public void setMenuVodVisible(boolean menuVodVisible) {
        this.menuVodVisible = menuVodVisible;
    }

    public boolean isMenuRoomServiceVisible() {
        return menuRoomServiceVisible;
    }

    public void setMenuRoomServiceVisible(boolean menuRoomServiceVisible) {
        this.menuRoomServiceVisible = menuRoomServiceVisible;
    }

    public boolean isMenuHotelVisible() {
        return menuHotelVisible;
    }

    public void setMenuHotelVisible(boolean menuHotelVisible) {
        this.menuHotelVisible = menuHotelVisible;
    }

    public boolean isMenuSystemVisible() {
        return menuSystemVisible;
    }

    public void setMenuSystemVisible(boolean menuSystemVisible) {
        this.menuSystemVisible = menuSystemVisible;
    }

    public boolean isMenuAppVisible() {
        return menuAppVisible;
    }

    public void setMenuAppVisible(boolean menuAppVisible) {
        this.menuAppVisible = menuAppVisible;
    }

    public boolean isMenuCheckOutVisible() {
        return menuCheckOutVisible;
    }

    public void setMenuCheckOutVisible(boolean menuCheckOutVisible) {
        this.menuCheckOutVisible = menuCheckOutVisible;
    }

    public boolean isMenuCibnVisible() {
        return menuCIBNVisible;
    }

    public void setMenuCibnVisible(boolean menuCheckOutVisible) {
        this.menuCIBNVisible = menuCheckOutVisible;
    }

    public boolean isMenuIqiyiVisible() {
        return menuiqiyiVisible;
    }

    public void setMenuIqiyiisible(boolean menuCheckOutVisible) {
        this.menuiqiyiVisible = menuCheckOutVisible;
    }

    public boolean isMenuConsumeVisible() {
        return menuConsumeVisible;
    }

    public void setMenuConsumeVisible(boolean menuConsumeVisible) {
        this.menuConsumeVisible = menuConsumeVisible;
    }

    public int getPictureInterval() {
        return pictureInterval;
    }

    public void setPictureInterval(int pictureInterval) {
        this.pictureInterval = pictureInterval;
    }

    public int getTimeshiftHours() {
        return timeshiftHours;
    }

    public void setTimeshiftHours(int timeshiftHours) {
        this.timeshiftHours = timeshiftHours;
    }

    public int getPmsType() {
        return pmsType;
    }

    public void setPmsType(int pmsType) {
        this.pmsType = pmsType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean contentEqual(Settings settings) {
        if (vodFreeMinutes != settings.vodFreeMinutes)
            return false;
        if (apkUpdateType != settings.apkUpdateType)
            return false;
        if (timeout != settings.timeout)
            return false;
        if (timeshiftDays != settings.timeshiftDays)
            return false;
        if (chargeMode != settings.chargeMode)
            return false;
        if (indexArea != settings.indexArea)
            return false;
        if (timeshiftServerPort != settings.timeshiftServerPort)
            return false;
        if (pictureInterval != settings.pictureInterval)
            return false;
        if (timeshiftHours != settings.timeshiftHours)
            return false;
        if (showWaterMark != settings.showWaterMark)
            return false;
        if (showWaterMarkVod != settings.showWaterMarkVod)
            return false;
        if (showLogo != settings.showLogo)
            return false;
        if (checkOutCleanFavorite != settings.checkOutCleanFavorite)
            return false;
        if (menuCaterVisible != settings.menuCaterVisible)
            return false;
        if (menuSceneryVisible != settings.menuSceneryVisible)
            return false;
        if (menuRecVisible != settings.menuRecVisible)
            return false;
        if (menuAppVisible != settings.menuAppVisible)
            return false;
        if ((symbol == null && settings.symbol != null) || (symbol != null && settings.symbol == null)
                || (symbol != null && settings.symbol != null && !symbol.contentEquals(settings.symbol)))
            return false;
        if ((protocol == null && settings.protocol != null) || (protocol != null && settings.protocol == null)
                || (protocol != null && settings.protocol != null && !protocol.contentEquals(settings.protocol)))
            return false;
        if ((timeshiftServerIp == null && settings.timeshiftServerIp != null)
                || (timeshiftServerIp != null && settings.timeshiftServerIp == null)
                || (timeshiftServerIp != null && settings.timeshiftServerIp != null
                && !timeshiftServerIp.contentEquals(settings.timeshiftServerIp)))
            return false;
        if ((timeshiftPlayIp == null && settings.timeshiftPlayIp != null)
                || (timeshiftPlayIp != null && settings.timeshiftPlayIp == null)
                || (timeshiftPlayIp != null && settings.timeshiftPlayIp != null
                && !timeshiftPlayIp.contentEquals(settings.timeshiftPlayIp)))
            return false;
        if ((timeshiftPlayPort == null && settings.timeshiftPlayPort != null)
                || (timeshiftPlayPort != null && settings.timeshiftPlayPort == null)
                || (timeshiftPlayPort != null && settings.timeshiftPlayPort != null
                && !timeshiftPlayPort.contentEquals(settings.timeshiftPlayPort)))
            return false;
        if ((timeshiftPlayPath == null && settings.timeshiftPlayPath != null)
                || (timeshiftPlayPath != null && settings.timeshiftPlayPath == null)
                || (timeshiftPlayPath != null && settings.timeshiftPlayPath != null
                && !timeshiftPlayPath.contentEquals(settings.timeshiftPlayPath)))
            return false;
        if ((timeshiftPlayProtocol == null && settings.timeshiftPlayProtocol != null)
                || (timeshiftPlayProtocol != null && settings.timeshiftPlayProtocol == null)
                || (timeshiftPlayProtocol != null && settings.timeshiftPlayProtocol != null
                && !timeshiftPlayProtocol.contentEquals(settings.timeshiftPlayProtocol)))
            return false;
        if ((timeFormat == null && settings.timeFormat != null) || (timeFormat != null && settings.timeFormat == null)
                || (timeFormat != null && settings.timeFormat != null
                && !timeFormat.contentEquals(settings.timeFormat)))
            return false;
        if ((operatorNumber == null && settings.operatorNumber != null)
                || (operatorNumber != null && settings.operatorNumber == null) || (operatorNumber != null
                && settings.operatorNumber != null && !operatorNumber.contentEquals(settings.operatorNumber)))
            return false;
        return (phone != null || settings.phone == null) && (phone == null || settings.phone != null)
                && (phone == null || settings.phone == null || phone.contentEquals(settings.phone));
    }

}
