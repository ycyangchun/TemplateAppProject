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

package com.zhcw.app.utils;

import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.DiskCache;
import com.xuexiang.xaop.cache.XDiskCache;
import com.zhcw.lib.base.bean.BaseBean;
import com.zhcw.lib.base.bean.ToastBean;
import com.zhcw.lib.utils.JsonUtils;
import com.zhcw.lib.utils.ZhcwUtils;

import java.util.HashMap;

public class ToastListUtil {

    /**
     * 实例
     *  Logger.d(ToastList.getInstance().getMapValue("DC101062","默认key 11111111111111111"));
     *  ToastList.getInstance().removeToast();// 更新 ToastList
     *  Logger.d(ToastList.getInstance().getMapValue("DC101059","默认key 22"));
     */
    private final String toastName = "toastlist.txt";
    private static final String KEY_TOAST = "toastMap";
    private static HashMap<String,String> toastBean;// toast
    private static String timeId = "-1";

    private static class Instance {
        private static final ToastListUtil INSTANCE = new ToastListUtil();
    }

    public static ToastListUtil getIT() {
        return ToastListUtil.Instance.INSTANCE;
    }

    public static String getTimeId() {
        return timeId;
    }

    /**
     * 读取 toastList
     * zhcwUtils 保存，便于查看用
     * DiskCache("toastMap") 应用中使用，
     * 调用方法时，如果缓存中没有则会创建并保存到 DiskCache
     * @return
     */
    @DiskCache(KEY_TOAST)
    public HashMap<String ,String> initToastMap(){
        return initToastList(toastName);
    }

    /**
     * 更新 toastList
     * 清除 DiskCache ，再次 getToastMap 就是最新数据
     */
    public void removeToast(){
        toastBean = null;
        XDiskCache.getInstance().remove(KEY_TOAST);
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
        timeId = toastBean.getTimeId();
        return toastBean.listToMap();
    }




    /**
     *  获取 key --> value
     * @param mapK
     * @param defaultV
     * @return
     */
    public String getMV(String mapK ,String defaultV){
        if(null == toastBean){
            toastBean = ToastListUtil.getIT().initToastMap();
        }
        return getMapValue(toastBean,mapK,defaultV);
    }

    /**
     *  获取 key --> value
     * @param map
     * @param mapK
     * @param defaultV
     * @return
     */
    public String getMapValue(HashMap<String,String> map,String mapK ,String defaultV){
        String v = null;
        if(null != map){
            v = map.get(mapK);
        }
        if(null == v){
            v = defaultV;
        }
        return v;
    }
}
