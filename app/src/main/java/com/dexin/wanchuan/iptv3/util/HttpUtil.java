package com.dexin.wanchuan.iptv3.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String urlAddress, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlAddress)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendHttpURLConnectionRequest(final String urlString, final HttpURLConnectionCallbackListener callbackListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    if (callbackListener != null) {
                        callbackListener.onResponse(response.toString());
                    }
                } catch (Exception e) {
                    if (callbackListener != null) {
                        callbackListener.onFailure(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

}

interface HttpURLConnectionCallbackListener {
    void onResponse(String response);

    void onFailure(Exception e);
}
