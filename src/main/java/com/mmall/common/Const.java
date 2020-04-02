package com.mmall.common;

/**
 * Created By Zzyy
 **/
public class Const {

    public static final String CURRENT_USER="currentUser";

    public static final String EMAIL="email";

    public static final String USERNAME="username";

    //普通用户和管理员是一个组，可以用枚举但过于繁重，所以用内部的一个接口类进行分组
    public interface Role{
        int ROLE_CUSTOMER=0;//普通用户
        int ROLE_ADMIN=1;//管理员
    }
}
