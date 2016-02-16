package com.jiaying.workstation.entity;

import java.io.Serializable;

/**
 * 作者：lenovo on 2016/1/20 00:09
 * 邮箱：353510746@qq.com
 * 功能：护士信息
 */
public class Nurse implements Serializable{
    private String url;
    private String name;
    private String workid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkid() {
        return workid;
    }

    public void setWorkid(String workid) {
        this.workid = workid;
    }
}
