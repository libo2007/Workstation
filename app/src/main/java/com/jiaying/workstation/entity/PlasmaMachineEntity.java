package com.jiaying.workstation.entity;

import java.io.Serializable;

/**
 * 作者：lenovo on 2016/1/20 09:26
 * 邮箱：353510746@qq.com
 * 功能：采浆机实体
 */
public class PlasmaMachineEntity implements Serializable {
    private String id;
    private String number;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
