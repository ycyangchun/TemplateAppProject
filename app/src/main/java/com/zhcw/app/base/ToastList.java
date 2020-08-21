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

import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.DiskCache;
import com.xuexiang.xaop.cache.XDiskCache;
import com.zhcw.lib.base.bean.BaseBean;
import com.zhcw.lib.base.bean.ToastBean;
import com.zhcw.lib.utils.JsonUtils;
import com.zhcw.lib.utils.ZhcwUtils;

import java.util.HashMap;

public class ToastList {

    /**
     * 实例
     *  Logger.d(Constants.getMapValue("DC101062","默认key 11111111111111111"));
     *  ToastList.getInstance().updateToast();// 更新 ToastList
     *  Logger.d(Constants.getMapValue("DC101059","默认key 22"));
     */
    private final String toastName = "toastlist.txt";

    private static class Instance {
        private static final ToastList INSTANCE = new ToastList();
    }

    public static ToastList getInstance() {
        return ToastList.Instance.INSTANCE;
    }

    /**
     * 读取 toastList
     * zhcwUtils 保存，便于查看用
     * DiskCache 应用中使用
     * @return
     */
    @DiskCache("toastMap")
    public HashMap<String ,String> getToastMap(){
        return initToastList(toastName);
    }

    /**
     * 更新 toastList
     * 清除 DiskCache ，再次 getToastMap 就是最新数据
     */
    public void updateToast(){
        Constants.toastBean = null;
        XDiskCache.getInstance().remove("toastMap");
    }

    /**
     * 更新 toastList
     * 清除 DiskCache ，再次 getToastMap 就是最新数据
     * @param toastList
     */
    public void updateToast(String toastList){
        if(null != toastList){
            ZhcwUtils.getInstance().readCacheFile(toastName);
        }
        updateToast();
    }

    /**
     * toastList  map
     *
     * @param toastPath
     * @return
     */
    public HashMap<String, String> initToastList(String toastPath) {
        //从ZhcwUtils读取
        String toastStr = ZhcwUtils.getInstance().readCacheFile(toastPath);
        ToastBean toastBean = JsonUtils.unifiedBeanToBody(toastStr,
                new TypeToken<BaseBean<ToastBean>>() {}.getType());
        return toastBean.listToMap();
    }
}
