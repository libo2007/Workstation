package com.jiaying.workstation.entity;

import java.io.Serializable;

/**
 * 作者：lenovo on 2016/2/25 21:02
 * 邮箱：353510746@qq.com
 * 功能：献浆员用户的信息
 */
public class User implements Serializable {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
