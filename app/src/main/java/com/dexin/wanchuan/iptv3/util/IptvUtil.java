package com.dexin.wanchuan.iptv3.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dexin.wanchuan.iptv3.bean.ApplicationInfo;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
@SuppressLint({"SdCardPath", "DefaultLocale", "InlinedApi"})
public class IptvUtil {

    private static final String TAG = "IptvUtil";

    public static final boolean IS_AVD = Build.MODEL.contentEquals("sdk")
            || Build.MODEL.contentEquals("Android SDK built for x86")
            || Build.MODEL.contentEquals("Android SDK built for x86_64");

    public static final boolean IS_STB = hasEthernet() && !IS_AVD;

    public static final boolean IS_DEXIN_AML8726_STB = Build.ID.contentEquals("JDQ39")
            && Build.HOST.contentEquals("iptv");

    public static final boolean IS_DEXIN_S805_STB = Build.ID.contentEquals("KOT49H")
            && Build.HOST.contentEquals("iptv");

    public static final boolean IS_AISHANGKE_RK3229_STB = Build.ID.contentEquals("NHG47K")
            || Build.HOST.contentEquals("aike5");

    public static final boolean IS_DEXIN_STB = IS_DEXIN_AML8726_STB || IS_DEXIN_S805_STB;

    public static final boolean IS_CHUANGWEI = Build.BRAND.contentEquals("Skyworth")
            || Build.MANUFACTURER.contentEquals("Skyworth Group Co., Ltd");

    public static final boolean IS_CHANGHONG = Build.BRAND.contentEquals("ChangHong")
            || Build.MANUFACTURER.contentEquals("ChangHong");

    private static final String SD_PATH = "/storage/external_storage/sdcard1";
    private static final String USB_PATH = "/storage/external_storage";
    private static final String SATA_PATH = "/storage/external_storage/sata";

    private static final int CONFIG_TYPE_TIME = 0x10001;
    private static final int CONFIG_TYPE_LANGUAGE = 0x10002;
    private static final String ACTION_DEXIN_CHANGE_CONFIG = "dexin.intent.CHANGE_CONFIG";

    private static final String IPTV_PACKAGE = "com.dexin.iptv2";

    // IP正则表达式
    private static final String PATTERN_IP = "([\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3})";
    // IP带端口的正则表达式
    private static final String PATTERN_IP_PORT = "([\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3})\\:([\\d]{1,5})";

    public static String hostIp;
    public static String hostMac;

    private static final char HEX_CHAR_ARRAY[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};

    public static String MD5Format(String source) {
        if (source == null) {
            return "";
        }
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte[] md5Byte = md.digest();

            char resultChar[] = new char[md5Byte.length * 2];
            int k = 0;
            for (int i = 0; i < md5Byte.length; i++) {
                resultChar[k++] = HEX_CHAR_ARRAY[md5Byte[i] >>> 4 & 0xf];
                resultChar[k++] = HEX_CHAR_ARRAY[md5Byte[i] & 0xf];
            }
            result = new String(resultChar);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int getKeyNumber(int keyCode) {
        int key = 0;
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                key = 0;
                break;
            case KeyEvent.KEYCODE_1:
                key = 1;
                break;
            case KeyEvent.KEYCODE_2:
                key = 2;
                break;
            case KeyEvent.KEYCODE_3:
                key = 3;
                break;
            case KeyEvent.KEYCODE_4:
                key = 4;
                break;
            case KeyEvent.KEYCODE_5:
                key = 5;
                break;
            case KeyEvent.KEYCODE_6:
                key = 6;
                break;
            case KeyEvent.KEYCODE_7:
                key = 7;
                break;
            case KeyEvent.KEYCODE_8:
                key = 8;
                break;
            case KeyEvent.KEYCODE_9:
                key = 9;
                break;
        }
        return key;
    }

    public static List<ApplicationInfo> getApps(Context context, boolean filterInstall) {
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = manager.queryIntentActivities(intent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        List<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();
        if (apps != null) {
            final int count = apps.size();
            if (applications == null) {
                applications = new ArrayList<ApplicationInfo>(count);
            }
            applications.clear();

            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);
                application.title = info.loadLabel(manager);
                application.setActivity(
                        new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name),
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                try {
                    application.icon = manager.getDrawable(info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.applicationInfo.icon, null);
                } catch (Exception e) {
                }
                if (application.icon == null)
                    application.icon = info.loadIcon(manager);
                String pkgName = info.activityInfo.applicationInfo.packageName;
                if (!(pkgName.equals(IPTV_PACKAGE) || pkgName.equals("com.example.Upgrade")
                        || pkgName.equals("com.amlapp.update.otaupgrade"))) {
                    if (!filterInstall || !(pkgName.equals("com.gsoft.appinstall")
                            || pkgName.equals("com.fb.FileBrower") || pkgName.equals("com.android.vending"))) {
                        applications.add(application);
                    }
                }
            }
        }
        apps.clear();
        return applications;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static boolean isEthernetOn(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        return info.isConnected();
    }

    public static boolean isWlanOn(Context context) {
        if (IS_AVD)
            return true;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info.isConnected();
    }

    @SuppressWarnings("deprecation")
    public static boolean isNetOn(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo eth = connectivity.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (eth != null && eth.isConnected()) || (wifi != null && wifi.isConnected())
                || (mobile != null && mobile.isConnected());
    }

    public static boolean onInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressWarnings("deprecation")
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static boolean isUsbExists() {
        File dir = new File(USB_PATH);
        if (dir.exists() && dir.isDirectory()) {
            if (dir.listFiles() != null) {
                if (dir.listFiles().length > 0) {
                    for (File file : dir.listFiles()) {
                        String path = file.getAbsolutePath();
                        if (path.startsWith(USB_PATH + "/sd") && !path.equals(SD_PATH)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isSataExists() {
        File dir = new File(SATA_PATH);
        return dir.exists() && dir.isDirectory();
    }

    public static boolean isSdcardExists() {
        if (Environment.getExternalStorageState().startsWith(Environment.MEDIA_MOUNTED)) {
            File dir = new File(SD_PATH);
            return dir.exists() && dir.isDirectory();
        }
        return false;
    }

    /**
     * 从RTSP URL中解析RTSP服务器IP端口
     *
     * @return
     */
    public static InetSocketAddress parseRemoteAddress(String url) {
        String ip = "";
        int port = IptvConfig.DEFAULT_RTSP_PORT;
        Pattern p = Pattern.compile(PATTERN_IP_PORT);
        Matcher m = p.matcher(url);
        if (m.find()) {
            ip = m.group(1);
            port = Integer.valueOf(m.group(2));
        } else {
            p = Pattern.compile(PATTERN_IP);
            m = p.matcher(url);
            if (m.find()) {
                ip = m.group();
            }
        }
        return new InetSocketAddress(ip, port);
    }

    /**
     * 转化毫秒时间为 hh:mm:ss或者mm:ss格式
     *
     * @param ms
     * @return
     */
    public static String formatPlayTime(int ms) {
        int seconds = ms / 1000;
        Format format = new DecimalFormat("00");
        String hour = format.format(seconds / 3600);
        String minute = format.format(seconds % 3600 / 60);
        String second = format.format(seconds % 60);
        if (Integer.valueOf(hour) == 0) {
            return minute + ":" + second;
        } else {
            return hour + ":" + minute + ":" + second;
        }
    }

    /**
     * 获得版本号
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(IptvConfig.IPTV_PACKAGE, 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e("版本号获取异常", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获得版本名称
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(IptvConfig.IPTV_PACKAGE, 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("版本号获取异常", e.getMessage());
        }
        return verName;
    }

    /**
     * Get the Ehternet MacAddress
     *
     * @return
     */
    private static String getEthMacAddress(Context context) {
        String macAddress = "";
        SharedPreferences sharedPreferences = context.getSharedPreferences("address", Context.MODE_PRIVATE);
        try {
            // macAddress =
            // FileUtil.loadFileAsString("/sdcard/IPTV/address/mac").toUpperCase();
            macAddress = sharedPreferences.getString("mac", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (macAddress == null || macAddress.length() < 17) {
            try {
                macAddress = FileUtil.loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (macAddress != null && macAddress.length() >= 17) {
                // FileUtil.writeStringToFile("/sdcard/IPTV/address/mac",
                // macAddress);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mac", macAddress);
                editor.commit();
            }
        }
        return macAddress;
    }

    /**
     * Get the WLAN MacAddress
     *
     * @return
     */
    private static String getWlanMacAddress() {
        String macAddress = "";
        try {
            macAddress = FileUtil.loadFileAsString("/sys/class/net/wlan0/address").toUpperCase().substring(0, 17);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    /**
     * Get the MacAddress
     *
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddress = "";
        if (IS_STB) {
            macAddress = IptvUtil.getEthMacAddress(context);
        } else {
            macAddress = hostMac;
            // macAddress = IptvUtil.getWlanMacAddress();
        }
        return macAddress;
    }

    public static String getLocalIpAddress() {
        String ethAddress = null;
        String wlanAddress = null;
        if (!IS_STB)
            return hostIp;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("eth")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipAddress = inetAddress.getHostAddress();
                            if (ipAddress.contains(".")) {
                                ethAddress = ipAddress;
                                break;
                            }
                        }
                    }
                } else if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipAddress = inetAddress.getHostAddress();
                            if (ipAddress.contains(".")) {
                                wlanAddress = ipAddress;
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ethAddress != null)
            return ethAddress;
        else
            return wlanAddress;
    }

    public static String getLocalEthIpAddress() {
        String ethAddress = null;
        if (!IS_STB)
            return hostIp;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("eth")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipAddress = inetAddress.getHostAddress();
                            if (ipAddress.contains(".")) {
                                ethAddress = ipAddress;
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ethAddress;
    }

    public static String getLocalWlanIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (!intf.getName().contains("wlan"))
                    continue;
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipAddress = inetAddress.getHostAddress();
                        if (ipAddress.contains("."))
                            return ipAddress;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个随机数字符串
     */
    public static String getRandomString() {
        Random r = new Random();
        return Math.abs(r.nextInt()) + "";
    }

    /**
     * 获取一个指定长度的随机数字符串
     *
     * @param length 指定字符串的长度
     */
    public static String getRandomString(int length) {
        Random r = new Random();
        String result = String.format("%0" + length + "d", Math.abs(r.nextInt()));
        return result.substring(result.length() - length);
    }

    public static List<ResolveInfo> getAppResolves(Context context, boolean filterInstall) {
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = manager.queryIntentActivities(intent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        List<ResolveInfo> removeList = new ArrayList<ResolveInfo>();
        if (apps != null) {
            for (ResolveInfo r : apps) {
                String pkgName = r.activityInfo.applicationInfo.packageName;
                if (pkgName.equals(IPTV_PACKAGE) || pkgName.equals("com.mbx.settingsmbox")
                        || pkgName.equals("com.example.Upgrade") || pkgName.equals("com.amlapp.update.otaupgrade")) {
                    removeList.add(r);
                }
                if (filterInstall && (pkgName.equals("com.gsoft.appinstall") || pkgName.equals("com.fb.FileBrower")
                        || pkgName.equals("com.android.vending"))) {
                    removeList.add(r);
                }
            }
            apps.removeAll(removeList);
        }
        return apps;
    }

    // /**
    // * 根据地址下载APK
    // *
    // * @param url
    // */
    // public static void downloadAPK(String url, String savedName) {
    // HttpClient client = new DefaultHttpClient();
    // HttpGet get = new HttpGet(url);
    // HttpResponse response;
    // try {
    // response = client.execute(get);
    // HttpEntity entity = response.getEntity();
    // InputStream is = entity.getContent();
    // FileOutputStream fos = null;
    // if (is != null) {
    // File file = new File(Environment.getExternalStorageDirectory(),
    // savedName);
    // fos = new FileOutputStream(file);
    // byte[] b = new byte[1024];
    // int charb = -1;
    // while ((charb = is.read(b)) != -1) {
    // fos.write(b, 0, charb);
    // }
    // }
    // fos.flush();
    // if (fos != null) {
    // fos.close();
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * 安装应用
     *
     * @param context
     */
    public static void installAPK(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        File file = new File(apkPath);
        if (Build.VERSION.SDK_INT >= 24) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);

        //		Intent intent = new Intent(Intent.ACTION_VIEW);
        //		intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        //		context.startActivity(intent);
    }

    /**
     * 设置ListView的宽度随子项item的最大宽度变化
     *
     * @param listView
     * @param minWidth ListView最小宽度
     */
    public static void setListViewWidthBasedOnChildren(ListView listView, int minWidth) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int maxWidth = minWidth;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, listView);
            item.measure(0, 0);
            if (maxWidth < item.getMeasuredWidth()) {
                maxWidth = item.getMeasuredWidth();
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.width = maxWidth;
        listView.setLayoutParams(params);
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public static boolean hasEthernet() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("eth")) {
                    return true;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setSystemTime(Context context, long millis) {
        Intent intent = new Intent();
        intent.setAction(ACTION_DEXIN_CHANGE_CONFIG);
        intent.putExtra("type", CONFIG_TYPE_TIME);
        intent.putExtra("time", millis);
        context.sendBroadcast(intent);
    }

    public static void setSystemLanguage(Context context, String language) {
        Intent intent = new Intent();
        intent.setAction(ACTION_DEXIN_CHANGE_CONFIG);
        intent.putExtra("type", CONFIG_TYPE_LANGUAGE);
        intent.putExtra("language", language);
        context.sendBroadcast(intent);
    }

}
