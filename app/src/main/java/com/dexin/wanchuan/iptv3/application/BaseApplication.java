package com.dexin.wanchuan.iptv3.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.dexin.wanchuan.iptv3.BuildConfig;
import com.dexin.wanchuan.iptv3.bean.Settings;
import com.dexin.wanchuan.iptv3.util.HttpUtil;
import com.dexin.wanchuan.iptv3.util.IptvSP;
import com.dexin.wanchuan.iptv3.util.WebTag;
import com.dexin.wanchuan.iptv3.widget.DigitalClock;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.vondear.rxtool.RxTool;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/11/26.
 */

public class BaseApplication extends Application {

    private IptvSP sp;
    public Context mContext;
    public static BaseApplication instance;
    public static Typeface typeface;

    private boolean connected = false;// 连接状态

    private Timer timer;
    private TimerTask task;

    private Settings settings = new Settings();// 配置
    private Calendar calendar = Calendar.getInstance();// 时间
    public static final String ACTION_CLIENT_UPDATE = "com.dexin.iptv2.CLIENT_UPDATE";
    public static final String ACTION_SETTING_UPDATE = "com.dexin.iptv2.SETTING_UPDATE";
    public static final String ACTION_FORCE_CHANGE_PROGRAM = "com.dexin.iptv2.FORCE_CHANGE_PROGRAM";


    public BaseApplication() {
        mContext = this;
    }

    public boolean isConnected() {
        return connected;
    }

    public Settings getSettings() {
        return settings;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public static BaseApplication getInstance() {
        if (instance == null) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        typeface = Typeface.createFromAsset(getAssets(), "fz.ttf");
        sp = new IptvSP(this);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Log.d("DataApplication", "update info");
                getDataFromServer();
            }
        };
        timer.schedule(task, 2000, 30 * 1000);
        RxTool.init(this);
        Utils.init(this);
        initOkGo();
    }

    private void getDataFromServer() {
        ActivityManager am;
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        getSettingsFromServer();
        getTimeFromServer();
    }

    private void getTimeFromServer() {
        String url = sp.getUrl(WebTag.TAG_CURRENT_TIMEMILLIS);
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String string = response.body().string();
                    Long timeMillis = (new Gson()).fromJson(string, new TypeToken<Long>() {
                    }.getType());
                    calendar.setTimeInMillis(timeMillis);
                    DigitalClock.adjustTime(calendar.getTime());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getSettingsFromServer() {
        String url = sp.getUrl(WebTag.TAG_SETTINGS);
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                connected = false;
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) {
                try {
                    connected = true;
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    Settings data = null;
                    data = gson.fromJson(responseData, new TypeToken<Settings>() {
                    }.getType());
                    if ((data == null && settings != null) || (data != null && settings == null)
                            || (data != null && settings != null && !data.contentEqual(settings)))
                        broadcastSettingUpdate();
                    settings = data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void reset() {
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Log.d("DataApplication", "update info");
                getDataFromServer();
            }
        };
        timer.schedule(task, 0, 30 * 1000);
    }

    private void broadcastSettingUpdate() {
        getApplicationContext().sendBroadcast(new Intent(ACTION_SETTING_UPDATE));
    }

    private void initOkGo() {
        final OkHttpClient.Builder lOkHttpClientBuilder = new OkHttpClient.Builder();
        // 配置log
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor lHttpLoggingInterceptor = new HttpLoggingInterceptor("TAG_OkGo");
            lHttpLoggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
            lHttpLoggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
            lOkHttpClientBuilder.addInterceptor(lHttpLoggingInterceptor);                    //添加OkGo默认debug日志
        }
        // 配置超时时间(缺省60s)
        lOkHttpClientBuilder.readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).connectTimeout(5, TimeUnit.SECONDS);
        // 其它统一的配置
        OkGo.getInstance().init(this).setOkHttpClient(lOkHttpClientBuilder.build()).setRetryCount(3);
    }
}
