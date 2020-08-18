/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zhcw.lib.utils;

import android.content.Context;
import android.text.TextUtils;

import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.file.FileUtils;
import com.zhcw.app.App;
import com.zhcw.app.base.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 补充 com.xuexiang.xutil.file.FileUtils
 */
public class FileUtilSupply{

    //应用自定义存储目录
    private static String ROOT_PATH ;
    private static final String CACHE = "cache";
    private static final String IMAGE = "img";
    private static final String CACHEWEB = "webCache";

    private static File APP_CACHE_FILE ;
    private static String APP_CACHE_PATH;
    private static File APP_IMAGE_FILE ;
    private static String APP_IMAGE_PATH;
    public static File APP_CACHEWEB_FILE ;
    public static String APP_CACHEWEB_PATH;
    public static String APP_INNER_PATH;

    public static void initFileUtils(){
        ROOT_PATH = FileUtils.getDiskDir(Constants.packageName);
    }
    /**
     * 在初始化时创建APP所需要的基础文件夹
     * 在6.0以上版本时需要进行权限申请
     */
    public static void initCache() {
        if(ROOT_PATH == null) throw new UnsupportedOperationException("FileUtils initFileUtils()");
        APP_CACHE_FILE = createFileDir(CACHE);
        APP_CACHE_PATH = APP_CACHE_FILE.getPath();
    }

    public static void initImg() {
        if(ROOT_PATH == null) throw new UnsupportedOperationException("FileUtils initFileUtils()");
        APP_IMAGE_FILE = createFileDir(IMAGE);
        APP_IMAGE_PATH = APP_IMAGE_FILE.getPath();
    }

    public static void innerCache() {
        APP_CACHEWEB_FILE = createFileDirInner(CACHEWEB);
        APP_CACHEWEB_PATH = APP_CACHEWEB_FILE.getPath();
    }

    /*
     * 创建目录
     *
     * @param context
     * @param dirName 文件夹名称
     * @return
     */
    public static File createFileDir(String dirName) {
        // 如SD卡已存在，则存储；反之存在data目录下
        String filePath = ROOT_PATH + File.separator + dirName;
        boolean create = FileUtils.createOrExistsDir(filePath);
        Logger.eTag("FileUtils", filePath + " has created. " + create);
        File destDir = new File(filePath);
        return destDir;
    }

    public static File createFileDirInner(String dirName) {
        // 存在data/data/目录下
        APP_INNER_PATH = App.getAppContext().getCacheDir().getPath();
        String filePath = APP_INNER_PATH + File.separator + dirName;
        boolean create = FileUtils.createOrExistsDir(filePath);
        Logger.eTag("FileUtils", filePath + " has created. " + create);
        File destDir = new File(filePath);
        return destDir;
    }

    // 本应用文件路径 xx/xx/cache/file/fileName
    public static String getCacheFilePath(String fileName){
        if(APP_CACHE_PATH == null) throw new UnsupportedOperationException("FileUtils initCache()");
        File f = new File(APP_CACHE_PATH);
        if(!f.exists()){
            initCache();// 目录被删除后重建目录
        }
        needMkdirs(APP_CACHE_PATH,fileName);
        return  APP_CACHE_PATH + File.separator + fileName;
    }



    // 本应用文件路径 data/data/app/cache/webCache/fileName
    public static String getInnerCacheFilePath(String fileName){
        if(APP_CACHEWEB_PATH == null) throw new UnsupportedOperationException("FileUtils innerCache()");
        File f = new File(APP_CACHEWEB_PATH);
        if(!f.exists()){
            innerCache();// 目录被删除后重建目录
        }
        needMkdirs(APP_CACHEWEB_PATH,fileName);
        return  APP_CACHEWEB_PATH + File.separator + fileName;
    }


    public static boolean needMkdirs(String cache_path,String fileName) {
        if (fileName == null) return false;
        fileName =  cache_path + File.separator + fileName ;
        File f = new File(fileName);
        if(!f.exists()) {
            String[] Lookup = fileName.split("/");
            String path = "";
            for (int i = 0; i < Lookup.length; i++) {
                if (TextUtils.isEmpty(Lookup[i])) continue;
                if (i == Lookup.length - 1) continue;

                path = path + "/" + Lookup[i];
                // 判断是否存在这个路径是否存在这个文件
                File existFolder = new File(path);
                if (!existFolder.exists()) {
                    existFolder.mkdirs();
                }
            }
        }
        return true;
    }

    /**
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    int num = subFiles.length;
                    for (int i = 0; i < num; i++) {
                        size += getFileSize(subFiles[i]);
                    }
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 获取系统内存
     *
     * @param context
     * @param type
     * @return
     */
    public static String getMemInfoIype(Context context, String type) {
        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 4 * 1024);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                if (str.contains(type)) {
                    break;
                }
            }
            bufferedReader.close();
            /* \\s表示   空格,回车,换行等空白符,
            +号表示一个或多个的意思     */
            String[] array = str.split("\\s+");
            // 获得系统总内存，单位是KB，乘以1024转换为Byte
            long length = Long.valueOf(array[1]).longValue() * 1024;
            return android.text.format.Formatter.formatFileSize(context, length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
