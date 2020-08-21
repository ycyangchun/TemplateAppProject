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

package com.zhcw.app.base;

import com.xuexiang.xaop.annotation.DiskCache;
import com.xuexiang.xaop.cache.XDiskCache;
import com.zhcw.lib.utils.ZhcwUtils;

import java.util.HashMap;

public class ToastList {

    public static String toastName = "toastlist.txt";
    /**
     * 读取 toastList
     * zhcwUtils 保存，便于查看用
     * DiskCache 应用中使用
     * @return
     */
    @DiskCache("toastMap")
    public static HashMap<String ,String> getToastMap(){
        return ZhcwUtils.getInstance().initToastList(toastName);
    }

    /**
     * 更新 toastList
     * 清除 DiskCache ，再次 getToastMap 就是最新数据
     */
    public static void updateToast(){
        Constants.toastBean = null;
        XDiskCache.getInstance().remove("toastMap");
    }

    /**
     * 更新 toastList
     * 清除 DiskCache ，再次 getToastMap 就是最新数据
     * @param toastList
     */
    public static void updateToast(String toastList){
        if(null != toastList){
            ZhcwUtils.getInstance().readCacheFile(toastName);
        }
        updateToast();
    }
}
