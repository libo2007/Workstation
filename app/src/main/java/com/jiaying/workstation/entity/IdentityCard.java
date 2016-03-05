package com.jiaying.workstation.entity;

import android.graphics.Bitmap;
import android.provider.SearchRecentSuggestions;

import java.io.Serializable;

/**
 * 作者：lenovo on 2016/3/4 17:09
 * 邮箱：353510746@qq.com
 * 功能：省份证实体
 */
public class IdentityCard implements Serializable {
    private String name;
    private String sex;
    private String nation;
    private String year;
    private String mouth;
    private String day;
    private String addr;
    private String idcardno;
    private String grantdept;
    private Bitmap photoBmp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMouth() {
        return mouth;
    }

    public void setMouth(String mouth) {
        this.mouth = mouth;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIdcardno() {
        return idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public String getGrantdept() {
        return grantdept;
    }

    public void setGrantdept(String grantdept) {
        this.grantdept = grantdept;
    }

    public Bitmap getPhotoBmp() {
        return photoBmp;
    }

    public void setPhotoBmp(Bitmap photoBmp) {
        this.photoBmp = photoBmp;
    }

    @Override
    public String toString() {
        return "IdentityCard{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", year='" + year + '\'' +
                ", mouth='" + mouth + '\'' +
                ", day='" + day + '\'' +
                ", addr='" + addr + '\'' +
                ", idcardno='" + idcardno + '\'' +
                ", grantdept='" + grantdept + '\'' +
                ", photoBmp=" + photoBmp +
                '}';
    }
}
