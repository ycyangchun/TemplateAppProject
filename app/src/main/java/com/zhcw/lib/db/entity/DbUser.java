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

package com.zhcw.lib.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import androidx.annotation.NonNull;

@DatabaseTable(tableName = "dbUser")
public class DbUser {

    @DatabaseField(generatedId = true)
    private long Id;
    @DatabaseField(columnName = "psw")
    private String password;
    @DatabaseField(columnName = "mobile")
    private String mobile;
    @DatabaseField(columnName = "type")
    private int type;

    public long getId() {
        return Id;
    }

    public DbUser setId(long id) {
        Id = id;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DbUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public DbUser setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public int getType() {
        return type;
    }

    public DbUser setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "DbUser{" +
                "Id=" + Id +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", type=" + type +
                '}';
    }
}
