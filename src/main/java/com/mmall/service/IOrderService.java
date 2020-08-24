package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * Created By Zzyy
 **/
public interface IOrderService {

    ServerResponse pay(Long orderNumber, Integer userId, String path);

    ServerResponse aliCallBack(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNumber);
}
