package com.mmall.common;

/**
 * Created By Zzyy
 **/
public enum ResponseCode {
    //枚举代替常量简化工作
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEEDLOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEAGAL_ARGUMENT");

    private final int code;
    private final String desc;

    //构造器
    ResponseCode(int code, String desc) {
        this.code=code;
        this.desc=desc;
    }

    //把code和desc开放出去
    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }




}
