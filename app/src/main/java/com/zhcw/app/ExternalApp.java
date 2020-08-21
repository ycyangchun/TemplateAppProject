/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
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
 */

package com.zhcw.app;

import android.app.Application;

import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.annotation.DataBase;
import com.xuexiang.xormlite.enums.DataBaseType;
import com.zhcw.lib.db.ExternalDataBase;

/**
 * <pre>
 *     desc   : 外部存储的数据库
 *     author : xuexiang
 *     time   : 2018/5/9 下午11:44
 * </pre>
 */
@DataBase(name = "external", type = DataBaseType.EXTERNAL, path = "/storage/emulated/0/zhcwLib/databases")
public class ExternalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ExternalDataBaseRepository.getInstance()
                .setIDatabase(new ExternalDataBase(ExternalDataBaseRepository.DATABASE_PATH, ExternalDataBaseRepository.DATABASE_NAME, ExternalDataBaseRepository.DATABASE_VERSION))
                .init(this);

    }
}
