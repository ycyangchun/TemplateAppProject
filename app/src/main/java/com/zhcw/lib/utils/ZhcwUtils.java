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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.google.gson.reflect.TypeToken;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.file.FileIOUtils;
import com.xuexiang.xutil.resource.ResourceUtils;
import com.xuexiang.xutil.tip.ToastUtils;
import com.zhcw.app.App;
import com.zhcw.lib.base.bean.BaseBean;
import com.zhcw.lib.base.bean.ToastBean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;


public class ZhcwUtils {
    private Context mContext;

    private ZhcwUtils() {
    }

    private static class Instance {
        private static final ZhcwUtils INSTANCE = new ZhcwUtils();
    }

    public static ZhcwUtils getInstance() {
        return Instance.INSTANCE;
    }

    public ZhcwUtils initZhcwUtilsContext(Context context) {
        mContext = context;
        return this;
    }

    public ZhcwUtils initZhcwUtilsContext() {
        mContext = App.getAppContext();
        return this;
    }

    ////////////// 加载cache /////////////////
    /**  示例
     ZhcwUtils zhcwUtils =  ZhcwUtils.getInstance();
     zhcwUtils.writeCacheFile("txt/1.txt","111111112222");
     zhcwUtils.writeAssetsCacheFile("ds/k3/t1.txt");
     zhcwUtils.writeAssetsCacheFile("ds/t2.txt");
     LogMgr.d(zhcwUtils.readCacheFile("ds/t3.txt"));
     **/

    /**
     * 文本写入cache
     *
     * @param filePath
     * @param content
     */
    public void writeCacheFile(String filePath, String content) {
        writeCacheFile(filePath, content, false);
    }

    /**
     * 文本写入cache
     *
     * @param filePath
     * @param content
     * @param overwrite 是否重复写入
     */
    public void writeCacheFile(String filePath, String content, boolean overwrite) {
        try {
            String cacheFilePath = FileUtilSupply.getCacheFilePath(filePath);
            File file = new File(cacheFilePath);
            if (file.exists() && !overwrite) {
                long size = FileUtilSupply.getFileSize(file);
                if (size > 0 && size == content.length()) {
                    Logger.d(filePath + " -->已存在 " + size);
                    return;
                }
            }
            FileIOUtils.writeFileFromString(cacheFilePath, content);
            Logger.d(filePath + " -->写入cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * assets 写入cache
     *
     * @param assetsPath
     */
    public void writeAssetsCacheFile(String assetsPath) {
        writeAssetsCacheFile(assetsPath, false);
    }

    /**
     * assets 写入cache
     *
     * @param assetsPath
     * @param overwrite  是否重复写入
     */
    public void writeAssetsCacheFile(String assetsPath, boolean overwrite) {
        try {
            String cacheFilePath = FileUtilSupply.getCacheFilePath(assetsPath);
            File file = new File(cacheFilePath);
            String content = ResourceUtils.readStringFromAssert(assetsPath);
            if (file.exists() && !overwrite) {
                long size = FileUtilSupply.getFileSize(file);
                if (size > 0 && size == content.length()) {
                    Logger.d(assetsPath + " -->assets已存在 " + size);
                    return;
                }
            }

            FileIOUtils.writeFileFromString(cacheFilePath,content);
            Logger.d(assetsPath + " -->assets写入cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取cache 数据
     *
     * @param filePath
     * @return
     */
    public String readCacheFile(String filePath) {
        String content = "";
        try {
            String cacheFilePath = FileUtilSupply.getCacheFilePath(filePath);
            File file = new File(cacheFilePath);
            if (file.exists()) {
                long size = FileUtilSupply.getFileSize(file);
                if (size > 0) {
                    Logger.d(filePath + " -->cache已存在 " + size);
                    content = FileIOUtils.readFile2String(cacheFilePath);
                }
            } else {
                Logger.d(filePath + " -->cache不存在");
                content = ResourceUtils.readStringFromAssert(filePath);
                FileIOUtils.writeFileFromString(cacheFilePath, content);
                Logger.d(filePath + " -->cache不存在，读取时写入cache");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return content;
        }

    }

    /**  示例 toastlist
     ZhcwUtils zhcwUtils =  ZhcwUtils.getInstance();
     Constants.toastBean = zhcwUtils.initToastList("toastlist.txt");
     Logger.d(Constants.getMapValue(Constants.toastBean, "","11"));
     Logger.d(Constants.getMapValue(Constants.toastBean, "DC101059","22"));
     **/

    /**
     * toastList  map
     *
     * @param toastPath
     * @return
     */
    public HashMap<String, String> initToastList(String toastPath) {
        String toastStr = readCacheFile(toastPath);
        ToastBean toastBean = JsonUtils.unifiedBeanToBody(toastStr,
                new TypeToken<BaseBean<ToastBean>>() {}.getType());
        return toastBean.listToMap();
    }


    ////////////// 加载cache /////////////////


    /**
     * 保存图片
     *
     * @param
     */
    public void saveBmp2Gallery(Context context, String imgByte) {
        try {
            byte[] bitmapArray = Base64.decode(imgByte, Base64.DEFAULT);
            InputStream input;
            Bitmap bitmap;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;// 1/8
            input = new ByteArrayInputStream(bitmapArray);
            SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                    input, null, options));
            bitmap = (Bitmap) softRef.get();
            if (imgByte != null) {
                imgByte = null;
            }
            if (input != null) {
                input.close();
            }

//            Bitmap bitmap = null;
//            try {
//                byte[] bitmapArray = Base64.decode(imgByte, Base64.DEFAULT);
//                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            ///////////
            String fileName;
            File file;
            String bitName = System.currentTimeMillis() + ".png";
            if (Build.BRAND.equals("Xiaomi")) { // 小米手机
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
            } else {  // Meizu 、Oppo
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
            }
            file = new File(fileName);

            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out;

            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }
            ToastUtils.toast("保存成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastUtils.toast("保存失败");
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.toast("保存失败");
        }

    }

}
