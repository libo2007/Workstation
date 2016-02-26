package com.jiaying.workstation.constant;

/**
 * 作者：lenovo on 2016/2/22 13:27
 * 邮箱：353510746@qq.com
 * 功能：类型常量
 */
public class TypeConstant {
    //登记
    public static final int  TYPE_REG = 1;
    //采浆
    public static final int  TYPE_BLOODPLASMACOLLECTION = 2;
    //体检
    public static final int TYPE_PHYSICAL_EXAM = 3;
    //查询
    public static final int TYPE_SEARCH = 5;


    //登记完成
    public static final int STATE_REG_OVER = 1001;
    //待体检
    public static final int STATE_PHYSICAL_EXAM_TODO = 1002;
    //体检中
    public static final int STATE_PHYSICAL_EXAMING = 1003;
    //体检通过
    public static final int STATE_PHYSICAL_EXAM_PASS = 1004;
    //体检未通过
    public static final int STATE_PHYSICAL_EXAM_NOT_PASS = 1005;
    //待采浆
    public static final int STATE_BLOODPLASMA_COLLECTION_TODO = 1006;
    //采浆中
    public static final int STATE_BLOODPLASMA_COLLECTING = 1007;
    //采浆完成
    public static final int STATE_BLOODPLASMA_COLLECTION_OVER = 1008;


}
