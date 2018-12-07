package com.dexin.wanchuan.iptv3.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.io.File;

@SuppressLint("SdCardPath")
public class IptvSP implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String XML_NAME = "settings";
    private static final String TAG = "IptvSP";

    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NEED_PASSWORD = "need_password";
    private static final String KEY_SERVER_IP = "server_ip";
    private static final String KEY_SERVER_PORT = "server_port";

    private String dataPath;
    @SuppressWarnings("unused")
    private Context context;
    private SharedPreferences mShared;
    private OnValueChangedListener mOnValueChangedListener;

    public IptvSP(Context context) {
        this.context = context;
        dataPath = "/sdcard/IPTV/";
        File path = new File(dataPath);
        if (!path.exists())
            path.mkdirs();
        Log.i(TAG, "sdcard wr " + path.exists() + " " + path.canWrite() + " " + path.canRead());
        if (!path.canRead() || !path.canWrite()) {
            dataPath = null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        this.mShared = sharedPreferences;
        mShared.registerOnSharedPreferenceChangeListener(this);
    }

    public IptvSP() {
        dataPath = "/sdcard/IPTV/";
        File path = new File(dataPath);
        if (!path.exists())
            path.mkdirs();
        Log.i(TAG, "sdcard wr " + path.exists() + " " + path.canWrite() + " " + path.canRead());
        if (!path.canRead() || !path.canWrite()) {
            dataPath = null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        this.mShared = sharedPreferences;
        mShared.registerOnSharedPreferenceChangeListener(this);
    }

    public SharedPreferences getSharedPreferences() {
        return mShared;
    }

    public String getHttpVodPath() {
        return getAddress() + IptvConfig.VOD_PATH;
    }

    private String getAddress() {
        return "http://" + getServerIP() + ":" + mShared.getInt(KEY_SERVER_PORT, IptvConfig.DEFAULT_SERVER_PORT);
    }

    public String getIPAddress() {
        return "http://" + getServerIP() + ":" + mShared.getInt(KEY_SERVER_PORT, IptvConfig.DEFAULT_SERVER_PORT);
    }

    public String getLiveRtspUrl(int id) {
        // getServerIP()
        return "rtsp://" + getServerIP() + ":" + IptvConfig.DEFAULT_RTSP_PORT + "/live/" + id + ".sdp";
    }

    public String getPassword() {
        return mShared.getString(KEY_PASSWORD, "888888");
    }

    public void setPassword(String password) {
        Editor editor = mShared.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public boolean isNeedPassword() {
        return mShared.getBoolean(KEY_NEED_PASSWORD, true);
    }

    public void setNeedPassword(boolean need) {
        Editor editor = mShared.edit();
        editor.putBoolean(KEY_NEED_PASSWORD, need);
        editor.commit();
    }

    public String getUrl(String tag) {
        String url = getAddress() + IptvConfig.WEBSERVICE_PATH + tag;
        return url;
    }

    public String getImagePath(String tag) {
        String path = getAddress() + IptvConfig.IMAGE_PATH + tag;
        return path;
    }

    public String getServerIP() {
        return mShared.getString(KEY_SERVER_IP, IptvConfig.DEFAULT_SERVER_IP);
    }

    public void setServerIp(String ip) {
        Editor editor = mShared.edit();
        editor.putString(KEY_SERVER_IP, ip);
        editor.commit();
    }

    public void setServerPort(int port) {
        Editor editor = mShared.edit();
        editor.putInt(KEY_SERVER_PORT, port);
        editor.commit();
    }

    public int getServerPort() {
        return mShared.getInt(KEY_SERVER_PORT, IptvConfig.DEFAULT_SERVER_PORT);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        mOnValueChangedListener = onValueChangedListener;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mOnValueChangedListener != null) {
            mOnValueChangedListener.onValueChanged(key);
        }
    }

    public interface OnValueChangedListener {
        void onValueChanged(String key);
    }

}
