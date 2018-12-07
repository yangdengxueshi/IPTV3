package com.dexin.wanchuan.iptv3.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static String readFileAsString(String path) {
        File file = new File(path);
        String data = null;
        if (file.exists()) {
            try {
                char[] seriesNumber = new char[48];
                FileReader fr = new FileReader(file);
                fr.read(seriesNumber);
                fr.close();
                data = new String(seriesNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String loadFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public static void writeStringToFile(String path, String data) {
        String oldString = readFileAsString(path);
        if (data != null && oldString != null && data.contentEquals(oldString))
            return;
        File file = new File(path);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            fw.write(data);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
